package com.project.askit.controller;

import com.project.askit.entity.Answer;
import com.project.askit.entity.Question;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.UnreviewedPosts;
import com.project.askit.rest.api.AnswerRestApi;
import com.project.askit.rest.api.QuestionRestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ReviewsPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;

    private final AnswerRestApi answerRestApi;

    private final Logger logger;

    @Autowired
    public ReviewsPageController(QuestionRestApi questionRestApi,
                                 AnswerRestApi answerRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.answerRestApi = answerRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/reviews")
    public Object showReviewsPage(RedirectAttributes redirectAttributes,
                                  Model model,
                                  HttpServletRequest request) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Get data
            Wrapper<Question> questionWrapper = questionRestApi.findAllByFields(null, 1, null, null, null, null, null, null, 0);
            if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            Wrapper<Answer> answerWrapper = answerRestApi.findAllByFields(null, 1, null, null, null, 0, null);
            if (answerWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get total items
            int unreviewedQuestions = questionWrapper.getTotalItems();
            int unreviewedAnswers = answerWrapper.getTotalItems();

            // Add to model
            model.addAttribute("unreviewedQuestions", unreviewedQuestions);
            model.addAttribute("unreviewedAnswers", unreviewedAnswers);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "reviews";
    }

    @ResponseBody
    @RequestMapping("/home/reviews/unreviewed-count")
    public Object getUnreviewedQuestions(HttpServletRequest request) {

        // Check if user is logged in
        if (!isLoggedIn(request)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);

        // Check if user has access
        if (!hasAccess(request, "reviewer")) return new ResponseEntity<>(NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);

        // Get data
        String type = request.getParameter("type");
        String minutes = request.getParameter("minutes");

        if (type.isBlank() || minutes.isBlank()) return new ResponseEntity<>(INVALID_FIELDS, HttpStatus.BAD_REQUEST);

        // Get users
        UnreviewedPosts unreviewedPosts = null;
        switch (type) {
            case "questions": {
                unreviewedPosts = questionRestApi.getUnreviewedQuestionsCountByMinutes(Integer.parseInt(minutes));
                if (unreviewedPosts == null) return "";
                break;
            }
            case "answers": {
                unreviewedPosts = answerRestApi.getUnreviewedAnswersCountByMinutes(Integer.parseInt(minutes));
                if (unreviewedPosts == null) return "";
                break;
            }
        }

        return new ResponseEntity<>(unreviewedPosts.getCount(), HttpStatus.OK);
    }
}