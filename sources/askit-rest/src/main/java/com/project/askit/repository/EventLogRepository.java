package com.project.askit.repository;

import com.project.askit.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventLogRepository extends JpaRepository<EventLog, Integer>, JpaSpecificationExecutor<EventLog> {
}