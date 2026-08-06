package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    List<Task> findAll(int page, int size, Comparator comparator);
    List<Task> findAll(TaskStatus status, int page, int size, Comparator comparator);
    Optional<Task> findById(String id);
    Task save(Task task);
    void deleteById(String id);
}
