package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.model.TaskEntity;
import io.java_core.taskmanagementapi.model.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {
    @Query(value = "SELECT * FROM TASKS", nativeQuery = true)
    List<TaskEntity> findAllByColumn(Pageable page);

    List<TaskEntity> findAllByStatus(TaskStatus status, Pageable pageable);

    void deleteById(String id);
}
