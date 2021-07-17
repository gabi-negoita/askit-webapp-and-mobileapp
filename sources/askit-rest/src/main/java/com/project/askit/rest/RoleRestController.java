package com.project.askit.rest;

import com.project.askit.entity.Role;
import com.project.askit.repository.RoleRepository;
import com.project.askit.specification.RoleSpecification;
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
public class RoleRestController {

    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RoleRestController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/api/roles")
    public Object findAllByFields(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "asc") String order,
                                  @RequestParam(required = false) String name) {
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
            RoleSpecification nameSpecification = (name == null) ? null : new RoleSpecification(new SearchCriteria("name", "=", name));

            // Create specification
            Specification<Role> specification = Specification.where(nameSpecification);

            // Get page of objects
            Page<Role> objectPage = roleRepository.findAll(specification, paging);

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

    @GetMapping("/api/roles/{id}")
    public Object findById(@PathVariable Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @PostMapping("/api/roles")
    public Object save(@RequestBody Role object) {
        Role savedObject;

        try {
            savedObject = roleRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/roles")
    public Object update(@RequestBody Role object) {
        Role updatedObject;

        try {
            updatedObject = roleRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/roles/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<Role> deletedObject;

        try {
            deletedObject = roleRepository.findById(id);
            roleRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}
