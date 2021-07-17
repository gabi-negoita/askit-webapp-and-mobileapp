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
public class ReviewAnswerPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;
    private final AnswerRestApi answerRestApi;
    private final UserRestApi userRestApi;
    private final EventLogRestApi eventLogRestApi;
    private final NotificationRestApi notificationRestApi;

    private final Logger logger;

    @Autowired
    public ReviewAnswerPageController(QuestionRestApi questionRestApi,
                                      AnswerRestApi answerRestApi,
                                      UserRestApi userRestApi,
                                      EventLogRestApi eventLogRestApi,
                                      NotificationRestApi notificationRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.answerRestApi = answerRestApi;
        this.userRestApi = userRestApi;
        this.eventLogRestApi = eventLogRestApi;
        this.notificationRestApi = notificationRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/reviews/answers/{answerId}")
    public Object showReviewAnswerPage(@PathVariable Integer answerId,
                                       Model model,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {

        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Get answer
            Answer answer = answerRestApi.findById(answerId);
            if (answer == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if answer has not been reviewed
            if (answer.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Get answer's user
            User answerUser = userRestApi.findById(answer.getUserId());
            if (answerUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Get answer's question
            Question question = questionRestApi.findById(answer.getQuestionId());
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question answers
            Wrapper<Answer> questionAnswersWrapper = answerRestApi.findAllByFields(null, Integer.MAX_VALUE, null, null, answer.getQuestionId(), 1, null);
            if (questionAnswersWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Add data to model
            model.addAttribute("answer", answer);
            model.addAttribute("answerUser", answerUser);
            model.addAttribute("question", question);
            model.addAttribute("questionAnswersWrapper", questionAnswersWrapper);

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

        return "review-answer";
    }

    @RequestMapping("/home/reviews/answers/{answerId}/approve")
    public Object approveAnswer(@PathVariable Integer answerId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Get object
            Answer answer = answerRestApi.findById(answerId);
            if (answer == null) throw new Exception(REST_API_CALL_FAILED);

            int oldApproved = answer.getApproved();

            // Check if it was reviewed meanwhile
            if (answer.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Set approved to 1 (approved)
            String comment = request.getParameter("comment");
            if (comment.isBlank()) comment = null;
            answer.setApproved(1);
            answer.setComment(comment);

            // Save object
            Answer updatedAnswer = answerRestApi.update(answer);
            if (updatedAnswer == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify user
            Notification notification = new Notification();
            notification.setTitle("Your answer has been approved");
            notification.setContent(null);
            notification.setUrl("/home/questions/" + updatedAnswer.getQuestionId());
            notification.setViewed(0);
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setUserId(updatedAnswer.getUserId());
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question
            Question question = questionRestApi.findById(updatedAnswer.getQuestionId());
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify question user
            notification = new Notification();
            notification.setTitle("Someone answered your question");
            notification.setContent(null);
            notification.setUrl("/home/questions/" + updatedAnswer.getQuestionId());
            notification.setViewed(0);
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setUserId(question.getUserId());
            savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed approved from \"" + oldApproved + "\" to \"" + updatedAnswer.getApproved() + "\" of answer with id " + answer.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Answer review has been successfully submitted"));

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

        return new RedirectView("/home/reviews/answers", true);
    }

    @RequestMapping("/home/reviews/answers/{answerId}/reject")
    public Object rejectAnswer(@PathVariable Integer answerId,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Get object
            Answer answer = answerRestApi.findById(answerId);
            if (answer == null) throw new Exception(REST_API_CALL_FAILED);

            int oldApproved = answer.getApproved();

            // Check if it was reviewed meanwhile
            if (answer.getApproved() != 0) throw new Exception(NOT_FOUND);

            // Set approved to -1 (rejected)
            String comment = request.getParameter("comment");
            if (comment.isBlank()) comment = null;
            answer.setApproved(-1);
            answer.setComment(comment);

            // Save object
            Answer updatedAnswer = answerRestApi.update(answer);
            if (updatedAnswer == null) throw new Exception(REST_API_CALL_FAILED);

            // Notify user
            Notification notification = new Notification();
            notification.setTitle("Your answer has been rejected");
            notification.setContent(null);
            notification.setUrl("/home/answers/edit/" + updatedAnswer.getId());
            notification.setViewed(0);
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            notification.setUserId(updatedAnswer.getUserId());
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed approved from \"" + oldApproved + "\" to \"" + updatedAnswer.getApproved() + "\" of answer with id " + answer.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Answer review has been successfully submitted"));

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

        return new RedirectView("/home/reviews/answers", true);
    }
}