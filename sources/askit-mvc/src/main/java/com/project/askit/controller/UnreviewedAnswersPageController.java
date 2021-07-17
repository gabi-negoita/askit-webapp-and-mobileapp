package com.project.askit.controller;

import com.project.askit.entity.Answer;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.rest.api.AnswerRestApi;
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
public class UnreviewedAnswersPageController extends AbstractPageController {

    private final AnswerRestApi answerRestApi;
    private final UserRestApi userRestApi;

    private final Logger logger;

    @Autowired
    public UnreviewedAnswersPageController(AnswerRestApi answerRestApi,
                                           UserRestApi userRestApi) {
        super();

        this.answerRestApi = answerRestApi;
        this.userRestApi = userRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/reviews/answers")
    public Object showUnreviewedAnswersPage(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(defaultValue = "oldest") String sort,
                                            Model model,
                                            HttpServletRequest request,
                                            RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "reviewer")) throw new Exception(NOT_AUTHORIZED);

            // Set answer page
            page = page == 0 ? page : page - 1;

            // Set answer sort by and order
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
            }

            // Get answers
            Wrapper<Answer> answerWrapper = answerRestApi.findAllByFields(page, size, sortBy, order, null, 0, null);
            if (answerWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get answer pagination
            List<Integer> answerPagination = Pagination.getPagination(answerWrapper.getCurrentPage(), 3, answerWrapper.getTotalPages());

            // Add to userMap answer's user
            Map<Integer, User> userMap = new HashMap<>();
            for (Answer answer : answerWrapper.getContent()) {
                // Get user
                User user = userRestApi.findById(answer.getUserId());
                if (user == null) throw new Exception(REST_API_CALL_FAILED);

                // Add to map required data
                userMap.put(answer.getUserId(), user);
            }

            model.addAttribute("answerWrapper", answerWrapper);
            model.addAttribute("answerPagination", answerPagination);
            model.addAttribute("userMap", userMap);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("sort", sort);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "unreviewed-answers";
    }
}