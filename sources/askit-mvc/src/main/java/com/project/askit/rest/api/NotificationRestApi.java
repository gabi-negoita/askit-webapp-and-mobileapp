package com.project.askit.rest.api;

import com.project.askit.entity.Notification;
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
public class NotificationRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationRestApi(Environment environment) {
        this.environment = environment;
    }

    public Wrapper<Notification> findAllByUserIdAndViewedAndUrl(Integer page,
                                                                Integer size,
                                                                String sort,
                                                                String order,
                                                                Integer userId,
                                                                Integer viewed,
                                                                String url) {
        Wrapper<Notification> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&userId=" + (userId == null ? "" : userId) +
                    "&viewed=" + (viewed == null ? "" : viewed) +
                    "&url=" + (url == null ? "" : url);
            ResponseEntity<Wrapper<Notification>> response = new RestTemplate().exchange(
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

    public Wrapper<Notification> findAllByUserIdAndViewed(Integer page,
                                                          Integer size,
                                                          String sort,
                                                          String order,
                                                          Integer userId,
                                                          Integer viewed) {
        Wrapper<Notification> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications/user-viewed";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&userId=" + (userId == null ? "" : userId) +
                    "&viewed=" + (viewed == null ? "" : viewed);
            ResponseEntity<Wrapper<Notification>> response = new RestTemplate().exchange(
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

    public Wrapper<Notification> findAllByUserId(Integer page,
                                                 Integer size,
                                                 String sort,
                                                 String order,
                                                 Integer userId) {
        Wrapper<Notification> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications/user";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&userId=" + (userId == null ? "" : userId);
            ResponseEntity<Wrapper<Notification>> response = new RestTemplate().exchange(
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

    public Notification findById(Integer id) {

        Notification result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications/" + id;
            ResponseEntity<Notification> response = new RestTemplate().exchange(
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

    public Notification save(Notification notification) {
        Notification result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications";
            HttpEntity<Notification> requestEntity = new HttpEntity<>(notification, null);

            ResponseEntity<Notification> response = new RestTemplate().exchange(
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

    public Notification update(Notification notification) {
        Notification result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/notifications";
            HttpEntity<Notification> requestEntity = new HttpEntity<>(notification, null);

            ResponseEntity<Notification> response = new RestTemplate().exchange(
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

}
