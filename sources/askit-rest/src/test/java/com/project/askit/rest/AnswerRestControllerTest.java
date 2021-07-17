package com.project.askit.rest;

import com.project.askit.entity.Answer;
import com.project.askit.repository.AnswerRepository;
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
public class AnswerRestControllerTest {

    private static boolean isSetupDone = false;
    private static int numberOfObjects = 10;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AnswerRepository repository;

    @Before
    public void setUp() {
        if (isSetupDone) return;

        for (int i = 1; i <= numberOfObjects; i++) {
            Answer object = new Answer(
                    "<p>htmlText_" + i + "</p>",
                    new Timestamp(System.currentTimeMillis()),
                    1,
                    1,
                    0,
                    "comment_" + i);
            repository.save(object);
        }

        isSetupDone = true;
    }

    @Test
    public void getMethodShouldReturnAllObjects() throws Exception {
        String uri = "/api/answers";
        String expectedString = "\"totalItems\":" + numberOfObjects;

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMethodShouldReturnObjectById() throws Exception {
        int id = 1;
        String uri = "/api/answers/" + id;
        String expectedString = "\"id\":" + id;

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postMethodShouldSaveObject() throws Exception {
        String body = "{\"htmlText\":\"<p>htmlText</p>\"," +
                "\"createdDate\":\"2000-01-01T00:00:00.000+00:00\"," +
                "\"categoryId\":1," +
                "\"userId\":1," +
                "\"approved\":0," +
                "\"comment\":\"comment\"}";
        String uri = "/api/answers";
        String expectedString = "\"htmlText\":\"<p>htmlText</p>\"";

        mvc.perform(post(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects++;
    }

    @Test
    public void putMethodShouldUpdateObject() throws Exception {
        String body = "{\"id\":11," +
                "\"htmlText\":\"<p>htmlTextupdated</p>\"," +
                "\"createdDate\":\"2000-01-01T00:00:00.000+00:00\"," +
                "\"categoryId\":1," +
                "\"userId\":1," +
                "\"approved\":0," +
                "\"comment\":\"comment\"}";
        String uri = "/api/answers";
        String expectedString = "\"htmlText\":\"<p>htmlTextupdated</p>\"";

        mvc.perform(put(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteMethodShouldDeleteObject() throws Exception {
        int id = 11;
        String uri = "/api/answers/" + id;
        String expectedString = "\"id\":" + id;

        mvc.perform(delete(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects--;
    }
}