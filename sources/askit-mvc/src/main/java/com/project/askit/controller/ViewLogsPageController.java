package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.rest.api.EventLogRestApi;
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
public class ViewLogsPageController extends AbstractPageController {

    private final EventLogRestApi eventLogRestApi;
    private final UserRestApi userRestApi;

    private final Logger logger;

    @Autowired
    public ViewLogsPageController(EventLogRestApi eventLogRestApi,
                                  UserRestApi userRestApi) {
        this.eventLogRestApi = eventLogRestApi;
        this.userRestApi = userRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/logs")
    public Object showViewLogsPage(Model model,
                                   HttpServletRequest request,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(defaultValue = "newest") String sortBy,
                                   @RequestParam(defaultValue = "") String action,
                                   @RequestParam(defaultValue = "") String search,
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
                case "oldest": {
                    sort = "createdDate";
                    break;
                }
                case "newest": {
                    sort = "createdDate";
                    order = "desc";
                    break;
                }
                case "actionatoz": {
                    sort = "action";
                    break;
                }
                case "actionztoa": {
                    sort = "action";
                    order = "desc";
                    break;
                }
                case "detailsatoz": {
                    sort = "info";
                    break;
                }
                case "detailsztoa": {
                    sort = "info";
                    order = "desc";
                    break;
                }
            }

            // Get logs
            Wrapper<EventLog> eventLogWrapper = eventLogRestApi.findAllByFields(page, size, sort, order, action, search, null);
            if (eventLogWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get log username
            Map<Integer, User> userMap = new HashMap<>();
            for (EventLog log : eventLogWrapper.getContent()) {
                // Get user
                User user = userRestApi.findById(log.getUserId());
                if (user == null) throw new Exception(REST_API_CALL_FAILED);

                userMap.put(log.getUserId(), user);
            }

            // Get pagination
            List<Integer> pagination = Pagination.getPagination(eventLogWrapper.getCurrentPage(), 3, eventLogWrapper.getTotalPages());

            model.addAttribute("eventLogWrapper", eventLogWrapper);
            model.addAttribute("userMap", userMap);
            model.addAttribute("pagination", pagination);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("action", action);
            model.addAttribute("search", search);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "logs";
    }
}