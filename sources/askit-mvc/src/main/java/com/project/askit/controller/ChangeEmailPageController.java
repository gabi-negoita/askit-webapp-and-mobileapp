package com.project.askit.controller;

import com.project.askit.entity.EventLog;
import com.project.askit.entity.User;
import com.project.askit.exeption.AlreadyChangedEmailException;
import com.project.askit.exeption.ExpiredLinkExeption;
import com.project.askit.exeption.InvalidLinkExeption;
import com.project.askit.model.EmailModel;
import com.project.askit.model.MessageModel;
import com.project.askit.rest.api.EventLogRestApi;
import com.project.askit.rest.api.UserRestApi;
import com.project.askit.util.Crypto;
import com.project.askit.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChangeEmailPageController extends AbstractPageController {

    private final UserRestApi userRestApi;
    private final EventLogRestApi eventLogRestApi;

    private final Logger logger;

    @Autowired
    public ChangeEmailPageController(UserRestApi userRestApi,
                                     EventLogRestApi eventLogRestApi) {
        super();

        this.userRestApi = userRestApi;
        this.eventLogRestApi = eventLogRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/change-email")
    public Object showChangeEmailPage(@RequestParam String key,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            String decryptedKey = Crypto.decrypt(key, Crypto.KEY);
            if (decryptedKey == null || decryptedKey.isBlank()) throw new InvalidLinkExeption();

            String[] keyValues = decryptedKey.split(EmailModel.DELIMITER);
            if (keyValues.length != 3) throw new InvalidLinkExeption();

            int userId = Integer.parseInt(keyValues[0]);
            String newEmail = keyValues[1];
            long time = Long.parseLong(keyValues[2]);

            // Check if key is expired (more than 24h have passed)
            if (System.currentTimeMillis() - time > 86400000) throw new ExpiredLinkExeption();

            // Get user
            User user = userRestApi.findById(userId);
            if (user == null) throw new Exception(REST_API_CALL_FAILED);

            String oldEmail = user.getEmail();
            if (newEmail.equals(oldEmail)) throw new AlreadyChangedEmailException();

            user.setEmail(newEmail);

            User updatedUser = userRestApi.update(user);
            if (updatedUser == null) throw new Exception(REST_API_CALL_FAILED);

            // Update session info
            User sessionUser = getSessionUser(request);
            if (sessionUser != null) sessionUser.setEmail(updatedUser.getEmail());

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.UPDATE);
            log.setInfo("Changed email from \"" + oldEmail + "\" to \"" + updatedUser.getEmail() + "\"");
            log.setUserId(updatedUser.getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");
            messageModel.setType(MessageModel.TYPE_SUCCESS);

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Your email has been successfully changed"));
            messageModel.setDetails(details);

            model.addAttribute("messageModel", messageModel);

        } catch (InvalidLinkExeption e) {
            MessageModel messageModel = e.getMessageModel();
            model.addAttribute("messageModel", messageModel);
        } catch (AlreadyChangedEmailException e) {
            MessageModel messageModel = e.getMessageModel();
            model.addAttribute("messageModel", messageModel);
        } catch (ExpiredLinkExeption e) {
            MessageModel messageModel = e.getMessageModel();
            model.addAttribute("messageModel", messageModel);
        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "confirm-email";
    }
}