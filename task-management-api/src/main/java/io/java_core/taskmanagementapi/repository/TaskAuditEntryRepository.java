package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.model.TaskAuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAuditEntryRepository extends JpaRepository<TaskAuditEntry,String> {
}
