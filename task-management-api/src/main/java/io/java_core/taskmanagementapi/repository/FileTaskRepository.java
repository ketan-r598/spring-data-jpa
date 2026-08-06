package io.java_core.taskmanagementapi.repository;

import io.java_core.taskmanagementapi.configuration.TaskProperties;
import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.*;

@Repository
@Profile("prod")
public class FileTaskRepository implements TaskRepository {

    private final TaskProperties taskProperties;
    private final ObjectMapper mapper;
    private Map<String, Task> taskMap;

    public FileTaskRepository(ObjectMapper mapper, TaskProperties taskProperties) {
        this.mapper = mapper;
        this.taskProperties = taskProperties;
    }

    @Override
    public List<Task> findAll() {
        return taskMap.values().stream().toList();
    }

    @Override
    public List<Task> findAll(int page, int size, Comparator comparator) {
        if(comparator == null) {
            return taskMap.values()
                    .stream()
                    .skip(page * size)
                    .limit(size)
                    .toList();
        }
        return taskMap.values()
                .stream()
                .sorted(comparator)
                .skip(page * size)
                .limit(size)
                .toList();
    }

    @Override
    public List<Task> findAll(TaskStatus status, int page, int size, Comparator comparator) {
        if(comparator == null) {
            return taskMap.values()
                    .stream()
                    .filter(t -> t.status() == status)
                    .skip(page * size)
                    .limit(size)
                    .toList();
        }
        return taskMap.values()
                .stream()
                .filter(t -> t.status() == status)
                .sorted(comparator)
                .skip(page * size)
                .limit(size)
                .toList();
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    @Override
    public Task save(Task task) {
        taskMap.put(task.id(), task);
        return taskMap.get(task.id());
    }

    @Override
    public void deleteById(String id) {
        taskMap.remove(id);
    }

    @PostConstruct
    public void setup() {
        if (taskProperties.getStorage().getFilePath() == null
                || taskProperties.getStorage().getFilePath().isBlank()) {
            throw new IllegalStateException("File Path is not valid...");
        }

        String filePath = taskProperties.getStorage().getFilePath();
        taskMap = new HashMap<>();

        List<Task> tasks = mapper.readValue(
                Path.of(filePath).toFile(),
                new TypeReference<List<Task>>() {
                }
        );
        tasks.forEach(task -> taskMap.put(task.id(), task));
    }

    @PreDestroy
    public void destroy() {
        String filePath = taskProperties.getStorage().getFilePath();
        System.out.println("Pre destroy is running...");

        if (filePath == null || filePath.isBlank()) {
            System.err.println("No valid filepath found...");
            return;
        }
        mapper.writeValue(Path.of(filePath).toFile(), taskMap.values().stream().toList());
    }
}