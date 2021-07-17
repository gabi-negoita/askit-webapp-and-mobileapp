package com.project.askit.rest;

import com.project.askit.entity.AnswerAttachment;
import com.project.askit.repository.AnswerAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AnswerAttachmentRestController {

    private final AnswerAttachmentRepository answerAttachmentRepository;

    @Autowired
    public AnswerAttachmentRestController(AnswerAttachmentRepository answerAttachmentRepository) {
        this.answerAttachmentRepository = answerAttachmentRepository;
    }

    @PostMapping("/api/answer-attachments")
    public Object save(@RequestBody AnswerAttachment object) {
        AnswerAttachment savedObject;

        try {
            savedObject = answerAttachmentRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }
}
