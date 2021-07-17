package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.CategoryIdAndPosts;
import com.project.askit.model.CategoryModel;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoriesPageController extends AbstractPageController {

    private final CategoryRestApi categoryRestApi;

    private final Logger logger;

    @Autowired
    public CategoriesPageController(CategoryRestApi categoryRestApi) {
        super();

        this.categoryRestApi = categoryRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/categories")
    public Object showCategoriesPage(HttpServletRequest request,
                                     Model model,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "titleatoz") String sortBy,
                                     @RequestParam(defaultValue = "") String search) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Set page
            page = page == 0 ? page : page - 1;

            String sort = "";
            String order = "";

            // Get sortBy and order
            switch (sortBy) {
                case "titleatoz": {
                    sort = "title";
                    break;
                }
                case "titleztoa": {
                    sort = "title";
                    order = "desc";
                    break;
                }
            }

            // Get categories
            Wrapper<Category> categoryWrapper = categoryRestApi.findAll(page, size, sort, order, search);
            if (categoryWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get pagination
            List<Integer> pagination = Pagination.getPagination(categoryWrapper.getCurrentPage(), 3, categoryWrapper.getTotalPages());

            // Add data to model
            if (model.getAttribute("categoryModel") == null) model.addAttribute("categoryModel", new CategoryModel());
            model.addAttribute("categoryWrapper", categoryWrapper);

            model.addAttribute("pagination", pagination);
            model.addAttribute("search", search);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("size", size);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "categories";
    }

    @ResponseBody
    @RequestMapping("/home/categories/most-used")
    public Object getMostUsedCategories(HttpServletRequest request) {

        // Check if user is logged in
        if (!isLoggedIn(request)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);

        // Check if user has access
        if (!hasAccess(request, "moderator")) return new ResponseEntity<>(NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);

        // Get username
        String date = request.getParameter("date");

        // Get users
        List<CategoryIdAndPosts> mostUsedCategories = categoryRestApi.getMostUsedCategories(date, 5);
        if (mostUsedCategories == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        List<Map<String, Object>> response = new ArrayList<>();

        // Post-process response
        for (CategoryIdAndPosts item : mostUsedCategories) {
            Category category = categoryRestApi.findById(item.getCategoryId());
            if (category == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

            Map<String, Object> row = new HashMap<>();
            row.put("id", item.getCategoryId());
            row.put("categoryTitle", category.getTitle());
            row.put("posts", item.getPosts());

            response.add(row);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
