package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.exeption.*;
import com.project.askit.model.EmailModel;
import com.project.askit.model.MessageModel;
import com.project.askit.model.ResetPasswordModel;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Crypto;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResetPasswordPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public ResetPasswordPageController(UserRestApi userRestApi,
                                       EventLogRestApi eventLogRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/reset-password")
    public Object showResetPasswordPage(@RequestParam String key,
                                        Model model) {
        // Add data to model
        ResetPasswordModel resetPasswordModel = new ResetPasswordModel();
        resetPasswordModel.setKey(key);
        model.addAttribute("resetPasswordModel", resetPasswordModel);

        return "reset-password";
    }

    @RequestMapping("/reset-password/process-form")
    public Object processResetPasswordForm(@Valid @ModelAttribute("resetPasswordModel") ResetPasswordModel resetPasswordModel,
                                           BindingResult result,
                                           RedirectAttributes redirectAttributes) {
        try {
            // Check data invalidity
            if (result.hasErrors()) throw new InvalidFieldsException(result);

            if (!resetPasswordModel.getPassword().equals(resetPasswordModel.getConfirmedPassword())) throw new PasswordsNotMatchException();

            String decryptedKey = Crypto.decrypt(resetPasswordModel.getKey(), Crypto.KEY);
            if (decryptedKey == null || decryptedKey.isBlank()) throw new InvalidLinkExeption();

            String[] keyValues = decryptedKey.split(EmailModel.DELIMITER);
            if (keyValues.length != 2) throw new InvalidLinkExeption();

            int userId = Integer.parseInt(keyValues[0]);
            long time = Long.parseLong(keyValues[1]);

            // Check if key is expired (more than 24h or 86400000 ms has passed)
            if (System.currentTimeMillis() - time > 86400000) throw new ExpiredLinkExeption();

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            // Check if the link has been already used
            if (user.getPassword().equals("{noop}" + resetPasswordModel.getPassword())) throw new AlreadyChangedPasswordException();

            // Change password
            user.setPassword("{noop}" + resetPasswordModel.getPassword());

            // Save changes
            User updatedUser = userRestApi.update(user);
            if (updatedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed password of user with id " + updatedUser.getId());
            log.setUserId(updatedUser.getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Your password has been successfully changed"));

            messageModel.setDetails(details);

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

            return new RedirectView("/login", true);

        } catch (InvalidFieldsException e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        } catch (PasswordsNotMatchException e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        } catch (InvalidLinkExeption e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        } catch (ExpiredLinkExeption e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        } catch (AlreadyChangedPasswordException e) {
            MessageModel messageModel = e.getMessageModel();
            redirectAttributes.addFlashAttribute("messageModel", messageModel);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return new RedirectView("/reset-password?key=" + URLEncoder.encode(resetPasswordModel.getKey(), StandardCharsets.UTF_8), true);
    }
}