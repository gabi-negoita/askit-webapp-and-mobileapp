package com.project.askit.rest.api;

import com.project.askit.entity.AnswerVote;
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
public class AnswerVoteRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnswerVoteRestApi(Environment environment) {
        this.environment = environment;
    }

    public AnswerVote findByAnswerIdAndUserId(int answerId,
                                              int userId) {
        AnswerVote result = null;
        try {
            String uri = environment.getProperty("rest.url.api") + "/answer-votes";
            String query = "?answerId=" + answerId + "&userId=" + userId;
            ResponseEntity<AnswerVote> response = new RestTemplate().exchange(
                    uri + query,
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

    public Wrapper<AnswerVote> findAllByUserId(Integer page,
                                               Integer size,
                                               String sort,
                                               String order,
                                               int userId) {
        Wrapper<AnswerVote> result = null;
        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order);
            String uri = environment.getProperty("rest.url.api") + "/answer-votes/user/" + userId + query;
            ResponseEntity<Wrapper<AnswerVote>> response = new RestTemplate().exchange(
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

    public AnswerVote save(AnswerVote object) {
        AnswerVote result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answer-votes";
            HttpEntity<AnswerVote> requestEntity = new HttpEntity<>(object, null);

            ResponseEntity<AnswerVote> response = new RestTemplate().exchange(
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

    public AnswerVote update(AnswerVote object) {
        AnswerVote result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answer-votes";
            HttpEntity<AnswerVote> requestEntity = new HttpEntity<>(object, null);

            ResponseEntity<AnswerVote> response = new RestTemplate().exchange(
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

    public AnswerVote deleteById(Integer id) {
        AnswerVote result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answer-votes/" + id;
            ResponseEntity<AnswerVote> response = new RestTemplate().exchange(
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
