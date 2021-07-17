package com.project.askit.rest;

import com.project.askit.entity.Answer;
import com.project.askit.repository.AnswerRepository;
import com.project.askit.specification.AnswerSpecification;
import com.project.askit.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AnswerRestController {

    private final AnswerRepository answerRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnswerRestController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @GetMapping("/api/answers")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam(required = false) Integer questionId,
                                  @RequestParam(required = false) Integer approved,
                                  @RequestParam(required = false) Integer userId) {
        try {
            Pageable paging;
            Page<Answer> objectPage;

            if (sort.equals("votes")) {
                // Set paging
                paging = PageRequest.of(page, size);

                if (order.equals("desc")) {
                    objectPage = answerRepository.findByVotesDesc(questionId, approved, paging);
                } else {
                    objectPage = answerRepository.findByVotes(questionId, approved, paging);
                }
            } else {
                // Set sort
                Sort pageSort = Sort.by(sort);
                if (order.equals("desc")) {
                    pageSort = pageSort.descending();
                }

                // Set paging
                paging = PageRequest.of(page, size, pageSort);

                // Set query custom specification
                AnswerSpecification approvedSpecification = (approved == null) ? null : new AnswerSpecification(new SearchCriteria("approved", "=", approved));
                AnswerSpecification userIdSpecification = (userId == null) ? null : new AnswerSpecification(new SearchCriteria("userId", "=", userId));
                AnswerSpecification questionIdSpecification = (questionId == null) ? null : new AnswerSpecification(new SearchCriteria("questionId", "=", questionId));

                // Create specification
                Specification<Answer> specification = Specification.where(approvedSpecification).and(userIdSpecification).and(questionIdSpecification);

                // Get page of objects
                objectPage = answerRepository.findAll(specification, paging);
            }

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("content", objectPage.getContent());
            response.put("currentPage", objectPage.getNumber());
            response.put("totalItems", objectPage.getTotalElements());
            response.put("totalPages", objectPage.getTotalPages());

            // Return response
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @GetMapping("/api/answers/{id}/statistics")
    public Object getStatistics(@PathVariable Integer id) {
        try {
            Optional<Answer> answer = answerRepository.findById(id);
            if (answer.isEmpty()) throw new Exception("Invalid id");

            Page<Integer> votes = answerRepository.getVotesById(id, null);

            Map<String, Object> response = new HashMap<>();
            response.put("votes", votes.getContent().get(0));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.MULTI_STATUS, "Provide a valid id");
        }
    }

    @GetMapping("/api/answers/unreviewed")
    public Object getUnreviewedAnswersCountByMinutes(@RequestParam(defaultValue = "5") Integer minutes) {
        try {
            int count = answerRepository.getUnreviewedAnswersCountByMinutes(minutes);

            Map<String, Object> response = new HashMap<>();
            response.put("count", count);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }
    }

    @GetMapping("/api/answers/{id}")
    public Object findById(@PathVariable Integer id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @PostMapping("/api/answers")
    public Object save(@RequestBody Answer object) {
        Answer savedObject;

        try {
            savedObject = answerRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/answers")
    public Object update(@RequestBody Answer object) {
        Answer updatedObject;

        try {
            updatedObject = answerRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/answers/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<Answer> deletedObject;

        try {
            deletedObject = answerRepository.findById(id);
            answerRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}