package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.model.*;
import com.project.askit.rest.api.EmailRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Crypto;
import com.project.askit.util.Pair;
import com.project.askit.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EditProfilePageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final EmailRestApi emailRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public EditProfilePageController(UserRestApi userRestApi,
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
        // Format date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor customDateEditor = new CustomDateEditor(simpleDateFormat, true, 10);
        dataBinder.registerCustomEditor(Date.class, customDateEditor);
    }

    @RequestMapping("/home/profile/edit/{userId}")
    public Object redirectToEditProfilePage(@PathVariable Integer userId,
                                            RedirectAttributes redirectAttributes) {
        String formattedUsername;
        try {
            // Get data
            User user = userRestApi.findById(userId);
            if (user == null) return handleRestApiCallFailed(redirectAttributes);

            // Format username
            formattedUsername = Utility.formatUrl(user.getUsername());

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/profile/edit/" + userId + "/" + formattedUsername, true);
    }

    @RequestMapping("/home/profile/edit/{userId}/{username}")
    public Object showEditProfilePage(@PathVariable Integer userId,
                                      @PathVariable String username,
                                      Model model,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);
            String formattedUsername = Utility.formatUrl(user.getUsername());

            // Check if url username is correct
            if (!formattedUsername.equals(username)) return new RedirectView("/home/profile/edit/" + userId + "/" + formattedUsername, true);

            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if page belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(userId)) throw new Exception(NOT_AUTHORIZED);

            // Check if data exists in model
            if (model.getAttribute("changeAccountInfoModel") == null) {
                // Create data
                ChangeAccountInfoModel changeAccountInfoModel = new ChangeAccountInfoModel();
                changeAccountInfoModel.setUsername(user.getUsername());
                changeAccountInfoModel.setDateOfBirth(user.getDateOfBirth());
                changeAccountInfoModel.setDescription(user.getDescription());

                // Add data to model
                model.addAttribute("changeAccountInfoModel", changeAccountInfoModel);
            }

            // Check if data exists in model
            if (model.getAttribute("changeAccountEmailModel") == null) {
                // Create data
                ChangeAccountEmailModel changeAccountEmailModel = new ChangeAccountEmailModel();
                changeAccountEmailModel.setCurrentEmail(user.getEmail());

                // Add data to model
                model.addAttribute("changeAccountEmailModel", changeAccountEmailModel);
            }

            // Check if data exists in model
            if (model.getAttribute("changeAccountPasswordModel") == null) {
                // Create data
                ChangeAccountPasswordModel changeAccountPasswordModel = new ChangeAccountPasswordModel();
                changeAccountPasswordModel.setUsername(username);

                // Add data to model
                model.addAttribute("changeAccountPasswordModel", changeAccountPasswordModel);
            }

            // Add data to model
            model.addAttribute("user", user);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "edit-profile";
    }

    @RequestMapping("/home/profile/edit/{userId}/change-account-info")
    public Object changeAccountInfo(HttpServletRequest request,
                                    @Valid @ModelAttribute("changeAccountInfoModel") ChangeAccountInfoModel changeAccountInfoModel,
                                    BindingResult result,
                                    @PathVariable Integer userId,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if page belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(userId)) throw new Exception(NOT_AUTHORIZED);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Extra processing
            if (changeAccountInfoModel.getDescription().isEmpty()) changeAccountInfoModel.setDescription(null);

            // Update data
            user.setUsername(changeAccountInfoModel.getUsername());
            user.setDateOfBirth(changeAccountInfoModel.getDateOfBirth());
            user.setDescription(changeAccountInfoModel.getDescription());

            // Save changes
            User savedUser = userRestApi.update(user);
            if (savedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Change session user username
            getSessionUser(request).setUsername(savedUser.getUsername());

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Modified user profile info with id " + savedUser.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Your profile information has been updated!"));

            messageModel.setDetails(details);

            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add model to redirect view
            redirectAttributes.addFlashAttribute("changeAccountInfoModel", changeAccountInfoModel);
        }

        return new RedirectView("/home/profile/edit/" + userId + "/" + Utility.formatUrl(getSessionUser(request).getUsername()), true);
    }

    @RequestMapping("/home/profile/edit/{userId}/change-account-email")
    public Object changeAccountEmail(HttpServletRequest request,
                                     @Valid @ModelAttribute("changeAccountEmailModel") ChangeAccountEmailModel changeAccountEmailModel,
                                     BindingResult result,
                                     @PathVariable Integer userId,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if page belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(userId)) throw new Exception(NOT_AUTHORIZED);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Extra validation: check if email already exists
            User existingUser = userRestApi.findByEmail(changeAccountEmailModel.getNewEmail());
            if (existingUser != null) throw new Exception(ALREADY_EXISTS);

            // Generate link
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String key = user.getId() + EmailModel.DELIMITER + changeAccountEmailModel.getNewEmail() + EmailModel.DELIMITER + System.currentTimeMillis();
            String encryptedKey = Crypto.encrypt(key, Crypto.KEY);

            // Create email
            EmailModel emailModel = new EmailModel();
            emailModel.setDestination(changeAccountEmailModel.getNewEmail());
            emailModel.setType(EmailModel.CHANGE_EMAIL);
            emailModel.setLink(baseUrl + "/" + emailModel.getType() + "?key=" + URLEncoder.encode(encryptedKey, StandardCharsets.UTF_8));

            // Send email
            EmailModel sentEmail = emailRestApi.sendEmail(emailModel);
            if (sentEmail == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            List<Pair<String, String>> details = new ArrayList<>();
            messageModel.setMessage("Almost there!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            details.add(new Pair<>(null, "A confirmation email has been sent to your new address"));
            details.add(new Pair<>("Note", "that the link inside the email will expire after 24 hours"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(ALREADY_EXISTS)) handleAlreadyExists(redirectAttributes, changeAccountEmailModel.getNewEmail());
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add model to redirect view
            redirectAttributes.addFlashAttribute("changeAccountEmailModel", changeAccountEmailModel);
        }

        return new RedirectView("/home/profile/edit/" + userId + "/" + Utility.formatUrl(getSessionUser(request).getUsername()), true);
    }

    @RequestMapping("/home/profile/edit/{userId}/change-account-password")
    public Object changeAccountPassword(HttpServletRequest request,
                                        @Valid @ModelAttribute("changeAccountPasswordModel") ChangeAccountPasswordModel changeAccountPasswordModel,
                                        BindingResult result,
                                        @PathVariable Integer userId,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if page belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(userId)) throw new Exception(NOT_AUTHORIZED);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Extra validation: check if passwords match
            if (!changeAccountPasswordModel.getNewPassword().equals(changeAccountPasswordModel.getConfirmNewPassword())) throw new Exception(PASSWORDS_NOT_MATCH);

            // Extra validation: check if credentials are correct
            if (!user.getPassword().equals("{noop}" + changeAccountPasswordModel.getCurrentPassword())) throw new Exception(INVALID_PASSWORD);

            // Update data
            user.setPassword("{noop}" + changeAccountPasswordModel.getNewPassword());

            // Save changes
            User updatedUser = userRestApi.update(user);
            if (updatedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed user password with id " + updatedUser.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Your password has been successfully changed"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(PASSWORDS_NOT_MATCH)) handlePasswordsNotMatch(redirectAttributes);
            else if (error.equals(INVALID_PASSWORD)) handleInvalidPassword(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("changeAccountPasswordModel", changeAccountPasswordModel);
        }

        return new RedirectView("/home/profile/edit/" + userId + "/" + Utility.formatUrl(getSessionUser(request).getUsername()), true);
    }
}