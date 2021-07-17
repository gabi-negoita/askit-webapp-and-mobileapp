package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.rest.api.EventLogRestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutPageController extends AbstractPageController {

    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    public LogoutPageController(EventLogRestApi eventLogRestApi) {
        super();

        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/logout")
    public Object rootRedirect(HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            User sessionUser = getSessionUser(request);
            if (sessionUser == null) throw new Exception(NOT_LOGGED_IN);
            request.getSession().invalidate();

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.SELECT);
            log.setInfo("Logged out user with id " + sessionUser.getId());
            log.setUserId(sessionUser.getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/login", true);
    }
}