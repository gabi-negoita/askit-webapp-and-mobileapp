package com.project.askit.controller;

import com.project.askit.entity.Category;
import com.project.askit.entity.EventLog;
import com.project.askit.entity.Question;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.CategoryModel;
import com.project.askit.model.CategoryStatistics;
import com.project.askit.model.MessageModel;
import com.project.askit.rest.api.CategoryRestApi;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.QuestionRestApi;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ManageCategoryPageController extends AbstractPageController {

    private final CategoryRestApi categoryRestApi;
    private final QuestionRestApi questionRestApi;
    private final EventLogRestApi eventLogRestApi;

    Logger logger;

    @Autowired
    public ManageCategoryPageController(CategoryRestApi categoryRestApi,
                                        QuestionRestApi questionRestApi,
                                        EventLogRestApi eventLogRestApi) {

        super();

        this.categoryRestApi = categoryRestApi;
        this.questionRestApi = questionRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/categories/manage-category/{categoryId}")
    public Object manageViewCategoryPage(HttpServletRequest request,
                                       @PathVariable Integer categoryId,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Get category
            Category category = categoryRestApi.findById(categoryId);
            if (category == null) throw new Exception(REST_API_CALL_FAILED);

            // Get category statistics
            CategoryStatistics categoryStatistics = categoryRestApi.getStatistics(categoryId);
            if (categoryStatistics == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if category is safe to delete
            Wrapper<Question> questionWrapper = questionRestApi.findAllByFields(null, null, null, null, null, null, categoryId, null, null);
            if (questionWrapper == null) throw new Exception(REST_API_CALL_FAILED);
            boolean isSafeToDelete = questionWrapper.getTotalItems() == 0;

            // Set up model
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setId(category.getId());
            categoryModel.setTitle(category.getTitle());

            // Add data to model
            model.addAttribute("categoryModel", categoryModel);
            model.addAttribute("isSafeToDelete", isSafeToDelete);
            model.addAttribute("categoryStatistics", categoryStatistics);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "manage-category";
    }

    @RequestMapping("/home/categories/manage-category/save-changes/{categoryId}")
    public Object changeCategoryTitle(HttpServletRequest request,
                                      @Valid @ModelAttribute("categoryModel") CategoryModel categoryModel,
                                      BindingResult result,
                                      @PathVariable Integer categoryId,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Check for field invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Check if category already exists
            Category existingCategory = categoryRestApi.findByTitle(categoryModel.getTitle());
            if (existingCategory != null) throw new Exception(ALREADY_EXISTS);

            // Get data
            Category category = categoryRestApi.findById(categoryId);
            if (category == null) throw new Exception(REST_API_CALL_FAILED);

            String oldTitle = category.getTitle();

            // Update data
            category.setTitle(categoryModel.getTitle());

            // Save changes
            Category updatedCategory = categoryRestApi.update(category);
            if (updatedCategory == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed title from \"" + oldTitle + "\" to \"" + category.getTitle() + "\" of category with id " + category.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Category changes have been successfully saved"));

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

            redirectAttributes.addFlashAttribute("categoryModel", categoryModel);
        }

        return new RedirectView("/home/categories/manage-category/" + categoryId, true);
    }

    @RequestMapping("/home/categories/manage-category/delete/{categoryId}")
    public Object deleteCategory(HttpServletRequest request,
                                 @PathVariable Integer categoryId,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "moderator")) throw new Exception(NOT_AUTHORIZED);

            // Save changes
            Category deletedCategory = categoryRestApi.deleteById(categoryId);
            if (deletedCategory == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.DELETE);
            log.setInfo("Deleted category with title \"" + deletedCategory.getTitle() + "\"");
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(deletedCategory.getTitle(), "category has been successfully deleted"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/home/categories", true);
    }
}
