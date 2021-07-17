package com.project.askit.rest;

import com.project.askit.entity.EventLog;
import com.project.askit.repository.EventLogRepository;
import com.project.askit.specification.EventLogSpecification;
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

@RestController
public class EventLogRestController {

    private final EventLogRepository eventLogRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventLogRestController(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    @GetMapping("/api/event-logs")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam(required = false) String action,
                                  @RequestParam(required = false) String info,
                                  @RequestParam(required = false) Integer userId) {
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
            EventLogSpecification actionSpecification = (action == null) ? null : new EventLogSpecification(new SearchCriteria("action", "=", action));
            EventLogSpecification infoSpecification = (info == null) ? null : new EventLogSpecification(new SearchCriteria("info", "=", info));
            EventLogSpecification userIdSpecification = (userId == null) ? null : new EventLogSpecification(new SearchCriteria("userId", "=", userId));

            // Create specification
            Specification<EventLog> specification = Specification.where(actionSpecification).and(infoSpecification).and(userIdSpecification);

            // Get page of objects
            Page<EventLog> objectPage = eventLogRepository.findAll(specification, paging);

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

    @PostMapping("/api/event-logs")
    public Object save(@RequestBody EventLog object) {
        EventLog savedObject;

        try {
            savedObject = eventLogRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }
}