package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.Question;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UnreviewedQuestionsPageController extends AbstractPageController {

    private final QuestionRestApi questionRestApi;

    private final CategoryRestApi categoryRestApi;
    private final UserRestApi userRestApi;

    private final Logger logger;

    @Autowired
    public UnreviewedQuestionsPageController(QuestionRestApi questionRestApi,
                                             CategoryRestApi categoryRestApi,
                                             UserRestApi userRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.categoryRestApi = categoryRestApi;
        this.userRestApi = userRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/reviews/questions")
    public Object showUnreviewedQuestionsPage(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(defaultValue = "oldest") String sort,
                                              @RequestParam(defaultValue = "") String categoryTitle,
                                              Model model,
                                              HttpServletRequest request,
                                              RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Set question page
            page = page == 0 ? page : page - 1;

            // Set question sort by and order
            String sortBy = "";
            String order = "";
            switch (sort) {
                case "newest": {
                    sortBy = "createdDate";
                    order = "desc";
                    break;
                }
                case "oldest": {
                    sortBy = "createdDate";
                    order = "asc";
                    break;
                }
                case "atoz": {
                    sortBy = "subject";
                    order = "asc";
                    break;
                }
                case "ztoa": {
                    sortBy = "subject";
                    order = "desc";
                    break;
                }
            }

            // Get data
            Integer categoryId = null;
            if (!categoryTitle.isBlank()) {
                Category category = categoryRestApi.findByTitle(categoryTitle);
                if (category == null) throw new Exception(REST_API_CALL_FAILED);

                categoryId = category.getId();
            }

            Wrapper<Question> questionWrapper = questionRestApi.findAllByFields(page, size, sortBy, order, null, null, categoryId, null, 0);
            if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get question pagination
            List<Integer> questionPagination = Pagination.getPagination(questionWrapper.getCurrentPage(), 3, questionWrapper.getTotalPages());

            // Map user to question's userId
            Map<Integer, User> userMap = new HashMap<>();

            // Map category to question's categoryId
            Map<Integer, Category> categoryMap = new HashMap<>();

            for (Question q : questionWrapper.getContent()) {
                // Get user
                User user = userRestApi.findById(q.getUserId());
                if (user == null) throw new Exception(REST_API_CALL_FAILED);

                // Add to map required data
                userMap.put(q.getUserId(), user);

                // Add data to category map if necessary
                if (categoryMap.containsKey(q.getCategoryId())) continue;

                Category category = categoryRestApi.findById(q.getCategoryId());
                if (category == null) throw new Exception(REST_API_CALL_FAILED);

                categoryMap.put(q.getCategoryId(), category);
            }

            model.addAttribute("questionWrapper", questionWrapper);
            model.addAttribute("questionPagination", questionPagination);
            model.addAttribute("categoryMap", categoryMap);
            model.addAttribute("userMap", userMap);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("sort", sort);
            model.addAttribute("categoryTitle", categoryTitle);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "unreviewed-questions";
    }
}