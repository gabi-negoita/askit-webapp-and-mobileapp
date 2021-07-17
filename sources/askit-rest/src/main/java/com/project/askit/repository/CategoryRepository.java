package com.project.askit.repository;

import com.project.askit.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    Category findByTitle(String title);

    @Query(value = "SELECT COUNT(*) " +
            "FROM category c, question q " +
            "WHERE c.id = q.category_id " +
            "AND c.id = :id " +
            "AND q.id IN (SELECT DISTINCT question_id FROM answer) ",
            nativeQuery = true)
    Page<Integer> getAnsweredQuestions(@Param("id") Integer id,
                                       Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM category c, question q " +
            "WHERE c.id = q.category_id " +
            "AND c.id = :id " +
            "AND q.id NOT IN " +
            "(SELECT DISTINCT question_id FROM answer)",
            nativeQuery = true)
    Page<Integer> getUnansweredQuestions(@Param("id") Integer id,
                                         Pageable pageable);

    @Query(value = "SELECT category_id, count(*) AS posts " +
            "FROM question " +
            "WHERE created_date LIKE CONCAT(:date, '%') " +
            "GROUP BY category_id " +
            "ORDER BY posts DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    int[][] getMostUsedCategoriesByDate(
            @Param("date") String date,
            @Param("limit") Integer limit);

    @Query(value = "SELECT category_id, count(*) AS posts " +
            "FROM question " +
            "WHERE approved = 1 " +
            "GROUP BY category_id " +
            "ORDER BY posts DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    int[][] getMostQuestionCategories(@Param("limit") Integer limit);
}