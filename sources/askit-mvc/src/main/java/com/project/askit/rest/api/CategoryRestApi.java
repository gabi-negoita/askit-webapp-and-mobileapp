package com.project.askit.rest.api;

import com.project.askit.entity.Category;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.CategoryIdAndPosts;
import com.project.askit.model.CategoryStatistics;
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
public class CategoryRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryRestApi(Environment environment) {
        this.environment = environment;
    }

    public Wrapper<Category> findAll(Integer page,
                                     Integer size,
                                     String sort,
                                     String order,
                                     String title) {
        Wrapper<Category> result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories";
            String query = "?page=" + (page == null ? "" : page) +
                    "&size=" + (size == null ? "" : size) +
                    "&sort=" + (sort == null ? "" : sort) +
                    "&order=" + (order == null ? "" : order) +
                    "&title=" + (title == null ? "" : title);
            ResponseEntity<Wrapper<Category>> response = new RestTemplate().exchange(
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

    public Category findByTitle(String title) {
        Category result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories/title/" + title;
            ResponseEntity<Category> response = new RestTemplate().exchange(
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

    public Category findById(Integer id) {
        Category result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories/" + id;
            ResponseEntity<Category> response = new RestTemplate().exchange(
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

    public CategoryStatistics getStatistics(Integer id) {
        CategoryStatistics result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories/" + id + "/statistics";
            ResponseEntity<CategoryStatistics> response = new RestTemplate().exchange(
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

    public List<CategoryIdAndPosts> getMostUsedCategories(String date,
                                                          int limit) {

        List<CategoryIdAndPosts> result = null;

        try {
            String query = "?date=" + date + "&limit=" + limit;
            String uri = environment.getProperty("rest.url.api") + "/categories/most-used" + query;
            ResponseEntity<List<CategoryIdAndPosts>> response = new RestTemplate().exchange(
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

    public List<CategoryIdAndPosts> getMostQuestionsCategories(int limit) {

        List<CategoryIdAndPosts> result = null;

        try {
            String query = "?limit=" + limit;
            String uri = environment.getProperty("rest.url.api") + "/categories/most-questions" + query;
            ResponseEntity<List<CategoryIdAndPosts>> response = new RestTemplate().exchange(
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

    public Category save(Category category) {
        Category result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories";
            HttpEntity<Category> requestEntity = new HttpEntity<>(category, null);
            ResponseEntity<Category> response = new RestTemplate().exchange(
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

    public Category update(Category category) {
        Category result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories";
            HttpEntity<Category> requestEntity = new HttpEntity<>(category, null);

            ResponseEntity<Category> response = new RestTemplate().exchange(
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

    public Category deleteById(Integer id) {
        Category result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/categories/" + id;
            ResponseEntity<Category> response = new RestTemplate().exchange(
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
