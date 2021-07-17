package com.project.askit.repository;

import com.project.askit.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, JpaSpecificationExecutor<Notification> {

    Page<Notification> findAllByUserIdOrUserIdIsNull(Integer userId,
                                                     Pageable pageable);

    Page<Notification> findAllByUserIdAndViewed(Integer userId,
                                                Integer viewed,
                                                Pageable pageable);

    Page<Notification> findAllByUserIdAndViewedAndUrlLike(Integer userId,
                                                          Integer viewed,
                                                          String url,
                                                          Pageable pageable);
}