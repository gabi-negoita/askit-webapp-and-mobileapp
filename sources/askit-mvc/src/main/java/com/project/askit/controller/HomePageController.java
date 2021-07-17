package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.Question;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.CategoryIdAndPosts;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.rest.api.QuestionRestApi;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomePageController extends AbstractPageController {

    private final CategoryRestApi categoryRestApi;
    private final QuestionRestApi questionRestApi;

    private final Logger logger;

    public HomePageController(CategoryRestApi categoryRestApi,
                              QuestionRestApi questionRestApi) {
        super();
        this.categoryRestApi = categoryRestApi;
        this.questionRestApi = questionRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home")
    public Object showPage(RedirectAttributes redirectAttributes,
                           Model model) {

        try {
            int limit = 4;
            // Get most used categories
            List<CategoryIdAndPosts> categoryIdAndPostsList = categoryRestApi.getMostQuestionsCategories(limit);
            if (categoryIdAndPostsList == null) throw new Exception(REST_API_CALL_FAILED);

            // Get most voted questions by categories
            List<Pair<String, List<Question>>> topPosts = new ArrayList<>();

            for (CategoryIdAndPosts item : categoryIdAndPostsList) {
                Category category = categoryRestApi.findById(item.getCategoryId());
                if (category == null) throw new Exception(REST_API_CALL_FAILED);

                Wrapper<Question> questionWrapper = questionRestApi.findByQuery(0, 3, "votes", "desc", category.getTitle(), "", "", 1);
                if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);

                Pair<String, List<Question>> pair = new Pair<>();
                pair.setKey(category.getTitle());
                pair.setValue(questionWrapper.getContent());
                topPosts.add(pair);
            }

            // Add data to model
            model.addAttribute("topPosts", topPosts);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "home";
    }
}