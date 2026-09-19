package io.java_core.taskmanagementapi.service;

import io.java_core.taskmanagementapi.configuration.TaskProperties;
import io.java_core.taskmanagementapi.event.TaskCompletedEvent;
import io.java_core.taskmanagementapi.event.TaskCreatedEvent;
import io.java_core.taskmanagementapi.exception.TaskNotFoundException;
import io.java_core.taskmanagementapi.mapper.TaskMapper;
import io.java_core.taskmanagementapi.model.AuditEntry;
import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskEntity;
import io.java_core.taskmanagementapi.model.TaskStatus;
import io.java_core.taskmanagementapi.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final TaskProperties taskProperties;

    private final ObjectFactory<AuditEntry> auditEntry;
    private final TaskMapper taskMapper;


    public TaskService(TaskRepository taskRepo, ApplicationEventPublisher eventPublisher, ObjectFactory<AuditEntry> auditEntry, TaskProperties taskProperties, TaskMapper taskMapper) {
        this.taskRepo = taskRepo;
        this.eventPublisher = eventPublisher;
        this.auditEntry = auditEntry;
        this.taskProperties = taskProperties;
        this.taskMapper = taskMapper;
    }

    public Task createTask(String title, String description) throws IllegalArgumentException {

        if (taskRepo.findAll().size() >= taskProperties.getLimits().getMaxTasks()) {
            throw new IllegalArgumentException("Task Limit Exceeded...");
        }

        TaskEntity newTaskEntity = new TaskEntity(title, description, TaskStatus.PENDING);

        Task savedTask = taskMapper.toDomain(taskRepo.save(newTaskEntity));

        eventPublisher.publishEvent(new TaskCreatedEvent(this, savedTask));

        log.info(" [AUDIT] | Task Created | {} | [ {} ]", auditEntry.getObject(), savedTask.id());

        return savedTask;
    }

    public Task completeTask(String id) {
        Task oldTask = taskRepo.findById(id).map(taskMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id..."));

        taskRepo.deleteById(id);
        Task newTask = new Task(oldTask.id(), oldTask.title(), oldTask.description(), TaskStatus.COMPLETED);
        newTask = taskMapper.toDomain(taskRepo.save(taskMapper.toEntity(newTask)));

        eventPublisher.publishEvent(new TaskCompletedEvent(this, newTask));

        log.info(" [AUDIT] | Task Completed | {} | [ {} ]", auditEntry.getObject(), newTask.id());

        return newTask;
    }


    public List<Task> getAllTasks(Pageable pageable) {
        return taskRepo.findAllByColumn(pageable).stream().map(taskMapper::toDomain).toList();
    }

    public List<Task> getAllTasks(TaskStatus status, Pageable pageable) {
        return taskRepo.findAllByStatus(status, pageable).stream().map(taskMapper::toDomain).toList();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepo.findById(id).stream().map(taskMapper::toDomain).findAny();
    }

    public void deleteTask(String id) {
        taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found", id));
        taskRepo.deleteById(id);
    }

    public Task updateTask(String id, String title, String description) {
        TaskEntity t = taskRepo.getReferenceById(id);

        if (t == null) throw new TaskNotFoundException("Task Not Found", id);

        if (description != null && !description.isBlank()) t.setDescription(description);
        t.setTitle(title);

        return taskMapper.toDomain(taskRepo.save(t));

    }
}