package com.project.askit.repository;

import com.project.askit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    @Query(value = "SELECT the_date " +
            "FROM (SELECT CAST(q.created_date AS DATE) the_date, COUNT(q.id) answers, 0 questions " +
            "FROM question q " +
            "WHERE q.user_id = :id " +
            "GROUP BY the_date " +
            "UNION ALL " +
            "SELECT CAST(a.created_date AS DATE) the_date , 0, COUNT(a.id) " +
            "FROM answer a " +
            "WHERE a.user_id = :id " +
            "GROUP BY the_date) AS subquery " +
            "GROUP BY the_date",
            nativeQuery = true)
    List<Date> getActivityDatesById(@Param("id") Integer id);

    @Query(value = "SELECT MAX(answers) " +
            "FROM (SELECT CAST(q.created_date AS DATE) the_date, COUNT(q.id) answers, 0 questions " +
            "FROM question q " +
            "WHERE q.user_id = :id " +
            "GROUP BY the_date " +
            "UNION ALL " +
            "SELECT CAST(a.created_date AS DATE) the_date , 0, COUNT(a.id) " +
            "FROM answer a " +
            "WHERE a.user_id = :id " +
            "GROUP BY the_date) AS subquery " +
            "GROUP BY the_date",
            nativeQuery = true)
    List<Integer> getActivityAnswersById(@Param("id") Integer id);

    @Query(value = "SELECT MAX(questions) " +
            "FROM (SELECT CAST(q.created_date AS DATE) the_date, COUNT(q.id) answers, 0 questions " +
            "FROM question q " +
            "WHERE q.user_id = :id " +
            "GROUP BY the_date " +
            "UNION ALL " +
            "SELECT CAST(a.created_date AS DATE) the_date , 0, COUNT(a.id) " +
            "FROM answer a " +
            "WHERE a.user_id = :id " +
            "GROUP BY the_date) AS subquery " +
            "GROUP BY the_date",
            nativeQuery = true)
    List<Integer> getActivityQuestionsById(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) " +
            "FROM user u, answer a " +
            "WHERE u.id = a.user_id " +
            "AND u.id = :id " +
            "AND a.approved = 1 ",
            nativeQuery = true)
    Integer getPostedAnswersCount(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) " +
            "FROM user u, question q " +
            "WHERE u.id = q.user_id " +
            "AND u.id = :id " +
            "AND q.approved = 1 ",
            nativeQuery = true)
    Integer getPostedQuestionsCount(@Param("id") Integer id);

    @Query(value = "SELECT SUM(votes) points " +
            "FROM (SELECT COALESCE(SUM(CASE WHEN qv.vote > 0 THEN qv.vote * 10 ELSE qv.vote * 5 END), 0) AS votes " +
            "FROM question q, question_vote qv " +
            "WHERE q.id = qv.question_id " +
            "AND q.user_id = :id " +
            "AND q.approved = 1 " +
            "UNION ALL " +
            "SELECT COALESCE(SUM(CASE WHEN av.vote > 0 THEN av.vote * 10 ELSE av.vote * 5 END), 0) " +
            "FROM answer a, answer_vote av " +
            "WHERE a.id = av.answer_id " +
            "AND a.user_id = :id " +
            "AND a.approved = 1) AS subquery",
            nativeQuery = true)
    Integer getRankingPoints(@Param("id") Integer id);

    @Query(value = "SELECT user_id, (questions + answers) AS posts " +
            "FROM (SELECT user_id, count(*) AS questions, 0 AS answers " +
            "FROM question " +
            "WHERE created_date LIKE CONCAT(:date, '%') " +
            "GROUP BY user_id " +
            "UNION ALL " +
            "SELECT user_id, 0 AS questions, count(*) AS answers " +
            "FROM answer " +
            "WHERE created_date LIKE CONCAT(:date, '%') " +
            "GROUP BY user_id) AS subquery " +
            "GROUP BY user_id " +
            "ORDER BY posts DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    int[][] getMostActiveUsersByDate(
            @Param("date") String date,
            @Param("limit") Integer limit);
}