package com.project.askit.rest;

import com.project.askit.entity.Notification;
import com.project.askit.repository.NotificationRepository;
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

@RestController
public class NotificationRestController {

    private final NotificationRepository notificationRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationRestController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/api/notifications")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam Integer userId,
                                  @RequestParam Integer viewed,
                                  @RequestParam String url) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) {
                pageSort = pageSort.descending();
            }

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Get page of objects
            Page<Notification> objectPage = notificationRepository.findAllByUserIdAndViewedAndUrlLike(userId, viewed, "%" + url + "%", paging);

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

    @GetMapping("/api/notifications/user")
    public Object findAllByUserId(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam Integer userId) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) {
                pageSort = pageSort.descending();
            }

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Get page of objects
            Page<Notification> objectPage = notificationRepository.findAllByUserIdOrUserIdIsNull(userId, paging);

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

    @GetMapping("/api/notifications/user-viewed")
    public Object findAllByUserIdAndViewed(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "5") Integer size,
                                           @RequestParam(defaultValue = "id") String sort,
                                           @RequestParam(defaultValue = "asc") String order,
                                           @RequestParam Integer userId,
                                           @RequestParam Integer viewed) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) {
                pageSort = pageSort.descending();
            }

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Get page of objects
            Page<Notification> objectPage = notificationRepository.findAllByUserIdAndViewed(userId, viewed, paging);

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

    @GetMapping("/api/notifications/{id}")
    public Object findById(@PathVariable Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @PostMapping("/api/notifications")
    public Object save(@RequestBody Notification object) {
        Notification savedObject;

        try {
            savedObject = notificationRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/notifications")
    public Object update(@RequestBody Notification object) {
        Notification updatedObject;

        try {
            updatedObject = notificationRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

}
