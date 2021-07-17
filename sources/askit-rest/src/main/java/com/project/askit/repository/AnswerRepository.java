package com.project.askit.repository;

import com.project.askit.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Integer>, JpaSpecificationExecutor<Answer> {

    @Query(value = "SELECT a.* " +
            "FROM answer a " +
            "LEFT JOIN answer_vote av " +
            "ON a.id = av.answer_id " +
            "LEFT JOIN question q " +
            "ON q.id = a.question_id " +
            "WHERE a.question_id = :questionId " +
            "AND a.approved = :approved " +
            "GROUP BY a.id " +
            "ORDER BY COALESCE(SUM(vote), 0)",
            countProjection = "a.id",
            nativeQuery = true)
    Page<Answer> findByVotes(@Param("questionId") Integer questionId,
                             @Param("approved") Integer approved,
                             Pageable pageable);

    @Query(value = "SELECT a.* " +
            "FROM answer a " +
            "LEFT JOIN answer_vote av " +
            "ON a.id = av.answer_id " +
            "LEFT JOIN question q " +
            "ON q.id = a.question_id " +
            "WHERE q.id = :questionId " +
            "AND a.approved = :approved " +
            "GROUP BY a.id " +
            "ORDER BY COALESCE(SUM(vote), 0) DESC",
            countProjection = "a.id",
            nativeQuery = true)
    Page<Answer> findByVotesDesc(@Param("questionId") Integer questionId,
                                 @Param("approved") Integer approved,
                                 Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(av.vote), 0) " +
            "FROM answer a, answer_vote av " +
            "WHERE a.id = av.answer_id " +
            "AND a.id = :id ",
            nativeQuery = true)
    Page<Integer> getVotesById(@Param("id") Integer id,
                               Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM answer " +
            "WHERE approved = 0 " +
            "AND created_date >= DATE_SUB(NOW(), INTERVAL :minutes MINUTE)",
            nativeQuery = true)
    int getUnreviewedAnswersCountByMinutes(@Param("minutes") Integer id);
}