package com.project.askit.repository;

import com.project.askit.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer>, JpaSpecificationExecutor<Question> {

    @Query(value = "SELECT * " +
            "FROM question q, category c " +
            "WHERE q.category_id = c.id " +
            "AND c.title LIKE CONCAT('%', :title, '%') " +
            "AND q.created_date LIKE CONCAT('%', :createdDate, '%') " +
            "AND (q.subject REGEXP :search " +
            "OR q.html_text REGEXP :search) " +
            "AND q.approved = :approved",
            nativeQuery = true)
    Page<Question> findByQuery(@Param("title") String categoryTitle,
                               @Param("createdDate") String createdDate,
                               @Param("search") String search,
                               @Param("approved") Integer approved,
                               Pageable pageable);

    @Query(value = "SELECT q.* " +
            "FROM question q " +
            "LEFT JOIN question_vote qv " +
            "ON qv.question_id = q.id " +
            "LEFT JOIN category c " +
            "ON c.id = q.category_id " +
            "WHERE c.title LIKE CONCAT('%', :title, '%') " +
            "AND q.created_date LIKE CONCAT('%', :createdDate, '%') " +
            "AND (q.subject REGEXP :search " +
            "OR q.html_text REGEXP :search) " +
            "AND q.approved = :approved " +
            "GROUP BY q.id " +
            "ORDER BY COALESCE(SUM(vote), 0)",
            countProjection = "q.id",
            nativeQuery = true)
    Page<Question> findByVotes(@Param("title") String categoryTitle,
                               @Param("createdDate") String createdDate,
                               @Param("search") String search,
                               @Param("approved") Integer approved,
                               Pageable pageable);

    @Query(value = "SELECT q.* " +
            "FROM question q " +
            "LEFT JOIN question_vote qv " +
            "ON qv.question_id = q.id " +
            "LEFT JOIN category c " +
            "ON c.id = q.category_id " +
            "WHERE c.title LIKE CONCAT('%', :title, '%') " +
            "AND q.created_date LIKE CONCAT('%', :createdDate, '%') " +
            "AND (q.subject REGEXP :search " +
            "OR q.html_text REGEXP :search) " +
            "AND q.approved = :approved " +
            "GROUP BY q.id " +
            "ORDER BY COALESCE(SUM(vote), 0) DESC",
            countProjection = "q.id",
            nativeQuery = true)
    Page<Question> findByVotesDesc(@Param("title") String categoryTitle,
                                   @Param("createdDate") String createdDate,
                                   @Param("search") String search,
                                   @Param("approved") Integer approved,
                                   Pageable pageable);

    @Query(value = "SELECT q.* " +
            "FROM question q " +
            "LEFT JOIN answer a " +
            "ON a.question_id = q.id " +
            "AND a.approved = 1 " +
            "LEFT JOIN category c " +
            "ON c.id = q.category_id " +
            "WHERE c.title LIKE CONCAT('%', :title, '%') " +
            "AND q.created_date LIKE CONCAT('%', :createdDate, '%') " +
            "AND (q.subject REGEXP :search " +
            "OR q.html_text REGEXP :search) " +
            "AND q.approved = :approved " +
            "GROUP BY q.id " +
            "ORDER BY COUNT(a.question_id)",
            countProjection = "q.id",
            nativeQuery = true)
    Page<Question> findByApprovedAnswersCount(@Param("title") String categoryTitle,
                                              @Param("createdDate") String createdDate,
                                              @Param("search") String search,
                                              @Param("approved") Integer approved,
                                              Pageable pageable);

    @Query(value = "SELECT q.* " +
            "FROM question q " +
            "LEFT JOIN answer a " +
            "ON a.question_id = q.id " +
            "AND a.approved = 1 " +
            "LEFT JOIN category c " +
            "ON c.id = q.category_id " +
            "WHERE c.title LIKE CONCAT('%', :title, '%') " +
            "AND q.created_date LIKE CONCAT('%', :createdDate, '%') " +
            "AND (q.subject REGEXP :search " +
            "OR q.html_text REGEXP :search) " +
            "AND q.approved = :approved " +
            "GROUP BY q.id " +
            "ORDER BY COUNT(a.question_id) DESC",
            countProjection = "q.id",
            nativeQuery = true)
    Page<Question> findByApprovedAnswersCountDesc(@Param("title") String categoryTitle,
                                                  @Param("createdDate") String createdDate,
                                                  @Param("search") String search,
                                                  @Param("approved") Integer approved,
                                                  Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(qv.vote), 0) " +
            "FROM question q, question_vote qv " +
            "WHERE q.id = qv.question_id " +
            "AND q.id = :id",
            nativeQuery = true)
    Page<Integer> getVotesById(@Param("id") Integer id,
                               Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM question q, answer a " +
            "WHERE q.id = a.question_id " +
            "AND q.id = :id " +
            "AND a.approved = 1",
            nativeQuery = true)
    Page<Integer> getApprovedAnswersCountById(@Param("id") Integer id,
                                              Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM question " +
            "WHERE approved = 0 " +
            "AND created_date >= DATE_SUB(NOW(), INTERVAL :minutes MINUTE)",
            nativeQuery = true)
    int getUnreviewedQuestionsCountByMinutes(@Param("minutes") Integer id);
}