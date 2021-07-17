package com.project.askit.controller;

import com.project.askit.model.ErrorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ErrorPageController extends AbstractPageController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ErrorPageController() {
        super();
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,
                              Model model) {
        ErrorModel errorModel = new ErrorModel();

        if (model.getAttribute("errorModel") == null) {
            List<String> details = new ArrayList<>();

            Integer errorCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (errorCode == null) errorCode = 500;

            if (errorCode < 500) details.add("This page could not be found or is unavailable right now");
            else if (errorCode < 600) details.add("Something went wrong");

            errorModel.setCode(errorCode);
            errorModel.setDetails(details);
            model.addAttribute("errorModel", new ErrorModel(errorCode, details));
        }

        logger.error(errorModel.toString());

        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
