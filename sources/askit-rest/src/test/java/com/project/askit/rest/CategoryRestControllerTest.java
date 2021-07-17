package com.project.askit.rest;

import com.project.askit.entity.Category;
import com.project.askit.repository.CategoryRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class CategoryRestControllerTest {

    private static boolean isSetupDone = false;
    private static int numberOfObjects = 10;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository repository;

    @Before
    public void setUp() {
        if (isSetupDone) return;

        for (int i = 1; i <= numberOfObjects; i++) {
            Category object = new Category("category_" + i);
            repository.save(object);
        }

        isSetupDone = true;
    }

    @Test
    public void shouldReturnAllObjects() throws Exception {
        String uri = "/api/categories";
        String expectedString = "\"totalItems\":" + numberOfObjects;

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldReturnObjectById() throws Exception {
        int id = 1;
        String uri = "/api/categories/" + id;
        String expectedString = "\"id\":" + id;

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldReturnObjectByTitle() throws Exception {
        int id = 1;
        String title = "category_" + id;
        String uri = "/api/categories/title/" + title;
        String expectedString = "\"title\":\"" + title + "\"";

        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldSaveObject() throws Exception {
        String body = "{" +
                "\"title\": \"test\"" +
                "}";
        String uri = "/api/categories";
        String expectedString = "\"title\":\"test\"";

        mvc.perform(post(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects++;
    }

    @Test
    public void shouldUpdateObject() throws Exception {
        String body = "{" +
                "\"id\": 11," +
                "\"title\": \"testupdated\"" +
                "}";
        String uri = "/api/categories";
        String expectedString = "\"title\":\"testupdated\"";

        mvc.perform(put(uri).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldDeleteObject() throws Exception {
        int id = 11;
        String uri = "/api/categories/" + id;
        String expectedString = "\"id\":" + id;

        mvc.perform(delete(uri))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedString)))
                .andDo(MockMvcResultHandlers.print());

        numberOfObjects--;
    }
}