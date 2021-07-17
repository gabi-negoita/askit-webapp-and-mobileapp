package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.EventLog;
import com.project.askit.model.CategoryModel;
import com.project.askit.model.MessageModel;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreateCategoryPageController extends AbstractPageController {

    private final CategoryRestApi categoryRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public CreateCategoryPageController(CategoryRestApi categoryRestApi,
                                        EventLogRestApi eventLogRestApi) {
        super();
        this.categoryRestApi = categoryRestApi;

        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/categories/create")
    public Object showCategoriesPage(HttpServletRequest request,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Add data to model
            if (model.getAttribute("categoryModel") == null) model.addAttribute("categoryModel", new CategoryModel());
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "create-category";
    }

    @PostMapping("/home/categories/create")
    public Object createCategory(HttpServletRequest request,
                                 @Valid @ModelAttribute("categoryModel") CategoryModel categoryModel,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get data
            Category newCategory = categoryRestApi.findByTitle(categoryModel.getTitle());
            if (newCategory != null) throw new Exception(ALREADY_EXISTS);

            // Create new category
            newCategory = new Category();
            newCategory.setTitle(categoryModel.getTitle());

            // Save data
            Category savedCategory = categoryRestApi.save(newCategory);
            if (savedCategory == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.INSERT);
            log.setInfo("Created category with id " + savedCategory.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(categoryModel.getTitle(), "category has been successfully created"));

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
            else if (error.equals(ALREADY_EXISTS)) handleAlreadyExists(redirectAttributes, categoryModel.getTitle());
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("categoryModel", categoryModel);

            // Log error
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return new RedirectView("/home/categories/create", true);
    }
}