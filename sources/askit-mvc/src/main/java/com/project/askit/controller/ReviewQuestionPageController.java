package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.MessageModel;
import com.project.askit.rest.api.*;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewQuestionPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;
    private final CategoryRestApi categoryRestApi;
    private final UserRestApi userRestApi;
    private final EventLogRestApi eventLogRestApi;
    private final NotificationRestApi notificationRestApi;

    private final Logger logger;

    @Autowired
    public ReviewQuestionPageController(QuestionRestApi questionRestApi,
                                        CategoryRestApi categoryRestApi,
                                        UserRestApi userRestApi,
                                        EventLogRestApi eventLogRestApi,
                                        NotificationRestApi notificationRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.categoryRestApi = categoryRestApi;
        this.userRestApi = userRestApi;
        this.eventLogRestApi = eventLogRestApi;
        this.notificationRestApi = notificationRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/reviews/questions/{questionId}")
    public Object showReviewQuestionPage(@PathVariable Integer questionId,
                                         Model model,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Get question
            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if question has not been reviewed
            if (question.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Get question's user
            User questionUser = userRestApi.findById(question.getUserId());
            if (questionUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question's category
            Category questionCategory = categoryRestApi.findById(question.getCategoryId());
            if (questionCategory == null) throw new Exception(REST_API_CALL_FAILED);

            // Add data to model
            model.addAttribute("question", question);
            model.addAttribute("questionUser", questionUser);
            model.addAttribute("questionCategory", questionCategory);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(NOT_FOUND)) return handleNotFound(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "review-question";
    }

    @RequestMapping("/home/reviews/questions/{questionId}/approve")
    public Object approveQuestion(@PathVariable Integer questionId,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            int oldApproved = question.getApproved();

            // Check if it was reviewed meanwhile
            if (question.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Set approved to 1 (approved)
            String comment = request.getParameter("comment");
            if (comment.isBlank()) comment = null;
            question.setApproved(1);
            question.setComment(comment);

            // Save object
            Question updatedQuestion = questionRestApi.update(question);
            if (updatedQuestion == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify user
            Notification notification = new Notification();
            notification.setTitle("Your question has been approved");
            notification.setContent(null);
            notification.setUrl("/home/questions/" + updatedQuestion.getId());
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setViewed(0);
            notification.setUserId(updatedQuestion.getUserId());
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed approved from \"" + oldApproved + "\" to \"" + updatedQuestion.getApproved() + "\" of question with id " + question.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Question review has been successfully submitted"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(NOT_FOUND)) return handleNotFound(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/reviews/questions", true);
    }

    @RequestMapping("/home/reviews/questions/{questionId}/reject")
    public Object rejectQuestion(@PathVariable Integer questionId,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            int oldApproved = question.getApproved();

            // Check if it was reviewed meanwhile
            if (question.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Set approved to 1 (approved)
            String comment = request.getParameter("comment");
            if (comment.isBlank()) comment = null;
            question.setApproved(-1);
            question.setComment(comment);

            // Save object
            Question updatedQuestion = questionRestApi.update(question);
            if (updatedQuestion == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify user
            Notification notification = new Notification();
            notification.setTitle("Your question has been rejected");
            notification.setContent(null);
            notification.setUrl("/home/questions/edit/" + updatedQuestion.getId());
            notification.setViewed(0);
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setUserId(updatedQuestion.getUserId());
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed approved from \"" + oldApproved + "\" to \"" + updatedQuestion.getApproved() + "\" of question with id " + question.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Question review has been successfully submitted"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(NOT_FOUND)) return handleNotFound(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/reviews/questions", true);
    }
}