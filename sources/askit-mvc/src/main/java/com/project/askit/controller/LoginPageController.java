package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.model.EmailModel;
import com.project.askit.model.LoginModel;
import com.project.askit.model.MessageModel;
import com.project.askit.model.ResetPasswordRequestModel;
import com.project.askit.rest.api.EmailRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Crypto;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final EmailRestApi emailRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public LoginPageController(UserRestApi userRestApi,
                               EmailRestApi emailRestApi,
                               EventLogRestApi eventLogRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.emailRestApi = emailRestApi;
        this.eventLogRestApi = eventLogRestApi;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        // add initBinder to convert/trim input strings
        // remove leading and trailing whitespace
        // resolve validation issue

        // true: trim whitespace all the way to null
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @RequestMapping("/login")
    public Object showLoginPage(Model model,
                                HttpServletRequest request) {
        // Check if user is logged in
        if (isLoggedIn(request)) return new RedirectView("/home", true);

        // Add data to model
        if (model.getAttribute("loginModel") == null) model.addAttribute("loginModel", new LoginModel());

        return "login";
    }

    @RequestMapping("/login/process-form")
    public Object processLoginForm(HttpServletRequest request,
                                   @Valid @ModelAttribute("loginModel") LoginModel loginModel,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        if (isLoggedIn(request)) return new RedirectView("/home", true);

        try {
            // Check data invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get user by email
            User user = userRestApi.findByEmail(loginModel.getEmail());
            if (user == null) throw new Exception(INVALID_CREDENTIALS);

            // Extra validation: check passwords
            if (!user.getPassword().equals("{noop}" + loginModel.getPassword())) throw new Exception(INVALID_CREDENTIALS);

            // Extra validation: check account status
            if (user.getStatus() != 1) throw new Exception(INVALID_ACCOUNT);

            // Add user to session
            request.getSession().setAttribute("user", user);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.SELECT);
            log.setInfo("Logged in user with id " + user.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(INVALID_CREDENTIALS)) handleInvalidCredentials(redirectAttributes);
            else if (error.equals(INVALID_ACCOUNT)) handleInvalidAccount(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("loginModel", loginModel);
        }

        return new RedirectView("/login", true);
    }

    @RequestMapping("/login/reset-password")
    public Object showResetPasswordPage(Model model) {
        if (model.getAttribute("resetPasswordRequestModel") == null) model.addAttribute("resetPasswordRequestModel", new ResetPasswordRequestModel());
        return "reset-password-request";
    }

    @RequestMapping("/login/reset-password/process-form")
    public Object processResetPasswordForm(@Valid @ModelAttribute("resetPasswordRequestModel") ResetPasswordRequestModel resetPasswordRequestModel,
                                           BindingResult result,
                                           RedirectAttributes redirectAttributes) {
        try {
            // Check data invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get user by email
            User user = userRestApi.findByEmail(resetPasswordRequestModel.getEmail());
            if (user == null) {
                emailRestApi.sendEmail(new EmailModel(resetPasswordRequestModel.getEmail(), null, EmailModel.INFORM_EMAIL));
            } else {
                // Generate link
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                String key = user.getId() + EmailModel.DELIMITER + System.currentTimeMillis();
                String encryptedKey = Crypto.encrypt(key, Crypto.KEY);

                // Create email
                EmailModel emailModel = new EmailModel();
                emailModel.setDestination(resetPasswordRequestModel.getEmail());
                emailModel.setType(EmailModel.RESET_PASSWORD_EMAIL);
                emailModel.setLink(baseUrl + "/" + emailModel.getType() + "?key=" + URLEncoder.encode(encryptedKey, StandardCharsets.UTF_8));

                // Send email
                emailRestApi.sendEmail(emailModel);
            }

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "A reset password request has been sent to your email address"));
            details.add(new Pair<>("Note", "that the link inside the email will expire after 24 hours"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("resetPasswordRequestModel", resetPasswordRequestModel);
        }

        return new RedirectView("/login/reset-password", true);
    }

}