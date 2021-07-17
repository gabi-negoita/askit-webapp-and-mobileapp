package com.project.askit.rest;

import com.project.askit.entity.Question;
import com.project.askit.repository.QuestionRepository;
import com.project.askit.specification.QuestionSpecification;
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
public class QuestionRestController {

    private final QuestionRepository questionRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public QuestionRestController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    private String getRegex(String string) {
        return string
                .replaceAll("[^a-zA-Z\\d]", " ")
                .trim()
                .replaceAll("\\s+", "|")
                .toLowerCase();
    }

    @GetMapping("/api/questions")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam(required = false) String subject,
                                  @RequestParam(required = false) String htmlText,
                                  @RequestParam(required = false) Integer categoryId,
                                  @RequestParam(required = false) Integer userId,
                                  @RequestParam(required = false) Integer approved) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) {
                pageSort = pageSort.descending();
            }

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Set query custom specification
            // TODO: generate search string by subject and htmlText

//            QuestionSpecification createdDateSpecification = (createdDate == null) ? null : new AnswerSpecification(new SearchCriteria("createdDate", "=", createdDate));
            QuestionSpecification subjectSpecification = (htmlText == null) ? null : new QuestionSpecification(new SearchCriteria("subject", "=", subject));
            QuestionSpecification htmlTextSpecification = (htmlText == null) ? null : new QuestionSpecification(new SearchCriteria("htmlText", "=", htmlText));
            QuestionSpecification categoryIdSpecification = (categoryId == null) ? null : new QuestionSpecification(new SearchCriteria("categoryId", "=", categoryId));
            QuestionSpecification userIdSpecification = (userId == null) ? null : new QuestionSpecification(new SearchCriteria("userId", "=", userId));
            QuestionSpecification approvedSpecification = (approved == null) ? null : new QuestionSpecification(new SearchCriteria("approved", "=", approved));

            // Create specification
            Specification<Question> specification = Specification.where(subjectSpecification).and(htmlTextSpecification).and(categoryIdSpecification).and(userIdSpecification).and(approvedSpecification);

            // Get page of objects
            Page<Question> objectPage = questionRepository.findAll(specification, paging);

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

    @GetMapping("/api/questions/query")
    public Object findByQuery(@RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer size,
                              @RequestParam(defaultValue = "id") String sortBy,
                              @RequestParam(defaultValue = "asc") String order,
                              @RequestParam(defaultValue = "") String categoryTitle,
                              @RequestParam(defaultValue = "") String createdDate,
                              @RequestParam(defaultValue = "|") String search,
                              @RequestParam(defaultValue = "1") Integer approved) {
        try {
            // TODO: refactor this
            // Create words regexp
            String regexSearch = getRegex(search);

            Pageable paging;
            Page<Question> objectPage;
            switch (sortBy) {
                case "votes": {
                    paging = PageRequest.of(page, size);
                    if (order.equals("desc")) {
                        objectPage = questionRepository.findByVotesDesc(categoryTitle, createdDate, regexSearch, approved, paging);
                    } else {
                        objectPage = questionRepository.findByVotes(categoryTitle, createdDate, regexSearch, approved, paging);
                    }
                    break;
                }
                case "answers": {
                    paging = PageRequest.of(page, size);
                    if (order.equals("desc")) {
                        objectPage = questionRepository.findByApprovedAnswersCountDesc(categoryTitle, createdDate, regexSearch, approved, paging);
                    } else {
                        objectPage = questionRepository.findByApprovedAnswersCount(categoryTitle, createdDate, regexSearch, approved, paging);
                    }
                    break;
                }
                default: {
                    if (order.equals("desc")) {
                        paging = PageRequest.of(page, size, Sort.by(sortBy).descending());
                    } else {
                        paging = PageRequest.of(page, size, Sort.by(sortBy));
                    }
                    objectPage = questionRepository.findByQuery(categoryTitle, createdDate, regexSearch, approved, paging);
                    break;
                }
            }

            Map<String, Object> response = new HashMap<>();

            response.put("content", objectPage.getContent());
            response.put("currentPage", objectPage.getNumber());
            response.put("totalItems", objectPage.getTotalElements());
            response.put("totalPages", objectPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/questions/{id}/statistics")
    public Object getStatistics(@PathVariable Integer id) {

        try {
            Page<Integer> votes = questionRepository.getVotesById(id, null);
            Page<Integer> answers = questionRepository.getApprovedAnswersCountById(id, null);

            Map<String, Object> response = new HashMap<>();
            response.put("votes", votes.getContent().get(0));
            response.put("answers", answers.getContent().get(0));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }
    }

    @GetMapping("/api/questions/unreviewed")
    public Object getUnreviewedQuestionsCountByMinutes(@RequestParam(defaultValue = "5") Integer minutes) {
        try {
            int count = questionRepository.getUnreviewedQuestionsCountByMinutes(minutes);

            Map<String, Object> response = new HashMap<>();
            response.put("count", count);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }
    }

    @GetMapping("/api/questions/{id}")
    public Object findById(@PathVariable Integer id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @PostMapping("/api/questions")
    public Object save(@RequestBody Question object) {
        Question savedObject;

        try {
            savedObject = questionRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/questions")
    public Object update(@RequestBody Question object) {
        Question updatedObject;

        try {
            updatedObject = questionRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/questions/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<Question> deletedObject;

        try {
            deletedObject = questionRepository.findById(id);
            questionRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}
