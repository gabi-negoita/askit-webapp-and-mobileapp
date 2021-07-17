package com.project.askit.rest.api;

import com.project.askit.entity.Role;
import com.project.askit.entity.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RoleRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RoleRestApi(Environment environment) {
        this.environment = environment;
    }

    public Wrapper<Role> findAllByFields(Integer page,
                                         Integer size,
                                         String sort,
                                         String order,
                                         String name) {
        Wrapper<Role> result = null;

        try {
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&name=" + (name == null ? "" : name);
            String uri = environment.getProperty("rest.url.api") + "/roles" + query;
            ResponseEntity<Wrapper<Role>> response = new RestTemplate().exchange(
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

    public Role findById(Integer id) {

        Role result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/roles/" + id;
            ResponseEntity<Role> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.GET, null,
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
