package com.project.askit.rest.api;

import com.project.askit.entity.Question;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.QuestionStatistics;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Component
public class QuestionRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public QuestionRestApi(Environment environment) {
        this.environment = environment;
    }

    public Question findById(Integer id) {

        Question result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/questions/" + id;
            ResponseEntity<Question> response = new RestTemplate().exchange(
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

    public Wrapper<Question> findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "5") Integer size,
                                             @RequestParam(defaultValue = "id") String sort,
                                             @RequestParam(defaultValue = "asc") String order,
                                             @RequestParam(required = false) String subject,
                                             @RequestParam(required = false) String htmlText,
                                             @RequestParam(required = false) Integer categoryId,
                                             @RequestParam(required = false) Integer userId,
                                             @RequestParam(required = false) Integer approved) {

        Wrapper<Question> result = null;

        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&subject=" + (subject == null ? "" : subject) +
                    "&htmlText=" + (htmlText == null ? "" : htmlText) +
                    "&categoryId=" + (categoryId == null ? "" : categoryId) +
                    "&userId=" + (userId == null ? "" : userId) +
                    "&approved=" + (approved == null ? "" : approved);
            String uri = environment.getProperty("rest.url.api") + "/questions" + query;
            ResponseEntity<Wrapper<Question>> response = new RestTemplate().exchange(
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

    public Wrapper<Question> findByQuery(Integer page,
                                         Integer size,
                                         String sortBy,
                                         String order,
                                         String categoryTitle,
                                         String createdDate,
                                         String search,
                                         Integer approved) {

        Wrapper<Question> result = null;

        try {
            String query = "?page=" + page +
                    "&size=" + size +
                    "&sortBy=" + sortBy +
                    "&order=" + order +
                    "&categoryTitle=" + categoryTitle +
                    "&createdDate=" + createdDate +
                    "&search=" + search +
                    "&approved=" + approved;
            String uri = environment.getProperty("rest.url.api") + "/questions/query" + query;
            ResponseEntity<Wrapper<Question>> response = new RestTemplate().exchange(
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

    public Question save(Question question) {
        Question result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/questions";
            HttpEntity<Question> requestEntity = new HttpEntity<>(question, null);

            ResponseEntity<Question> response = new RestTemplate().exchange(
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

    public Question update(Question question) {
        Question result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/questions";
            HttpEntity<Question> requestEntity = new HttpEntity<>(question, null);

            ResponseEntity<Question> response = new RestTemplate().exchange(
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

    public QuestionStatistics getStatistics(Integer id) {

        QuestionStatistics result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/questions/" + id + "/statistics";

            ResponseEntity<QuestionStatistics> response = new RestTemplate().exchange(
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

    public UnreviewedPosts getUnreviewedQuestionsCountByMinutes(int minutes) {

        UnreviewedPosts result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/questions/unreviewed?minutes=" + minutes;

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
