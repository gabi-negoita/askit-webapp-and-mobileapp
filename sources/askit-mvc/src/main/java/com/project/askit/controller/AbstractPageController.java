package com.project.askit.controller;

import com.project.askit.entity.Role;
import com.project.askit.entity.User;
import com.project.askit.model.ErrorModel;
import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;
import com.project.askit.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageController {

    public final String NOT_LOGGED_IN = "Not logged in";
    public final String NOT_AUTHORIZED = "Not authorized";
    public final String REST_API_CALL_FAILED = "Rest API call failed";
    public final String INVALID_FIELDS = "Invalid fields";
    public final String ALREADY_EXISTS = "Already exists";
    public final String INVALID_USER_ID = "Invalid user ID";
    public final String PASSWORDS_NOT_MATCH = "Passwords do not match";
    public final String INVALID_CREDENTIALS = "Invalid credentials";
    public final String INVALID_PASSWORD = "Invalid password";
    public final String INVALID_ACCOUNT = "Invalid account";
    public final String INVALID_LINK = "Invalid link";
    public final String ALREADY_ACTIVE = "Already active";
    public final String NOT_FOUND = "Not found";

    @Autowired
    public AbstractPageController() {
    }

    protected User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    protected boolean isLoggedIn(HttpServletRequest request) {
        return getSessionUser(request) != null;
    }

    protected boolean hasAccess(HttpServletRequest request,
                                String accessRole) {
        if (!isLoggedIn(request)) return false;

        for (Role role : getSessionUser(request).getRoles())
            if (role.getName().equalsIgnoreCase(accessRole))
                return true;

        return false;
    }

    protected RedirectView handleNotLoggedIn(RedirectAttributes redirectAttributes) {
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(NOT_LOGGED_IN);
        messageModel.setType(MessageModel.TYPE_WARNING);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "You must be logged in to access this page"));

        messageModel.setDetails(details);

        redirectAttributes.addFlashAttribute("messageModel", messageModel);

        return new RedirectView("/login", true);
    }

    protected RedirectView handleRestApiCallFailed(RedirectAttributes redirectAttributes) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        List<String> details = new ArrayList<>();
        details.add("Something went wrong while processing your request");
        errorModel.setDetails(details);

        redirectAttributes.addFlashAttribute("errorModel", errorModel);

        return new RedirectView("/error", true);
    }

    protected RedirectView handleSomethingWentWrong(RedirectAttributes redirectAttributes) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        List<String> details = new ArrayList<>();
        details.add("Something went wrong. Please try again.");
        errorModel.setDetails(details);

        redirectAttributes.addFlashAttribute("errorModel", errorModel);

        return new RedirectView("/error", true);
    }

    protected RedirectView handleNotAuthorized(RedirectAttributes redirectAttributes) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setCode(HttpStatus.UNAUTHORIZED.value());
        List<String> details = new ArrayList<>();
        details.add("You do not have permission to access this page");
        errorModel.setDetails(details);

        redirectAttributes.addFlashAttribute("errorModel", errorModel);

        return new RedirectView("/error", true);
    }

    protected void handleInvalidFields(RedirectAttributes redirectAttributes,
                                       BindingResult result) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_FIELDS);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            details.add(new Pair<>(Utility.capitalizeAndSeparate(error.getField()), error.getDefaultMessage()));
        }

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleAlreadyExists(RedirectAttributes redirectAttributes,
                                       String objectName) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(ALREADY_EXISTS);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(objectName, "already exists"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleInvalidUserId(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_USER_ID);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>("User ID", "does not exist"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handlePasswordsNotMatch(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_FIELDS);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Password and confirm password do not match"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleInvalidCredentials(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_CREDENTIALS);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Your email or password are incorrect"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleInvalidPassword(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_PASSWORD);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "You password is not correct"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleInvalidAccount(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_ACCOUNT);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Your account is either inactive or blocked"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleInvalidLink(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(INVALID_LINK);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "The link is expired or incorrect"));
        details.add(new Pair<>(null, "Please try to repeat the process"));

        messageModel.setDetails(details);

        // Add data to redirect view
        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected void handleAlreadyActive(RedirectAttributes redirectAttributes) {
        // Create message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(ALREADY_ACTIVE);
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Your account has already been activated"));
        details.add(new Pair<>(null, "No further actions are required"));

        messageModel.setDetails(details);

        redirectAttributes.addFlashAttribute("messageModel", messageModel);
    }

    protected RedirectView handleNotFound(RedirectAttributes redirectAttributes) {
        int code = HttpStatus.NOT_FOUND.value();
        List<String> details = new ArrayList<>();
        details.add("The resource could not be found");

        redirectAttributes.addFlashAttribute("errorModel", new ErrorModel(code, details));

        return new RedirectView("/error", true);
    }

}