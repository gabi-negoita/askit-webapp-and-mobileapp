package com.project.askit.controller;

import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.UserIdAndPostCount;
import com.project.askit.rest.api.UserRestApi;
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
public class ViewUsersPageController extends AbstractPageController {

    private final UserRestApi userRestApi;

    private final Logger logger;

    @Autowired
    public ViewUsersPageController(UserRestApi userRestApi) {
        super();

        this.userRestApi = userRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/users")
    public Object showViewUsersPage(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(defaultValue = "usernameatoz") String sortBy,
                                    @RequestParam(defaultValue = "") String search,
                                    Model model,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Set page
            page = page == 0 ? page : page - 1;

            String sort = "";
            String order = "";

            // Get sortBy and order
            switch (sortBy) {
                case "usernameatoz": {
                    sort = "username";
                    break;
                }
                case "usernameztoa": {
                    sort = "username";
                    order = "desc";
                    break;
                }
                case "emailatoz": {
                    sort = "email";
                    break;
                }
                case "emailztoa": {
                    sort = "email";
                    order = "desc";
                    break;
                }
                case "newest": {
                    sort = "createdDate";
                    order = "desc";
                    break;
                }
                case "oldest": {
                    sort = "createdDate";
                    break;
                }
            }

            // Get users
            Wrapper<User> userWrapper = userRestApi.findAllByUsernameOrEmail(page, size, sort, order, search, search);
            if (userWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get array of pages that are going to be displayed into the pagination part
            List<Integer> pagination = Pagination.getPagination(userWrapper.getCurrentPage(), 3, userWrapper.getTotalPages());

            model.addAttribute("userWrapper", userWrapper);
            model.addAttribute("pagination", pagination);
            model.addAttribute("search", search);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("page", page);
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

        return "users";
    }

    @ResponseBody
    @RequestMapping("/home/users/most-active")
    public Object getMostActiveUsers(HttpServletRequest request) {

        // Check if user is logged in
        if (!isLoggedIn(request)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);

        // Check if user has access
        if (!hasAccess(request, "administrator")) return new ResponseEntity<>(NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);

        // Get username
        String date = request.getParameter("date");

        // Get users
        List<UserIdAndPostCount> mostActiveUsers = userRestApi.getMostActiveUsers(date, 5);
        if (mostActiveUsers == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        List<Map<String, Object>> response = new ArrayList<>();

        // Post-process response
        for (UserIdAndPostCount item : mostActiveUsers) {
            User user = userRestApi.findById(item.getUserId());
            if (user == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

            Map<String, Object> row = new HashMap<>();
            row.put("id", item.getUserId());
            row.put("username", user.getUsername());
            row.put("contributions", item.getPosts());

            response.add(row);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}