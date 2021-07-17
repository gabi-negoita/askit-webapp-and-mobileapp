package com.project.askit.rest;

import com.project.askit.entity.User;
import com.project.askit.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.sql.Date;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRestControllerTest {

    private static boolean isSetupDone = false;
    private static int numberOfObjects = 10;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() {
        if (isSetupDone) return;

        for (int i = 1; i <= numberOfObjects; i++) {
            User object = new User(
                    "username_" + i,
                    "password_" + i,
                    "address_" + i + "@email.com",
                    Date.valueOf("2000-01-01"),
                    "Description#" + i,
                    new Timestamp(System.currentTimeMillis()),
                    0);
            repository.save(object);
        }

        isSetupDone = true;
    }

    @Test
    public void getMethodShouldReturnAllObjects() throws Exception {
        /* Without parameters */
        String uri = "/api/users";
        String expectedString = "\"totalItems\":" + numberOfObjects;
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        /* With parameters */
        uri += "?page=1&size=&sort=username&order=desc&username=&email=";
        expectedString = "\"currentPage\":1";
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnErrorOnInvalidParameters() throws Exception {
        /* Page parameter contains letters instead of numbers */
        String uri = "/api/users?page=abc&size=&sort=username&order=desc&username=&email=";
        mvc.perform(get(uri))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        /* Size parameter contains letters instead of numbers */
        uri = "/api/users?page=0&size=abc&sort=username&order=desc&username=&email=";
        mvc.perform(get(uri))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnObjectById() throws Exception {
        int id = 1;
        String uri = "/api/users/" + id;

        String expectedString = "\"id\":" + id;

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnErrorOnInvalidId() throws Exception {
        int id = 999;
        String uri = "/api/users/" + id;
        String expectedString = "Provide a valid id";

        mvc.perform(get(uri))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnObjectByEmail() throws Exception {
        int id = 1;
        String email = "address_" + id + "@email.com";
        String uri = "/api/users/login/" + email;
        String expectedString = "\"email\":\"" + email + "\"";

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnErrorOnInvalidEmail() throws Exception {
        int id = 999;
        String email = "address_" + id + "@email.com";
        String uri = "/api/users/login/" + email;
        String expectedString = "Provide valid parameters";

        mvc.perform(get(uri))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postMethodShouldSaveObject() throws Exception {
        String body = "{" +
                "\"username\": \"user\"," +
                "\"password\": \"pass\"," +
                "\"email\": \"test@email.com\"," +
                "\"dateOfBirth\": \"" + Date.valueOf("2000-01-01") + "\"," +
                "\"description\": \"Test description\"," +
                "\"status\": 0," +
                "\"createdDate\": \"2021-01-01T00:00:00.000+00:00\"," +
                "\"roles\": []" +
                "}";
        String uri = "/api/users";
        String expectedString = "\"email\":\"test@email.com\"";

        mvc.perform(post(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects++;
    }

    @Test
    public void postMethodShouldReturnErrorOnInvalidBody() throws Exception {
        String body = "{" +
                "\"username\": \"user\"," +
                "\"password\": \"pass\"," +
                "\"email\": \"test@email.com\"," +
                "\"dateOfBirth\": \"" + Date.valueOf("2000-01-01") + "\"," +
                "\"description\": \"Test description\"," +
                "\"status\": 0," +
                "\"createdDate\": \"2021-01-01T00:00:00.000+00:00\"," +
                "}";
        String uri = "/api/users";

        mvc.perform(post(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects++;
    }

    @Test
    public void putMethodShouldUpdateObject() throws Exception {
        String body = "{" +
                "\"id\": 11," +
                "\"username\": \"user\"," +
                "\"password\": \"pass\"," +
                "\"email\": \"testupdated@email.com\"," +
                "\"dateOfBirth\": \"" + Date.valueOf("2000-01-01") + "\"," +
                "\"description\": \"Test description\"," +
                "\"status\": 0," +
                "\"createdDate\": \"2021-01-01T00:00:00.000+00:00\"," +
                "\"roles\": []" +
                "}";
        String uri = "/api/users";
        String expectedString = "\"email\":\"testupdated@email.com\"";

        mvc.perform(put(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void putMethodShouldReturnErrorOnInvalidBody() throws Exception {
        String body = "{" +
                "\"id\": 11," +
                "\"username\": \"user\"," +
                "\"password\": \"pass\"," +
                "\"email\": \"testupdated@email.com\"," +
                "\"dateOfBirth\": \"" + Date.valueOf("2000-01-01") + "\"," +
                "\"description\": \"Test description\"," +
                "\"status\": 0," +
                "\"createdDate\": \"2021-01-01T00:00:00.000+00:00\"," +
                "}";
        String uri = "/api/users";

        mvc.perform(put(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteMethodShouldDeleteObject() throws Exception {
        int id = 11;
        String uri = "/api/users/" + id;

        String expectedString = "\"id\":" + id;

        mvc.perform(delete(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects--;
    }

    @Test
    public void deleteMethodShouldReturnErrorOnInvalidId() throws Exception {
        int id = 999;
        String uri = "/api/users/" + id;
        String expectedString = "Provide a valid id";

        mvc.perform(delete(uri))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects--;
    }
}