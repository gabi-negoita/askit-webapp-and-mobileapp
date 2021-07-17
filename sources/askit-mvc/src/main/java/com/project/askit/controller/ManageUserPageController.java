package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.Role;
import com.project.askit.entity.User;
import com.project.askit.entity.Wrapper;
import com.project.askit.model.ChangeAccountRolesModel;
import com.project.askit.model.ChangeAccountStatusModel;
import com.project.askit.model.MessageModel;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.RoleRestApi;
import com.project.askit.rest.api.UserRestApi;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ManageUserPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final RoleRestApi roleRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public ManageUserPageController(UserRestApi userRestApi,
                                    RoleRestApi roleRestApi,
                                    EventLogRestApi eventLogRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.roleRestApi = roleRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/users/manage-user/{userId}")
    public Object showManageUserPage(@PathVariable Integer userId,
                                     Model model,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Get roles
            Wrapper<Role> roleWrapper = roleRestApi.findAllByFields(null, null, null, null, null);
            if (roleWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Create user role map
            Map<Integer, Boolean> userRoleMap = new HashMap<>();
            for (Role role : user.getRoles()) {
                userRoleMap.put(role.getId(), true);
            }

            // Add data to model
            if (model.getAttribute("changeAccountStatusModel") == null) {
                ChangeAccountStatusModel changeAccountStatusModel = new ChangeAccountStatusModel();
                changeAccountStatusModel.setStatus(user.getStatus());

                model.addAttribute("changeAccountStatusModel", changeAccountStatusModel);
            }

            if (model.getAttribute("changeAccountRolesModel") == null) {
                ChangeAccountRolesModel changeAccountRolesModel = new ChangeAccountRolesModel();
                int[] userRoles = new int[user.getRoles().size()];
                int index = 0;
                for (Role role : user.getRoles()) {
                    userRoles[index++] = role.getId();
                }

                changeAccountRolesModel.setRoles(userRoles);

                model.addAttribute("changeAccountRolesModel", changeAccountRolesModel);
            }

            model.addAttribute("user", user);
            model.addAttribute("roleWrapper", roleWrapper);
            model.addAttribute("userRoleMap", userRoleMap);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "manage-user";
    }

    @RequestMapping("/home/users/manage-user/change-status/{userId}")
    public Object changeAccountStatus(@PathVariable Integer userId,
                                      HttpServletRequest request,
                                      @Valid @ModelAttribute("changeAccountStatusModel") ChangeAccountStatusModel changeAccountStatusModel,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            int oldStatus = user.getStatus();

            // Update user data
            user.setStatus(changeAccountStatusModel.getStatus());

            // Save changes
            User updatedUser = userRestApi.update(user);
            if (updatedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed account status from \"" + oldStatus + "\" to \"" + user.getStatus() + "\" of user with id " + updatedUser.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setType(MessageModel.TYPE_SUCCESS);
            messageModel.setMessage("Success!");

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Account status successfully changed"));

            messageModel.setDetails(details);

            // Add message to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("changeAccountStatusModel", changeAccountStatusModel);
        }

        return new RedirectView("/home/users/manage-user/" + userId, true);
    }

    @RequestMapping("/home/users/manage-user/change-roles/{userId}")
    public Object changeAccountRoles(@PathVariable Integer userId,
                                     HttpServletRequest request,
                                     @Valid @ModelAttribute("changeAccountRolesModel") ChangeAccountRolesModel changeAccountRolesModel,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check if has access
            if (!hasAccess(request, "administrator")) throw new Exception(NOT_AUTHORIZED);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Get user new roles
            int[] roleIds = changeAccountRolesModel.getRoles();
            List<Role> newRoles = new ArrayList<>();
            if (roleIds != null) {
                for (int roleId : roleIds) {
                    Role role = roleRestApi.findById(roleId);
                    if (role == null) throw new Exception(REST_API_CALL_FAILED);

                    newRoles.add(role);
                }
            }

            // Update data
            user.setRoles(newRoles);

            // Save changes
            User updatedUser = userRestApi.update(user);
            if (updatedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed roles of user with id " + updatedUser.getId());
            log.setUserId(getSessionUser(request).getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setType(MessageModel.TYPE_SUCCESS);
            messageModel.setMessage("Success!");

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "User roles successfully changed"));

            messageModel.setDetails(details);

            // Add message to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(NOT_AUTHORIZED)) return handleNotAuthorized(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            redirectAttributes.addFlashAttribute("changeAccountRolesModel", changeAccountRolesModel);
        }

        return new RedirectView("/home/users/manage-user/" + userId, true);
    }
}