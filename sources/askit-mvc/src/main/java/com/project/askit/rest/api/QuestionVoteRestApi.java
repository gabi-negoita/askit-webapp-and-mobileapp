package com.project.askit.rest.api;

import com.project.askit.entity.QuestionVote;
import com.project.askit.entity.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuestionVoteRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public QuestionVoteRestApi(Environment environment) {
        this.environment = environment;
    }

    public QuestionVote findByQuestionIdAndUserId(int questionId,
                                                  int userId) {
        QuestionVote result = null;
        try {
            String query = "?questionId=" + questionId + "&userId=" + userId;
            String uri = environment.getProperty("rest.url.api") + "/question-votes" + query;
            ResponseEntity<QuestionVote> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public Wrapper<QuestionVote> findAllByUserId(Integer page,
                                                 Integer size,
                                                 String sort,
                                                 String order,
                                                 int userId) {
        Wrapper<QuestionVote> result = null;
        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order);
            String uri = environment.getProperty("rest.url.api") + "/question-votes/user/" + userId + query;
            ResponseEntity<Wrapper<QuestionVote>> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public QuestionVote save(QuestionVote object) {

        QuestionVote result = null;
        try {
            String uri = environment.getProperty("rest.url.api") + "/question-votes";
            HttpEntity<QuestionVote> requestEntity = new HttpEntity<>(object, null);

            ResponseEntity<QuestionVote> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public QuestionVote update(QuestionVote object) {

        QuestionVote result = null;
        try {
            String uri = environment.getProperty("rest.url.api") + "/question-votes";
            HttpEntity<QuestionVote> requestEntity = new HttpEntity<>(object, null);

            ResponseEntity<QuestionVote> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public QuestionVote deleteById(Integer id) {
        QuestionVote result = null;
        try {
            String uri = environment.getProperty("rest.url.api") + "/question-votes/" + id;
            ResponseEntity<QuestionVote> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.DELETE,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

}
