package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.Question;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.QuestionStatistics;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.rest.api.QuestionRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuestionsPageController extends AbstractPageController {

    private final CategoryRestApi categoryRestApi;
    private final QuestionRestApi questionRestApi;
    private final UserRestApi userRestApi;

    private final Logger logger;

    @Autowired
    public QuestionsPageController(CategoryRestApi categoryRestApi,
                                   QuestionRestApi questionRestApi,
                                   UserRestApi userRestApi) {
        super();

        this.categoryRestApi = categoryRestApi;
        this.questionRestApi = questionRestApi;
        this.userRestApi = userRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/questions")
    public Object showQuestionsPage(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(defaultValue = "newest") String sortBy,
                                    @RequestParam(defaultValue = "") String category,
                                    @RequestParam(defaultValue = "") String search,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Get categories
            Wrapper<Category> categoryWrapper = categoryRestApi.findAll(null, Integer.MAX_VALUE, "title", null, null);
            if (categoryWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Map each category as categoryId - category
            Map<Integer, Category> categoryMap = new HashMap<>();
            for (Category c : categoryWrapper.getContent()) {
                Integer id = c.getId();
                c.setId(null);

                categoryMap.put(id, c);
            }

            // Set page
            page = page == 0 ? page : page - 1;

            String sort = "";
            String order = "";

            // Get sortBy and order
            switch (sortBy) {
                case "oldest": {
                    sort = "created_date";
                    break;
                }
                case "newest": {
                    sort = "created_date";
                    order = "desc";
                    break;
                }
                case "leastvotes": {
                    sort = "votes";
                    break;
                }
                case "mostvotes": {
                    sort = "votes";
                    order = "desc";
                    break;
                }
                case "leastanswers": {
                    sort = "answers";
                    break;
                }
                case "mostanswers": {
                    sort = "answers";
                    order = "desc";
                    break;
                }
                case "atoz": {
                    sort = "subject";
                    break;
                }
                case "ztoa": {
                    sort = "subject";
                    order = "desc";
                    break;
                }
            }

            // Get question wrapper
            Wrapper<Question> questionWrapper = questionRestApi.findByQuery(page, size, sort, order, category, "", search, 1);
            if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get array of pages that are going to be displayed into the pagination part
            List<Integer> pagination = Pagination.getPagination(questionWrapper.getCurrentPage(), 3, questionWrapper.getTotalPages());

            Map<Integer, User> userMap = new HashMap<>();
            for (Question q : questionWrapper.getContent()) {

                // Get question statistics
                QuestionStatistics questionStatistics = questionRestApi.getStatistics(q.getId());
                if (questionStatistics == null) throw new Exception(REST_API_CALL_FAILED);
                q.setQuestionStatistics(questionStatistics);

                // Get each question's username
                Integer userId = q.getUserId();

                // Add data to map
                User user = userRestApi.findById(userId);
                if (user == null) throw new Exception(REST_API_CALL_FAILED);
                userMap.put(userId, user);
            }

            // Add data to model
            model.addAttribute("categoryWrapper", categoryWrapper);
            model.addAttribute("categoryMap", categoryMap);
            model.addAttribute("questionWrapper", questionWrapper);
            model.addAttribute("userMap", userMap);
            model.addAttribute("pagination", pagination);
            model.addAttribute("category", category);
            model.addAttribute("search", search);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("size", size);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "questions";
    }

}
