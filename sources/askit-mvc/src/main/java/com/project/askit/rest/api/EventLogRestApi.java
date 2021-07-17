package com.project.askit.rest.api;

import com.project.askit.entity.EventLog;
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
public class EventLogRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventLogRestApi(Environment environment) {
        this.environment = environment;
    }

    public Wrapper<EventLog> findAllByFields(Integer page,
                                             Integer size,
                                             String sort,
                                             String order,
                                             String action,
                                             String info,
                                             Integer userId) {
        Wrapper<EventLog> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/event-logs";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&action=" + (action == null ? "" : action) +
                    "&info=" + (info == null ? "" : info) +
                    "&userId=" + (userId == null ? "" : userId);
            ResponseEntity<Wrapper<EventLog>> response = new RestTemplate().exchange(
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

    public EventLog save(EventLog object) {
        EventLog result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/event-logs";
            HttpEntity<EventLog> requestEntity = new HttpEntity<>(object, null);

            ResponseEntity<EventLog> response = new RestTemplate().exchange(
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
}
