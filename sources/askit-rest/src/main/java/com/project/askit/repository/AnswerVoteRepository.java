package com.project.askit.repository;

import com.project.askit.entity.AnswerVote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Integer>, JpaSpecificationExecutor<AnswerVote> {
    AnswerVote findByAnswerIdAndUserId(int answerId,
                                       int userId);

    Page<AnswerVote> findAllByUserId(Integer userId,
                                     Pageable pageable);
}