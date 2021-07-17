package com.project.askit.rest;

import com.project.askit.entity.User;
import com.project.askit.model.UserActivity;
import com.project.askit.model.UserIdAndPostCount;
import com.project.askit.repository.UserRepository;
import com.project.askit.specification.SearchCriteria;
import com.project.askit.specification.UserSpecification;
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

import java.sql.Date;
import java.util.*;

@RestController
public class UserRestController {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/users")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email) {
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
            UserSpecification usernameSpecification = (username == null) ? null : new UserSpecification(new SearchCriteria("username", "=", username));
            UserSpecification emailSpecification = (email == null) ? null : new UserSpecification(new SearchCriteria("email", "=", email));

            // Create specification
            Specification<User> specification = Specification.where(usernameSpecification).and(emailSpecification);

            // Get page of objects
            Page<User> objectPage = userRepository.findAll(specification, paging);

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/login/{email}")
    public Object findByEmail(@PathVariable String email) {
        User user;

        try {
            user = userRepository.findByEmail(email);
            if (user == null) throw new Exception("Email doesn't exists");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }

        return user;
    }

    @GetMapping("/api/users/notifications")
    public Object findAllByUsernameOrEmail(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "5") Integer size,
                                           @RequestParam(defaultValue = "id") String sort,
                                           @RequestParam(defaultValue = "asc") String order,
                                           @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String email) {
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
            UserSpecification usernameSpecification = (username == null) ? null : new UserSpecification(new SearchCriteria("username", "=", username));
            UserSpecification emailSpecification = (email == null) ? null : new UserSpecification(new SearchCriteria("email", "=", email));

            // Create specification
            Specification<User> specification = Specification.where(usernameSpecification).or(emailSpecification);

            // Get page of objects
            Page<User> objectPage = userRepository.findAll(specification, paging);

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{id}/activity")
    public Object getActivityById(@PathVariable Integer id) {

        try {
            List<UserActivity> userActivityList = new ArrayList<>();

            List<Date> userActivityDates = userRepository.getActivityDatesById(id);
            List<Integer> userActivityQuestions = userRepository.getActivityQuestionsById(id);
            List<Integer> userActivityAnswers = userRepository.getActivityAnswersById(id);

            for (int i = 0; i < userActivityDates.size(); i++) {
                userActivityList.add(new UserActivity(userActivityDates.get(i), userActivityQuestions.get(i), userActivityAnswers.get(i)));
            }

            return new ResponseEntity<>(userActivityList, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @GetMapping("/api/users/{id}/statistics")
    public Object getStatistics(@PathVariable Integer id) {
        try {
            Integer postedAnswersCount = userRepository.getPostedAnswersCount(id);
            Integer postedQuestionsCount = userRepository.getPostedQuestionsCount(id);
            Integer rankingPoints = userRepository.getRankingPoints(id);

            Map<String, Object> response = new HashMap<>();
            response.put("postedAnswers", postedAnswersCount);
            response.put("postedQuestions", postedQuestionsCount);
            response.put("rankingPoints", rankingPoints);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @GetMapping("/api/users/most-active")
    public Object getMostActiveUsers(@RequestParam String date,
                                     @RequestParam(defaultValue = "5") Integer limit) {
        try {
            int[][] mostActiveUsers = userRepository.getMostActiveUsersByDate(date, limit);

            List<UserIdAndPostCount> response = new ArrayList<>();
            for (int[] row : mostActiveUsers) {
                response.add(new UserIdAndPostCount(row[0], row[1]));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @GetMapping("/api/users/{id}")
    public Object findById(@PathVariable Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @PostMapping("/api/users")
    public Object save(@RequestBody User object) {
        User savedObject;

        try {
            savedObject = userRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/users")
    public Object update(@RequestBody User object) {
        User updatedObject;

        try {
            updatedObject = userRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/users/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<User> deletedObject;

        try {
            deletedObject = userRepository.findById(id);
            userRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}