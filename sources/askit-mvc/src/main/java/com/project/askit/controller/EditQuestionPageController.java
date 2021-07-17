package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.MessageModel;
import com.project.askit.model.PostQuestionModel;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.NotificationRestApi;
import com.project.askit.rest.api.QuestionRestApi;
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
public class EditQuestionPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;
    private final CategoryRestApi categoryRestApi;
    private final NotificationRestApi notificationRestApi;
    private final EventLogRestApi eventLogRestApi;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EditQuestionPageController(QuestionRestApi questionRestApi,
                                      CategoryRestApi categoryRestApi,
                                      NotificationRestApi notificationRestApi,
                                      EventLogRestApi eventLogRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.categoryRestApi = categoryRestApi;
        this.notificationRestApi = notificationRestApi;
        this.eventLogRestApi = eventLogRestApi;
    }

    @RequestMapping("/home/questions/edit/{questionId}")
    public Object showEditQuestionPage(HttpServletRequest request,
                                       Model model,
                                       @PathVariable Integer questionId,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get data
            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if question belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(question.getUserId())) throw new Exception(NOT_AUTHORIZED);

            // Get data
            Wrapper<Category> categoryWrapper = categoryRestApi.findAll(null, Integer.MAX_VALUE, "title", null, null);
            if (categoryWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Check for notifications
            Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserIdAndViewedAndUrl(null, null, "viewed", "desc", sessionUser.getId(), 0, "/home/questions/edit/" + question.getId());
            for (Notification notification : notificationWrapper.getContent()) {
                notification.setViewed(1);
                Notification updatedNotification = notificationRestApi.save(notification);
                if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Add data to model
            if (model.getAttribute("postQuestionModel") == null) {
                Category category = categoryRestApi.findById(question.getCategoryId());
                if (category == null) throw new Exception(REST_API_CALL_FAILED);

                // Create model object
                PostQuestionModel postQuestionModel = new PostQuestionModel();
                postQuestionModel.setCategory(category.getTitle());
                postQuestionModel.setBody(question.getHtmlText());
                postQuestionModel.setSubject(question.getSubject());

                // Add data to model
                model.addAttribute("postQuestionModel", postQuestionModel);
            }

            // Add data to model
            model.addAttribute("categories", categoryWrapper.getContent());
            model.addAttribute("question", question);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "edit-question";
    }

    @RequestMapping("/home/questions/save-changes/{questionId}")
    public Object saveQuestionChanges(HttpServletRequest request,
                                      @Valid @ModelAttribute("postQuestionModel") PostQuestionModel postQuestionModel,
                                      BindingResult result,
                                      @PathVariable Integer questionId,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get data
            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if question belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(question.getUserId())) throw new Exception(NOT_AUTHORIZED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Set approved by role
            int approved = 0;
            for (Role role : sessionUser.getRoles()){
                if (role.getName().equalsIgnoreCase("teacher")){
                    approved = 1;
                    break;
                }
            }

            Category category = categoryRestApi.findByTitle(postQuestionModel.getCategory());
            if (category == null) throw new Exception(REST_API_CALL_FAILED);

            // Create question to update
            Question newQuestion = new Question();
            newQuestion.setId(questionId);
            newQuestion.setSubject(postQuestionModel.getSubject());
            newQuestion.setHtmlText(postQuestionModel.getBody());
            newQuestion.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            newQuestion.setCategoryId(category.getId());
            newQuestion.setUserId(sessionUser.getId());
            newQuestion.setApproved(approved);

            // Update question
            Question updatedQuestion = questionRestApi.update(newQuestion);
            if (updatedQuestion == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Modified question with id " + updatedQuestion.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);
            List<Pair<String, String>> details = new ArrayList<>();

            details.add(new Pair<>(null, "Your questions has been successfully submitted"));
            if (approved == 0) details.add(new Pair<>("Note", "Your question will become public as soon as a reviewer approves it"));

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
            redirectAttributes.addFlashAttribute("postQuestionModel", postQuestionModel);

            // Log error
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return new RedirectView("/home/questions/edit/" + questionId, true);
    }
}
