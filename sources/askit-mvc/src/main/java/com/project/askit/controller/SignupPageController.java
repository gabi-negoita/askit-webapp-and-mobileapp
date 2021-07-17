package com.project.askit.controller;

import com.project.askit.entity.Notification;
import com.project.askit.entity.User;
import com.project.askit.exeption.AlreadyActiveAccountException;
import com.project.askit.model.EmailModel;
import com.project.askit.model.MessageModel;
import com.project.askit.model.ResetPasswordRequestModel;
import com.project.askit.model.SignupModel;
import com.project.askit.rest.api.EmailRestApi;
import com.project.askit.rest.api.NotificationRestApi;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final EmailRestApi emailRestApi;
    private final NotificationRestApi notificationRestApi;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SignupPageController(UserRestApi userRestApi,
                                EmailRestApi emailRestApi,
                                NotificationRestApi notificationRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.emailRestApi = emailRestApi;
        this.notificationRestApi = notificationRestApi;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @RequestMapping("/register")
    public Object showSignupPage(HttpServletRequest request,
                                 Model model) {
        // Check if user is logged in
        if (isLoggedIn(request)) return new RedirectView("/home", true);

        // Add data to models
        if (model.getAttribute("signupModel") == null) model.addAttribute("signupModel", new SignupModel());

        return "signup";
    }

    @RequestMapping("/register/process-form")
    public Object signUp(@Valid @ModelAttribute("signupModel") SignupModel signupModel,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        try {
            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Check if passwords match
            if (!signupModel.getPassword().equals(signupModel.getConfirmedPassword())) throw new Exception(PASSWORDS_NOT_MATCH);

            // Check if email exists
            User existingEmail = userRestApi.findByEmail(signupModel.getEmail());
            if (existingEmail != null) throw new Exception(ALREADY_EXISTS);

            // Create user
            User user = new User();
            user.setUsername(signupModel.getUsername());
            user.setPassword("{noop}" + signupModel.getPassword());
            user.setEmail(signupModel.getEmail());
            user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            user.setStatus(0);

            // Save user
            User savedUser = userRestApi.save(user);
            if (savedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Generate link
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String key = savedUser.getId() + EmailModel.DELIMITER + System.currentTimeMillis();
            String encryptedKey = Crypto.encrypt(key, Crypto.KEY);

            // Create email
            EmailModel emailModel = new EmailModel();
            emailModel.setDestination(savedUser.getEmail());
            emailModel.setType(EmailModel.CONFIRM_EMAIL);
            emailModel.setLink(baseUrl + "/" + emailModel.getType() + "?key=" + URLEncoder.encode(encryptedKey, StandardCharsets.UTF_8));

            EmailModel sentEmail = emailRestApi.sendEmail(emailModel);
            if (sentEmail == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify user
            Notification notification = new Notification();
            notification.setTitle("Welcome to Askit!");
            notification.setContent("We hope you will enjoy using our platform and don't forget to have fun while doing it!");
            notification.setUrl("");
            notification.setViewed(0);
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setUserId(savedUser.getId());

            // Save data
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Update url
            savedNotification.setUrl("/home/profile/notifications/view-notification/" + savedNotification.getId());

            // Save changes
            Notification updatedNotification = notificationRestApi.update(savedNotification);
            if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Almost there!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();

            details.add(new Pair<>(null, "A confirmation email has been sent to your email address"));
            details.add(new Pair<>("Note", "that the link inside the email will expire after 24 hours"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(ALREADY_EXISTS)) handleAlreadyExists(redirectAttributes, signupModel.getEmail());
            else if (error.equals(PASSWORDS_NOT_MATCH)) handlePasswordsNotMatch(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("signupModel", signupModel);
        }

        return new RedirectView("/register", true);
    }

    @RequestMapping("/register/resend-email")
    public Object showResetPasswordPage(Model model) {
        if (model.getAttribute("resetPasswordRequestModel") == null) model.addAttribute("resetPasswordRequestModel", new ResetPasswordRequestModel());
        return "resend-confirmation-email";
    }

    @RequestMapping("/register/resend-email/process-form")
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
                if (user.getStatus() == 1) throw new AlreadyActiveAccountException();

                // Generate link
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                String key = user.getId() + EmailModel.DELIMITER + System.currentTimeMillis();
                String encryptedKey = Crypto.encrypt(key, Crypto.KEY);

                // Create email
                EmailModel emailModel = new EmailModel();
                emailModel.setDestination(user.getEmail());
                emailModel.setType(EmailModel.CONFIRM_EMAIL);
                emailModel.setLink(baseUrl + "/" + emailModel.getType() + "?key=" + URLEncoder.encode(encryptedKey, StandardCharsets.UTF_8));

                EmailModel sentEmail = emailRestApi.sendEmail(emailModel);
                if (sentEmail == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Almost there!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "A confirmation email has been sent to your email address"));
            details.add(new Pair<>("Note", "that the link inside the email will expire after 24 hours"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (AlreadyActiveAccountException e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        }
        catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("resetPasswordRequestModel", resetPasswordRequestModel);
        }

        return new RedirectView("/register/resend-email", true);
    }
}
