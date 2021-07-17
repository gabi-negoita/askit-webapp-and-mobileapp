package com.project.askit.rest;

import com.project.askit.entity.Category;
import com.project.askit.model.CategoryIdAndPosts;
import com.project.askit.repository.CategoryRepository;
import com.project.askit.specification.CategorySpecification;
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

import java.util.*;

@RestController
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/categories")
    public Object findAllByTitle(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "5") Integer size,
                                 @RequestParam(defaultValue = "id") String sort,
                                 @RequestParam(defaultValue = "asc") String order,
                                 @RequestParam(required = false) String title) {
        try {
            Pageable paging;

            // Set sort
            Sort pageSort = Sort.by(sort);
            if (order.equals("desc")) pageSort = pageSort.descending();

            // Set page
            paging = PageRequest.of(page, size, pageSort);

            // Create specification
            CategorySpecification titleSpecification = (title == null) ? null : new CategorySpecification(new SearchCriteria("title", "=", title));

            Specification<Category> specification = Specification.where(titleSpecification);

            // Get page of objects
            Page<Category> objectPage = categoryRepository.findAll(specification, paging);

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

    @GetMapping("/api/categories/title/{title}")
    public Object findByTitle(@PathVariable String title) {
        Category category;
        try {
            category = categoryRepository.findByTitle(title);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }

        return category;
    }

    @GetMapping("/api/categories/{id}")
    public Object findById(@PathVariable Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id"));
    }

    @GetMapping("/api/categories/{id}/statistics")
    public ResponseEntity<Object> getStatistics(@PathVariable Integer id) {
        try {
            Page<Integer> answeredQuestions = categoryRepository.getAnsweredQuestions(id, null);
            Page<Integer> unansweredQuestions = categoryRepository.getUnansweredQuestions(id, null);

            Map<String, Object> response = new HashMap<>();
            response.put("answeredQuestions", answeredQuestions.getContent().get(0));
            response.put("unansweredQuestions", unansweredQuestions.getContent().get(0));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }
    }

    @GetMapping("/api/categories/most-used")
    public Object getMostUsedCategories(@RequestParam String date,
                                        @RequestParam(defaultValue = "5") Integer limit) {
        try {
            int[][] mostUsedCategories = categoryRepository.getMostUsedCategoriesByDate(date, limit);

            List<CategoryIdAndPosts> response = new ArrayList<>();
            for (int[] row : mostUsedCategories) {
                response.add(new CategoryIdAndPosts(row[0], row[1]));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @GetMapping("/api/categories/most-questions")
    public Object getMostQuestionsCategories(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            int[][] mostQuestionsCategories = categoryRepository.getMostQuestionCategories(limit);

            List<CategoryIdAndPosts> response = new ArrayList<>();
            for (int[] row : mostQuestionsCategories) {
                response.add(new CategoryIdAndPosts(row[0], row[1]));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide valid parameters");
        }
    }

    @PostMapping("/api/categories")
    public Object save(@RequestBody Category object) {
        Category savedObject;

        try {
            savedObject = categoryRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return savedObject;
    }

    @PutMapping("/api/categories")
    public Object update(@RequestBody Category object) {
        Category updatedObject;

        try {
            updatedObject = categoryRepository.save(object);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid entity");
        }

        return updatedObject;
    }

    @DeleteMapping("/api/categories/{id}")
    public Object deleteById(@PathVariable Integer id) {
        Optional<Category> deletedObject;

        try {
            deletedObject = categoryRepository.findById(id);
            categoryRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid id");
        }

        return deletedObject;
    }
}
