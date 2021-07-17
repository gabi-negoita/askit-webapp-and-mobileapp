package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.Notification;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.MessageModel;
import com.project.askit.model.SendNotificationModel;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.NotificationRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SendNotificationPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final NotificationRestApi notificationRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public SendNotificationPageController(UserRestApi userRestApi,
                                          NotificationRestApi notificationRestApi,
                                          EventLogRestApi eventLogRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.notificationRestApi = notificationRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/send-notification")
    public Object showSendNotificationPage(HttpServletRequest request,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if user has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Add data to model
            if (model.getAttribute("sendNotificationModel") == null) model.addAttribute("sendNotificationModel", new SendNotificationModel());

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "send-notification";
    }

    @RequestMapping("/home/send-notification/send")
    public Object sendNotification(HttpServletRequest request,
                                   @Valid @ModelAttribute("sendNotificationModel") SendNotificationModel sendNotificationModel,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if user has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Extra validation: check if notification is global
            Integer viewed = 0;
            if (sendNotificationModel.isBroadcast()) {
                sendNotificationModel.setUserId(null);
                viewed = null;
            } else {
                // Check if user id exists
                User temp = userRestApi.findById(sendNotificationModel.getUserId());
                if (temp == null) throw new Exception(INVALID_USER_ID);
            }

            // Create data
            Notification notification = new Notification();
            notification.setTitle(sendNotificationModel.getTitle());
            notification.setContent(sendNotificationModel.getContent());
            notification.setUserId(sendNotificationModel.getUserId());
            notification.setViewed(viewed);
            notification.setUrl("");
            notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            // Save data
            Notification savedNotification = notificationRestApi.save(notification);
            if (savedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Update url
            savedNotification.setUrl("/home/profile/notifications/view-notification/" + savedNotification.getId());

            // Save changes
            Notification updatedNotification = notificationRestApi.update(savedNotification);
            if (updatedNotification == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.INSERT);
            log.setInfo("Sent notification with id " + updatedNotification.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();

            details.add(new Pair<>(null, "Your notification has been successfully sent"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(INVALID_USER_ID)) handleInvalidUserId(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("sendNotificationModel", sendNotificationModel);
        }

        return new RedirectView("/home/send-notification", true);
    }

    @ResponseBody
    @RequestMapping("/home/send-notification/api/users")
    public Object getUsers(HttpServletRequest request) {

        List<Map<String, Object>> response = new ArrayList<>();
        // Check if user is logged in
        if (!isLoggedIn(request)) return new ResponseEntity<>(NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);

        // Check if user has access
        if (!hasAccess(request, "administrator")) return new ResponseEntity<>(NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);

        // Get username
        String keyWords = request.getParameter("username");

        // Check if username is empty
        if (keyWords.isBlank()) return new ResponseEntity<>(INVALID_FIELDS, HttpStatus.BAD_REQUEST);

        // Get users
        Wrapper<User> userWrapper = userRestApi.findAllByUsernameOrEmail(null, 5, "username", "asc", keyWords, keyWords);
        if (userWrapper == null) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);

        // Post-process data
        for (User user : userWrapper.getContent()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", user.getId());
            item.put("username", user.getUsername());
            item.put("email", user.getEmail());

            response.add(item);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}