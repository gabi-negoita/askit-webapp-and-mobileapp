package com.project.askit.rest;

import com.project.askit.entity.AnswerVote;
import com.project.askit.repository.AnswerVoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AnswerVoteRestController {

    private final AnswerVoteRepository answerVoteRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnswerVoteRestController(AnswerVoteRepository answerVoteRepository) {
        this.answerVoteRepository = answerVoteRepository;
    }

    @GetMapping("/api/answer-votes")
    public Object findByAnswerIdAndUserId(@RequestParam int answerId,
                                          @RequestParam int userId) {
        AnswerVote answerVote;
        try {
            answerVote = answerVoteRepository.findByAnswerIdAndUserId(answerId, userId);
            if (answerVote == null) throw new Exception("Not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }

        return answerVote;
    }

    @GetMapping("/api/answer-votes/user/{userId}")
    public Object findAllByUserId(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @PathVariable Integer userId) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) pageSort = pageSort.descending();

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Get page of objects
            Page<AnswerVote> objectPage = answerVoteRepository.findAllByUserId(userId, paging);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("content", objectPage.getContent());
            response.put("currentPage", objectPage.getNumber());
            response.put("totalItems", objectPage.getTotalElements());
            response.put("totalPages", objectPage.getTotalPages());

            // Return response
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @PostMapping("/api/answer-votes")
    public Object save(@RequestBody AnswerVote object) {
        AnswerVote savedObject;

        try {
            savedObject = answerVoteRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/answer-votes")
    public Object update(@RequestBody AnswerVote object) {
        AnswerVote updatedObject;

        try {
            updatedObject = answerVoteRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/answer-votes/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<AnswerVote> deletedObject;

        try {
            deletedObject = answerVoteRepository.findById(id);
            answerVoteRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}
