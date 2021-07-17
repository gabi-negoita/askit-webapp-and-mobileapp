package com.project.askit.rest.api;

import com.project.askit.entity.Answer;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.AnswerStatistics;
import com.project.askit.model.UnreviewedPosts;
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
public class AnswerRestApi {

    private final Environment environment;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnswerRestApi(Environment environment) {
        this.environment = environment;
    }

    public Wrapper<Answer> findAllByFields(Integer page,
                                           Integer size,
                                           String sort,
                                           String order,
                                           Integer questionId,
                                           Integer approved,
                                           Integer userId) {
        Wrapper<Answer> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&questionId=" + (questionId == null ? "" : questionId) +
                    "&approved=" + (approved == null ? "" : approved) +
                    "&userId=" + (userId == null ? "" : userId);

            ResponseEntity<Wrapper<Answer>> response = new RestTemplate().exchange(
                    uri + query,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                logger.error(errorMessage);
            }

            result = response.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public Answer findById(Integer id) {

        Answer result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers/" + id;
            ResponseEntity<Answer> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    Answer.class
            );

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

    public AnswerStatistics getStatistics(Integer id) {
        AnswerStatistics result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers/" + id + "/statistics";
            ResponseEntity<AnswerStatistics> response = new RestTemplate().exchange(
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

    public Answer save(Answer Object) {
        Answer result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers";
            HttpEntity<Answer> requestEntity = new HttpEntity<>(Object, null);
            ResponseEntity<Answer> response = new RestTemplate().exchange(
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

    public Answer update(Answer answer) {
        Answer result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers";
            HttpEntity<Answer> requestEntity = new HttpEntity<>(answer, null);

            ResponseEntity<Answer> response = new RestTemplate().exchange(
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

    public UnreviewedPosts getUnreviewedAnswersCountByMinutes(int minutes) {

        UnreviewedPosts result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answers/unreviewed?minutes=" + minutes;

            ResponseEntity<UnreviewedPosts> response = new RestTemplate().exchange(
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
}
