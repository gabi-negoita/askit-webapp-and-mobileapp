package com.project.askit.controller;

import com.project.askit.entity.Notification;
import com.project.askit.rest.api.NotificationRestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewNotificationPageController extends AbstractPageController {

    private final NotificationRestApi notificationRestApi;

    Logger logger;

    @Autowired
    public ViewNotificationPageController(NotificationRestApi notificationRestApi) {

        super();

        this.notificationRestApi = notificationRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/profile/notifications/view-notification/{notificationId}")
    public Object showViewNotificationPage(HttpServletRequest request,
                                           @PathVariable Integer notificationId,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get notification
            Notification notification = notificationRestApi.findById(notificationId);
            if (notification == null) throw new Exception(REST_API_CALL_FAILED);

            // Set notification as viewed
            if (notification.getViewed() != null && notification.getViewed() == 0) {
                notification.setViewed(1);
                Notification updatedNotification = notificationRestApi.update(notification);
                if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);
            }

            // Add data to model
            model.addAttribute("notification", notification);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "view-notification";
    }
}
