package com.project.askit.controller;

import com.project.askit.entity.AnswerVote;
import com.project.askit.entity.QuestionVote;
import com.project.askit.entity.User;
import com.project.askit.rest.api.AnswerVoteRestApi;
import com.project.askit.rest.api.QuestionVoteRestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class VoteController extends AbstractPageController {

    private final QuestionVoteRestApi questionVoteRestApi;
    private final AnswerVoteRestApi answerVoteRestApi;

    private final Logger logger;

    @Autowired
    public VoteController(QuestionVoteRestApi questionVoteRestApi,
                          AnswerVoteRestApi answerVoteRestApi) {
        super();

        this.questionVoteRestApi = questionVoteRestApi;
        this.answerVoteRestApi = answerVoteRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @ResponseBody
    @RequestMapping("/questions/vote")
    public Object voteQuestion(HttpServletRequest request) {

        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get data
            User user = (User) request.getSession().getAttribute("user");
            int vote = Integer.parseInt(request.getParameter("vote"));
            int questionId = Integer.parseInt(request.getParameter("questionId"));

            // Check for vote removal
            if (vote == 0) {
                QuestionVote oldVote = questionVoteRestApi.findByQuestionIdAndUserId(questionId, user.getId());
                if (oldVote == null) throw new Exception(REST_API_CALL_FAILED);

                QuestionVote deletedQuestionVote = questionVoteRestApi.deleteById(oldVote.getId());
                if (deletedQuestionVote == null) throw new Exception(REST_API_CALL_FAILED);

                return new ResponseEntity<>("", HttpStatus.OK);
            }

            QuestionVote existingVote = questionVoteRestApi.findByQuestionIdAndUserId(questionId, user.getId());
            if (existingVote != null) {
                // Change vote
                existingVote.setVote(vote);

                // Save changes
                QuestionVote updatedQuestionVote = questionVoteRestApi.update(existingVote);
                if (updatedQuestionVote == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.SERVICE_UNAVAILABLE);

                return new ResponseEntity<>("", HttpStatus.OK);
            }

            QuestionVote newQuestionVote = new QuestionVote(vote, questionId, user.getId());
            QuestionVote savedQuestionVote = questionVoteRestApi.save(newQuestionVote);
            if (savedQuestionVote == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.SERVICE_UNAVAILABLE);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);
            else if (error.equals(REST_API_CALL_FAILED)) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping("/answers/vote")
    public Object voteAnswer(HttpServletRequest request) {
        try{
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get data
            User user = (User) request.getSession().getAttribute("user");
            int vote = Integer.parseInt(request.getParameter("vote"));
            int answerId = Integer.parseInt(request.getParameter("answerId"));

            // Check for vote remove
            if (vote == 0) {
                // Get vote
                AnswerVote oldVote = answerVoteRestApi.findByAnswerIdAndUserId(answerId, user.getId());
                if (oldVote == null) throw new Exception(REST_API_CALL_FAILED);

                // Delete vote
                AnswerVote deletedAnswerVote = answerVoteRestApi.deleteById(oldVote.getId());
                if (deletedAnswerVote == null) throw new Exception(REST_API_CALL_FAILED);

                return new ResponseEntity<>("", HttpStatus.OK);
            }

            AnswerVote existingVote = answerVoteRestApi.findByAnswerIdAndUserId(answerId, user.getId());
            if (existingVote != null) {
                // Change vote
                existingVote.setVote(vote);

                // Save changes
                AnswerVote answerVote = answerVoteRestApi.update(existingVote);
                if (answerVote == null) throw new Exception(REST_API_CALL_FAILED);

                return new ResponseEntity<>("", HttpStatus.OK);
            }

            AnswerVote newAnswerVote = new AnswerVote(vote, answerId, user.getId());
            AnswerVote savedAnswerVote = answerVoteRestApi.save(newAnswerVote);
            if (savedAnswerVote == null) throw new Exception(REST_API_CALL_FAILED);

            return new ResponseEntity<>("", HttpStatus.OK);
        }catch (Exception e){
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);
            else if (error.equals(REST_API_CALL_FAILED)) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}