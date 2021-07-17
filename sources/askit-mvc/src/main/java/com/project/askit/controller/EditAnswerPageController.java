package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.MessageModel;
import com.project.askit.model.PostAnswerModel;
import com.project.askit.rest.api.AnswerRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.NotificationRestApi;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EditAnswerPageController extends AbstractPageController {

    private final AnswerRestApi answerRestApi;
    private final NotificationRestApi notificationRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public EditAnswerPageController(AnswerRestApi answerRestApi,
                                    NotificationRestApi notificationRestApi,
                                    EventLogRestApi eventLogRestApi) {
        super();

        this.answerRestApi = answerRestApi;
        this.notificationRestApi = notificationRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/answers/edit/{answerId}")
    public Object showEditAnswerPage(HttpServletRequest request,
                                     Model model,
                                     @PathVariable Integer answerId,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            User sessionUser = getSessionUser(request);
            if (sessionUser == null) throw new Exception(NOT_LOGGED_IN);

            // Get data
            Answer answer = answerRestApi.findById(answerId);
            if (answer == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if answer belongs to user
            if (!answer.getUserId().equals(getSessionUser(request).getId())) throw new Exception(NOT_AUTHORIZED);

            // Check for notifications
            Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserIdAndViewedAndUrl(null, null, "viewed", "desc", sessionUser.getId(), 0, "/home/answers/edit/" + answer.getId());
            for (Notification notification : notificationWrapper.getContent()) {
                notification.setViewed(1);
                Notification updatedNotification = notificationRestApi.save(notification);
                if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Check if model contains data
            if (model.getAttribute("postAnswerModel") == null) {
                // Create model object
                PostAnswerModel postAnswerModel = new PostAnswerModel();
                postAnswerModel.setBody(answer.getHtmlText());

                // Add data to model
                model.addAttribute("postAnswerModel", postAnswerModel);
            }

            // Add data to model
            model.addAttribute("answer", answer);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "edit-answer";
    }

    @RequestMapping("/home/answers/save-changes/{answerId}")
    public Object saveAnswerChanges(HttpServletRequest request,
                                    @Valid @ModelAttribute("postAnswerModel") PostAnswerModel postAnswerModel,
                                    BindingResult result,
                                    @PathVariable Integer answerId,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get data
            Answer answer = answerRestApi.findById(answerId);
            if (answer == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if answer belongs to user
            if (!answer.getUserId().equals(getSessionUser(request).getId())) throw new Exception(NOT_AUTHORIZED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Set approved by role
            int approved = 0;
            for (Role role : getSessionUser(request).getRoles()){
                if (role.getName().equalsIgnoreCase("teacher")){
                    approved = 1;
                    break;
                }
            }

            // Update data
            answer.setHtmlText(postAnswerModel.getBody());
            answer.setApproved(approved);
            answer.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            // Save changes
            Answer updatedAnswer = answerRestApi.update(answer);
            if (updatedAnswer == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Modified answer with id " + updatedAnswer.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();

            details.add(new Pair<>(null, "Your answer has been successfully submitted"));
            if (approved == 0) details.add(new Pair<>("Note", "Your answer will become public as soon as a reviewer approves it"));

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
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add model to redirect view
            redirectAttributes.addFlashAttribute("postAnswerModel", postAnswerModel);
        }

        return new RedirectView("/home/answers/edit/" + answerId, true);
    }

}
