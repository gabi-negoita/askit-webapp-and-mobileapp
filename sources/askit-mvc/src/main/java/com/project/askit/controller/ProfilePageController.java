package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.UserActivity;
import com.project.askit.model.UserStatistics;
import com.project.askit.rest.api.*;
import com.project.askit.util.Pagination;
import com.project.askit.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfilePageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final QuestionRestApi questionRestApi;
    private final AnswerRestApi answerRestApi;
    private final QuestionVoteRestApi questionVoteRestApi;
    private final AnswerVoteRestApi answerVoteRestApi;
    private final NotificationRestApi notificationRestApi;

    private final Logger logger;

    @Autowired
    public ProfilePageController(UserRestApi userRestApi,
                                 QuestionRestApi questionRestApi,
                                 AnswerRestApi answerRestApi,
                                 NotificationRestApi notificationRestApi,
                                 QuestionVoteRestApi questionVoteRestApi,
                                 AnswerVoteRestApi answerVoteRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.questionRestApi = questionRestApi;
        this.answerRestApi = answerRestApi;
        this.notificationRestApi = notificationRestApi;
        this.questionVoteRestApi = questionVoteRestApi;
        this.answerVoteRestApi = answerVoteRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/profile/{userId}")
    public Object redirectToProfilePage(@PathVariable Integer userId,
                                        RedirectAttributes redirectAttributes) {
        String formattedUsername;
        try {
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            formattedUsername = Utility.formatUrl(user.getUsername());
        } catch (Exception e) {
            String error = e.getMessage();
            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/profile/" + userId + "/" + formattedUsername, true);
    }

    @RequestMapping("/home/profile/{userId}/{username}")
    public Object showProfilePage(@PathVariable Integer userId,
                                  @PathVariable String username,
                                  @RequestParam(defaultValue = "0") Integer questionPage,
                                  @RequestParam(defaultValue = "0") Integer questionVotePage,
                                  @RequestParam(defaultValue = "0") Integer answerPage,
                                  @RequestParam(defaultValue = "0") Integer answerVotePage,
                                  HttpServletRequest request,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if account is active
            if (user.getStatus() != 1) throw new Exception(NOT_FOUND);

            // Check if the uri is correct
            String formattedUsername = Utility.formatUrl(user.getUsername());
            if (!username.equals(formattedUsername)) return new RedirectView("/home/profile/" + userId + "/" + formattedUsername, true);

            // Set pages
            questionPage = questionPage == 0 ? questionPage : questionPage - 1;
            answerPage = answerPage == 0 ? answerPage : answerPage - 1;

            User sessionUser = getSessionUser(request);
            if (sessionUser != null) {
                // Set pages
                questionVotePage = questionVotePage == 0 ? questionVotePage : questionVotePage - 1;
                answerVotePage = answerVotePage == 0 ? answerVotePage : answerVotePage - 1;
            }

            // Get data
            Wrapper<Question> questionWrapper = questionRestApi.findAllByFields(questionPage, 10, "createdDate", "desc", null, null, null, user.getId(), 1);
            if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            Wrapper<Answer> answerWrapper = answerRestApi.findAllByFields(answerPage, 8, "createdDate", "desc", null, 1, user.getId());
            if (answerWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            UserStatistics userStatistics = userRestApi.getStatistics(userId);
            if (userStatistics == null) throw new Exception(REST_API_CALL_FAILED);

            Wrapper<QuestionVote> questionVoteWrapper = null;
            Wrapper<AnswerVote> answerVoteWrapper = null;
            if (sessionUser != null) {
                // Get user voted questions
                questionVoteWrapper = questionVoteRestApi.findAllByUserId(questionVotePage, 10, "vote", "desc", user.getId());
                if (questionVoteWrapper == null) throw new Exception(REST_API_CALL_FAILED);

                // Get user voted answers
                answerVoteWrapper = answerVoteRestApi.findAllByUserId(answerVotePage, 10, "vote", "desc", user.getId());
                if (answerVoteWrapper == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Get pagination
            List<Integer> questionPagination = Pagination.getPagination(questionPage, 3, questionWrapper.getTotalPages());
            List<Integer> answerPagination = Pagination.getPagination(answerPage, 3, answerWrapper.getTotalPages());

            List<Integer> questionVotePagination = null;
            List<Integer> answerVotePagination = null;
            if (sessionUser != null) {
                // Get pagination
                questionVotePagination = Pagination.getPagination(questionVotePage, 3, questionVoteWrapper.getTotalPages());
                answerVotePagination = Pagination.getPagination(answerVotePage, 3, answerVoteWrapper.getTotalPages());
            }

            // Create questionMap based on answer
            Map<Answer, Question> answerPostedToQuestionMap = new HashMap<>();
            for (Answer a : answerWrapper.getContent()) {
                // Get question
                Question question = questionRestApi.findById(a.getQuestionId());
                if (question == null) throw new Exception(REST_API_CALL_FAILED);

                // Add data to map
                answerPostedToQuestionMap.put(
                        new Answer(null, a.getCreatedDate(), a.getQuestionId(), null, null),
                        new Question(question.getSubject(), null, null, null, null, null));
            }

            Map<Integer, Integer> answerQuestionMap = null;
            Map<Integer, String> questionMap = new HashMap<>();
            if (sessionUser != null) {
                // Add voted questions to map
                for (QuestionVote qv : questionVoteWrapper.getContent()) {
                    if (questionMap.containsKey(qv.getQuestionId())) continue;

                    Question question = questionRestApi.findById(qv.getQuestionId());
                    if (question == null) throw new Exception(REST_API_CALL_FAILED);

                    questionMap.put(qv.getQuestionId(), question.getSubject());
                }

                // Add voted answers to map
                answerQuestionMap = new HashMap<>();
                for (AnswerVote av : answerVoteWrapper.getContent()) {
                    Answer answer = answerRestApi.findById(av.getAnswerId());
                    if (answer == null) throw new Exception(REST_API_CALL_FAILED);

                    answerQuestionMap.put(answer.getId(), answer.getQuestionId());

                    if (questionMap.containsKey(answer.getQuestionId())) continue;

                    Question question = questionRestApi.findById(answer.getQuestionId());
                    if (question == null) throw new Exception(REST_API_CALL_FAILED);

                    questionMap.put(answer.getQuestionId(), question.getSubject());
                }
            }

            if (user.getDateOfBirth() != null) {
                // Get user's age
                Integer age = Math.abs(Period.between(LocalDate.now(), user.getDateOfBirth().toLocalDate()).getYears());
                model.addAttribute("age", age);
            }

            // Add data to model
            model.addAttribute("user", user);
            model.addAttribute("questionWrapper", questionWrapper);
            model.addAttribute("questionPagination", questionPagination);
            model.addAttribute("answerWrapper", answerWrapper);
            model.addAttribute("answerPostedToQuestionMap", answerPostedToQuestionMap);
            model.addAttribute("questionMap", questionMap);
            model.addAttribute("answerPagination", answerPagination);
            model.addAttribute("userStatistics", userStatistics);

            if (sessionUser != null) {
                model.addAttribute("questionVoteWrapper", questionVoteWrapper);
                model.addAttribute("questionVotePagination", questionVotePagination);
                model.addAttribute("answerVoteWrapper", answerVoteWrapper);
                model.addAttribute("answerVotePagination", answerVotePagination);
                model.addAttribute("answerQuestionMap", answerQuestionMap);
            }
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else if (error.equals(NOT_FOUND)) return handleNotFound(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "profile";
    }

    @ResponseBody
    @RequestMapping("/home/profile/{userId}/api/user-activity")
    public Object getUserActivity(@PathVariable Integer userId) {
        // Get activity
        List<UserActivity> response = userRestApi.getActivity(userId);
        if (response == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/home/profile/api/notifications")
    public Object getNotifications(HttpServletRequest request) {
        // Check if user is logged in
        if (!isLoggedIn(request)) return null;

        // Get user
        User user = getSessionUser(request);

        // Get notifications
        Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserId(null, 5, "createdDate", "desc", user.getId());
        if (notificationWrapper == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        // Post-process data
        List<Map<String, Object>> response = new ArrayList<>();
        for (Notification notification : notificationWrapper.getContent()) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", notification.getTitle());
            item.put("url", notification.getUrl());
            item.put("createdDate", notification.getCreatedDate());
            item.put("viewed", notification.getViewed());

            response.add(item);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/home/profile/api/notifications/mark-all-as-read")
    public Object markAllNotificationsAsRead(HttpServletRequest request) {
        // Check if user is logged in
        if (!isLoggedIn(request)) return null;

        // Get user
        User user = getSessionUser(request);

        // Get notifications
        Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserIdAndViewed(null, Integer.MAX_VALUE, null, null, user.getId(), 0);
        if (notificationWrapper == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        // Post-process data
        for (Notification notification : notificationWrapper.getContent()) {
            if (notification.getViewed() == null || notification.getViewed() != 0) continue;

            notification.setViewed(1);
            Notification updatedNotification = notificationRestApi.update(notification);
            if (updatedNotification == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}