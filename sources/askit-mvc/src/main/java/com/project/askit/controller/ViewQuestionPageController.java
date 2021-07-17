package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.AnswerStatistics;
import com.project.askit.model.MessageModel;
import com.project.askit.model.PostAnswerModel;
import com.project.askit.model.QuestionStatistics;
import com.project.askit.rest.api.*;
import com.project.askit.util.Pagination;
import com.project.askit.util.Pair;
import com.project.askit.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ViewQuestionPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;
    private final CategoryRestApi categoryRestApi;
    private final UserRestApi userRestApi;
    private final AnswerRestApi answerRestApi;
    private final QuestionVoteRestApi questionVoteRestApi;
    private final AnswerVoteRestApi answerVoteRestApi;
    private final AnswerAttachmentRestApi answerAttachmentRestApi;
    private final EventLogRestApi eventLogRestApi;
    private final NotificationRestApi notificationRestApi;

    private final Logger logger;

    @Autowired
    public ViewQuestionPageController(QuestionRestApi questionRestApi,
                                      CategoryRestApi categoryRestApi,
                                      UserRestApi userRestApi,
                                      AnswerRestApi answerRestApi,
                                      QuestionVoteRestApi questionVoteRestApi,
                                      AnswerVoteRestApi answerVoteRestApi,
                                      AnswerAttachmentRestApi answerAttachmentRestApi,
                                      EventLogRestApi eventLogRestApi,
                                      NotificationRestApi notificationRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.categoryRestApi = categoryRestApi;
        this.userRestApi = userRestApi;
        this.answerRestApi = answerRestApi;
        this.questionVoteRestApi = questionVoteRestApi;
        this.answerVoteRestApi = answerVoteRestApi;
        this.answerAttachmentRestApi = answerAttachmentRestApi;
        this.eventLogRestApi = eventLogRestApi;
        this.notificationRestApi = notificationRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/questions/{questionId}")
    public Object redirectToViewQuestionPage(@PathVariable Integer questionId,
                                             RedirectAttributes redirectAttributes) {
        String formattedSubject;
        try {
            // Get data
            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            formattedSubject = Utility.formatUrl(question.getSubject());
        } catch (Exception e) {
            String error = e.getMessage();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/questions/" + questionId + "/" + formattedSubject, true);
    }

    @RequestMapping("/home/questions/{questionId}/{subject}")
    public Object showViewQuestionPage(HttpServletRequest request,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(defaultValue = "mostvotes") String sort,
                                       @PathVariable Integer questionId,
                                       @PathVariable String subject,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Get question
            Question question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if url is well formatted
            String urlSubject = Utility.formatUrl(question.getSubject());
            if (!subject.equals(urlSubject)) return new RedirectView("/home/questions/" + questionId + "/" + urlSubject + "?page=" + page + "&size=" + size + "&sort=" + sort, true);

            // Check question status
            if (question.getApproved() != 1) throw new Exception(NOT_FOUND);

            // Get question category
            Category category = categoryRestApi.findById(question.getCategoryId());
            if (category == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question's user
            User questionUser = userRestApi.findById(question.getUserId());
            if (questionUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question statistics
            QuestionStatistics questionStatistics = questionRestApi.getStatistics(questionId);
            if (questionStatistics == null) throw new Exception(REST_API_CALL_FAILED);

            // Set question statistics
            question.setQuestionStatistics(questionStatistics);

            // Set page
            page = page == 0 ? page : page - 1;

            // Set sort by and order
            String sortBy = null;
            String order = null;

            System.out.println(sort);

            // Set sort
            switch (sort) {
                case "oldest": {
                    sortBy = "createdDate";
                    break;
                }
                case "newest": {
                    sortBy = "createdDate";
                    order = "desc";
                    break;
                }
                case "leastvotes": {
                    sortBy = "votes";
                    break;
                }
                case "mostvotes": {
                    sortBy = "votes";
                    order = "desc";
                    break;
                }
            }

            // Get answers
            Wrapper<Answer> answerWrapper = answerRestApi.findAllByFields(page, size, sortBy, order, question.getId(), 1, null);
            if (answerWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get each answer username and statistics
            Map<Integer, String> userAnswersMap = new HashMap<>();
            for (Answer answer : answerWrapper.getContent()) {
                // Get user
                User user = userRestApi.findById(answer.getUserId());
                if (user == null) throw new Exception(REST_API_CALL_FAILED);

                // Get answer statistics
                AnswerStatistics answerStatistics = answerRestApi.getStatistics(answer.getId());
                if (answerStatistics == null) throw new Exception(REST_API_CALL_FAILED);

                answer.setAnswerStatistics(answerStatistics);
                userAnswersMap.put(answer.getUserId(), user.getUsername());
            }

            // Get answer pagination
            List<Integer> pagination = Pagination.getPagination(page, 3, answerWrapper.getTotalPages());

            // Check if user is logged in
            if (isLoggedIn(request)) {
                User sessionUser = getSessionUser(request);

                // Get user's vote on question
                QuestionVote questionVote = questionVoteRestApi.findByQuestionIdAndUserId(question.getId(), sessionUser.getId());
                if (questionVote != null) model.addAttribute("questionVote", questionVote);

                // Get user's vote on answers
                Map<Integer, Integer> answerVotesMap = new HashMap<>();
                for (Answer answer : answerWrapper.getContent()) {
                    // Get vote
                    AnswerVote answerVote = answerVoteRestApi.findByAnswerIdAndUserId(answer.getId(), sessionUser.getId());
                    if (answerVote != null) answerVotesMap.put(answer.getId(), answerVote.getVote());
                }

                // Check for notifications
                Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserIdAndViewedAndUrl(null, null, "viewed", "desc", sessionUser.getId(), 0, "/home/questions/" + question.getId());
                for (Notification notification : notificationWrapper.getContent()) {
                    notification.setViewed(1);
                    Notification updatedNotification = notificationRestApi.save(notification);
                    if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);
                }

                model.addAttribute("answerVotesMap", answerVotesMap);
            }

            // Add data to model
            if (model.getAttribute("postAnswerModel") == null) {
                model.addAttribute("postAnswerModel", new PostAnswerModel());
            }

            model.addAttribute("question", question);
            model.addAttribute("questionCategory", category);
            model.addAttribute("questionUsername", questionUser.getUsername());
            model.addAttribute("answerWrapper", answerWrapper);
            model.addAttribute("pagination", pagination);
            model.addAttribute("userAnswersMap", userAnswersMap);
            model.addAttribute("page", page);
            model.addAttribute("sort", sort);
            model.addAttribute("size", size);

            // Set session image locations
            List<String> locations = (List<String>) request.getSession().getAttribute("locations");
            if (locations == null) locations = new ArrayList<>();
            request.getSession().setAttribute("locations", locations);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_FOUND)) return handleNotFound(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "view-question";
    }

    @RequestMapping("/home/questions/answer/{questionId}")
    public Object postAnswer(HttpServletRequest request,
                             @Valid @ModelAttribute("postAnswerModel") PostAnswerModel postAnswerModel,
                             BindingResult result,
                             @PathVariable Integer questionId,
                             RedirectAttributes redirectAttributes) {
        Question question = null;
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get question for redirect
            question = questionRestApi.findById(questionId);
            if (question == null) throw new Exception(REST_API_CALL_FAILED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get data
            User sessionUser = getSessionUser(request);

            // Set approved by role
            int approved = 0;
            for (Role role : sessionUser.getRoles()){
                if (role.getName().equalsIgnoreCase("teacher")){
                    approved = 1;
                    break;
                }
            }

            // Create answer
            Answer answer = new Answer();
            answer.setHtmlText(postAnswerModel.getBody());
            answer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            answer.setQuestionId(questionId);
            answer.setUserId(sessionUser.getId());
            answer.setApproved(approved);

            // Save answer
            Answer savedAnswer = answerRestApi.save(answer);
            if (savedAnswer == null) throw new Exception(REST_API_CALL_FAILED);

            // Store image locations
            List<String> locations = (List<String>) request.getSession().getAttribute("locations");
            if (locations != null) {
                for (String location : locations) {
                    AnswerAttachment answerAttachment = new AnswerAttachment(
                            location,
                            savedAnswer.getId());
                    AnswerAttachment savedAnswerAttachment = answerAttachmentRestApi.save(answerAttachment);
                    if (savedAnswerAttachment == null) throw new Exception(REST_API_CALL_FAILED);
                }

                request.getSession().removeAttribute("locations");
            }

            // Notify user
            if (approved == 1) {
                Notification notification = new Notification();
                notification.setTitle("Someone answered your question");
                notification.setContent(null);
                notification.setUrl("/home/questions/" + savedAnswer.getQuestionId());
                notification.setViewed(0);
                notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                notification.setUserId(question.getUserId());
                Notification savedNotification = notificationRestApi.save(notification);
                if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Posted answer with id " + savedAnswer.getId());
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
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("postAnswerModel", postAnswerModel);
        }

        return new RedirectView("/home/questions/" + questionId + "/" + Utility.formatUrl(question.getSubject()), true);
    }
}