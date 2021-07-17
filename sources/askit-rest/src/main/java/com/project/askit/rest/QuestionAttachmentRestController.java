package com.project.askit.rest;

import com.project.askit.entity.QuestionAttachment;
import com.project.askit.repository.QuestionAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class QuestionAttachmentRestController {

    private final QuestionAttachmentRepository questionAttachmentRepository;

    @Autowired
    public QuestionAttachmentRestController(QuestionAttachmentRepository questionAttachmentRepository) {
        this.questionAttachmentRepository = questionAttachmentRepository;
    }

    @PostMapping("/api/question-attachments")
    public Object save(@RequestBody QuestionAttachment object) {
        QuestionAttachment savedObject;

        try {
            savedObject = questionAttachmentRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }
}