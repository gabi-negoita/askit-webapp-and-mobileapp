package com.project.askit.repository;

import com.project.askit.entity.QuestionVote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Integer>, JpaSpecificationExecutor<QuestionVote> {
    QuestionVote findByQuestionIdAndUserId(int questionId,
                                           int userId);

    Page<QuestionVote> findAllByUserId(Integer userId,
                                       Pageable pageable);
}