package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.model.TaskAuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskAuditEntryRepository extends JpaRepository<TaskAuditEntry,String> {

    @Modifying
    @Query(value = "DELETE FROM TaskAuditEntry t WHERE t.taskEntity.id = :taskId")
    void deleteAllByTaskId(@Param("taskId") String taskId);
}
