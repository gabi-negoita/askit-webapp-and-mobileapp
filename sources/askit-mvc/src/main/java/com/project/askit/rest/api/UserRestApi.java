package com.project.askit.rest.api;

import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.UserActivity;
import com.project.askit.model.UserIdAndPostCount;
import com.project.askit.model.UserStatistics;
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

import java.util.List;

@Component
public class UserRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserRestApi(Environment environment) {
        this.environment = environment;
    }

    public User findById(Integer id) {

        User result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users/" + id;
            ResponseEntity<User> response = new RestTemplate().exchange(
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

    public Wrapper<User> findAllByFields(Integer page,
                                         Integer size,
                                         String sort,
                                         String order,
                                         String username,
                                         String email) {
        Wrapper<User> result = null;

        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&username=" + (username == null ? "" : username) +
                    "&email=" + (email == null ? "" : email);
            String uri = environment.getProperty("rest.url.api") + "/users" + query;
            ResponseEntity<Wrapper<User>> response = new RestTemplate().exchange(
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

    public Wrapper<User> findAllByUsernameOrEmail(Integer page,
                                                  Integer size,
                                                  String sort,
                                                  String order,
                                                  String username,
                                                  String email) {
        Wrapper<User> result = null;

        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&username=" + (username == null ? "" : username) +
                    "&email=" + (email == null ? "" : email);
            String uri = environment.getProperty("rest.url.api") + "/users/notifications" + query;
            ResponseEntity<Wrapper<User>> response = new RestTemplate().exchange(
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

    public User findByEmail(String email) {

        User result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users/login/" + email;
            ResponseEntity<User> response = new RestTemplate().exchange(
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

    public User save(User user) {
        User result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users";
            HttpEntity<User> requestEntity = new HttpEntity<>(user, null);

            ResponseEntity<User> response = new RestTemplate().exchange(
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

    public User update(User user) {
        User result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users";
            HttpEntity<User> requestEntity = new HttpEntity<>(user, null);

            ResponseEntity<User> response = new RestTemplate().exchange(
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

    public List<UserActivity> getActivity(Integer id) {

        List<UserActivity> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users/" + id + "/activity";
            ResponseEntity<List<UserActivity>> response = new RestTemplate().exchange(
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

    public List<UserIdAndPostCount> getMostActiveUsers(String date,
                                                       int limit) {

        List<UserIdAndPostCount> result = null;

        try {
            String query = "?date=" + date + "&limit=" + limit;
            String uri = environment.getProperty("rest.url.api") + "/users/most-active" + query;
            ResponseEntity<List<UserIdAndPostCount>> response = new RestTemplate().exchange(
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

    public UserStatistics getStatistics(Integer id) {

        UserStatistics result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/users/" + id + "/statistics";
            ResponseEntity<UserStatistics> response = new RestTemplate().exchange(
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
