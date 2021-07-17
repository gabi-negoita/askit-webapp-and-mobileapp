package com.project.askit.controller;

import com.project.askit.entity.Notification;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.rest.api.NotificationRestApi;
import com.project.askit.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationsPageController extends AbstractPageController {

    private final NotificationRestApi notificationRestApi;

    private final Logger logger;

    @Autowired
    public NotificationsPageController(NotificationRestApi notificationRestApi) {
        super();

        this.notificationRestApi = notificationRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/profile/notifications/{userId}")
    public Object showNotificationsPage(Model model,
                                        HttpServletRequest request,
                                        @PathVariable Integer userId,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(defaultValue = "newest") String sortBy,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if page belongs to user
            User sessionUser = getSessionUser(request);
            if (!sessionUser.getId().equals(userId)) throw new Exception(NOT_AUTHORIZED);

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
                }

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

            // Get notifications
            Wrapper<Notification> notificationWrapper = notificationRestApi.findAllByUserId(page, size, sort, order, userId);
            if (notificationWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get pagination
            List<Integer> pagination = Pagination.getPagination(notificationWrapper.getCurrentPage(), 3, notificationWrapper.getTotalPages());

            model.addAttribute("notificationWrapper", notificationWrapper);
            model.addAttribute("pagination", pagination);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "notifications";
    }
}
