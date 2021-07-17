package com.project.askit.controller;

import com.project.askit.entity.*;
import com.project.askit.model.MessageModel;
import com.project.askit.model.PostQuestionModel;
import com.project.askit.rest.api.*;
import com.project.askit.util.Crypto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AskQuestionPageController extends AbstractPageController{

    private final QuestionRestApi questionRestApi;
    private final CategoryRestApi categoryRestApi;
    private final QuestionAttachmentRestApi questionAttachmentRestApi;
    private final EventLogRestApi eventLogRestApi;
    private final FileUploadRestApi fileUploadRestApi;

    private final Logger logger;

    @Autowired
    public AskQuestionPageController(QuestionRestApi questionRestApi,
                                     CategoryRestApi categoryRestApi,
                                     QuestionAttachmentRestApi questionAttachmentRestApi,
                                     EventLogRestApi eventLogRestApi,
                                     FileUploadRestApi fileUploadRestApi) {
        super();

        this.questionRestApi = questionRestApi;
        this.categoryRestApi = categoryRestApi;
        this.questionAttachmentRestApi = questionAttachmentRestApi;
        this.eventLogRestApi = eventLogRestApi;
        this.fileUploadRestApi = fileUploadRestApi;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/home/questions/ask")
    public Object showAskQuestionPage(HttpServletRequest request,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Get categories
            Wrapper<Category> categoryWrapper = categoryRestApi.findAll(null, Integer.MAX_VALUE, "title", null, null);
            if (categoryWrapper == null) throw new Exception(REST_API_CALL_FAILED);

            // Add data to model
            if (model.getAttribute("postQuestionModel") == null) model.addAttribute("postQuestionModel", new PostQuestionModel());
            model.addAttribute("categories", categoryWrapper.getContent());

            // Set session image locations
            List<String> locations = (List<String>) request.getSession().getAttribute("locations");
            if (locations == null) locations = new ArrayList<>();
            request.getSession().setAttribute("locations", locations);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);
        }

        return "ask-question";
    }

    @RequestMapping("/home/questions/post")
    public Object postQuestion(HttpServletRequest request,
                               @Valid @ModelAttribute("postQuestionModel") PostQuestionModel postQuestionModel,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        try {
            User sessionUser = getSessionUser(request);

            // Check if user is logged in
            if (!isLoggedIn(request)) throw new Exception(NOT_LOGGED_IN);

            // Check fields invalidity
            if (result.hasErrors()) throw new Exception(INVALID_FIELDS);

            // Set approved by role
            int approved = 0;
            for (Role role : sessionUser.getRoles()){
                if (role.getName().equalsIgnoreCase("teacher")){
                    approved = 1;
                    break;
                }
            }

            // Get category
            Category category = categoryRestApi.findByTitle(postQuestionModel.getCategory());
            if (category == null) throw new Exception(REST_API_CALL_FAILED);

            // Create question
            Question questionToSave = new Question(
                    postQuestionModel.getSubject(),
                    postQuestionModel.getBody(),
                    new Timestamp(System.currentTimeMillis()),
                    category.getId(),
                    sessionUser.getId(),
                    approved);

            // Save question
            Question savedQuestion = questionRestApi.save(questionToSave);
            if (savedQuestion == null) throw new Exception("Rest API call failed");

            // Log operation
            EventLog log = new EventLog();
            log.setAction(EventLog.INSERT);
            log.setInfo("Posted question with id " + savedQuestion.getId());
            log.setUserId(sessionUser.getId());
            EventLog savedLog = eventLogRestApi.save(log);
            if (savedLog == null) throw new Exception(REST_API_CALL_FAILED);

            // Store locations
            List<String> locations = (List<String>) request.getSession().getAttribute("locations");
            if (locations != null) {
                for (String location : locations) {
                    QuestionAttachment questionAttachment = new QuestionAttachment(
                            location,
                            savedQuestion.getId());
                    QuestionAttachment savedQuestionAttachment = questionAttachmentRestApi.save(questionAttachment);
                    if (savedQuestionAttachment == null) throw new Exception(REST_API_CALL_FAILED);
                }

                request.getSession().removeAttribute("locations");
            }

            // Create message
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage("Success!");

            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>(null, "Your questions has been successfully submitted"));

            messageModel.setType(MessageModel.TYPE_SUCCESS);
            messageModel.setDetails(details);

            // Add extra details
            if (approved == 0) details.add(new Pair<>("Note", "Your question will become public as soon as a reviewer approves it"));

            // Add data to redirect view
            redirectAttributes.addFlashAttribute("messageModel", messageModel);

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(NOT_LOGGED_IN)) return handleNotLoggedIn(redirectAttributes);
            else if (error.equals(INVALID_FIELDS)) handleInvalidFields(redirectAttributes, result);
            else if (error.equals(REST_API_CALL_FAILED)) return handleRestApiCallFailed(redirectAttributes);
            else return handleSomethingWentWrong(redirectAttributes);

            // Add model to redirect view
            redirectAttributes.addFlashAttribute("postQuestionModel", postQuestionModel);
        }

        return new RedirectView("/home/questions/ask", true);
    }

    @RequestMapping("/upload-file")
    @ResponseBody
    public Object uploadFile(@RequestParam("file") MultipartFile file,
                             HttpServletRequest request) {
        Map<String, String> result;
        try {
            if (file == null) throw new Exception("No file content");

            // Generate name
            String sha = Crypto.getShaFromInputStream(file.getInputStream());
            if (sha == null) throw new Exception("SHA could not be generated");
            String fileName = sha + "_" + System.currentTimeMillis() + ".png";

            // Upload image
            result = fileUploadRestApi.uploadFile(file, fileName);
            if (result == null) throw new Exception(REST_API_CALL_FAILED);

            // Store path in session
            List<String> locations = (List<String>) request.getSession().getAttribute("locations");
            locations.add(result.get("location"));

        } catch (Exception e) {
            String error = e.getMessage();
            logger.error(error);
            e.printStackTrace();

            if (error.equals(REST_API_CALL_FAILED)) return new ResponseEntity<>(REST_API_CALL_FAILED, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

}
