package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("dev")
public class InMemoryTaskRepository implements TaskRepository {

    private final HashMap<String, Task> taskRepo;

    public InMemoryTaskRepository() {
        taskRepo = new HashMap<>();
    }

    @Override
    public List<Task> findAll() {
        return taskRepo.values().stream().toList();
    }

    @Override
    public List<Task> findAll(int page, int size, Comparator comparator) {
        return taskRepo.values().stream().toList();
    }

    @Override
    public List<Task> findAll(TaskStatus status, int page, int size, Comparator comparator) {
        return taskRepo.values().stream().toList();
    }


    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(taskRepo.get(id));
    }

    @Override
    public Task save(Task task) {
        taskRepo.put(task.id(), task);
        return taskRepo.get(task.id());
    }

    @Override
    public void deleteById(String id) {
        taskRepo.remove(id);
    }
}
