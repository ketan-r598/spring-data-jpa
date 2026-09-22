package io.java_core.taskmanagementapi.service;

import io.java_core.taskmanagementapi.configuration.TaskProperties;
import io.java_core.taskmanagementapi.event.TaskCompletedEvent;
import io.java_core.taskmanagementapi.event.TaskCreatedEvent;
import io.java_core.taskmanagementapi.exception.TaskNotFoundException;
import io.java_core.taskmanagementapi.mapper.TaskMapper;
import io.java_core.taskmanagementapi.model.*;
import io.java_core.taskmanagementapi.repository.TaskAuditEntryRepository;
import io.java_core.taskmanagementapi.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TaskAuditEntryRepository taskAuditEntryRepository;


    public TaskService(TaskRepository taskRepo, ApplicationEventPublisher eventPublisher, ObjectFactory<AuditEntry> auditEntry, TaskProperties taskProperties, TaskMapper taskMapper,
                       TaskAuditEntryRepository taskAuditEntryRepository) {
        this.taskRepo = taskRepo;
        this.eventPublisher = eventPublisher;
        this.auditEntry = auditEntry;
        this.taskProperties = taskProperties;
        this.taskMapper = taskMapper;
        this.taskAuditEntryRepository = taskAuditEntryRepository;
    }

    @Transactional
    public Task createTask(String title, String description) throws IllegalArgumentException {

        if (taskRepo.count() >= taskProperties.getLimits().getMaxTasks()) {
            throw new IllegalArgumentException("Task Limit Exceeded...");
        }

        TaskEntity newTaskEntity = new TaskEntity(title, description, TaskStatus.PENDING);
        TaskAuditEntry taskAuditEntry = new TaskAuditEntry(TaskAction.CREATED, "New task is created");
        newTaskEntity.addLogs(taskAuditEntry);

        log.info("New Task Created {}", newTaskEntity);
        log.info("New Task Audit Entry Created {}", taskAuditEntry);

        Task savedTask = taskMapper.toDomain(taskRepo.save(newTaskEntity));
        taskAuditEntryRepository.save(taskAuditEntry);

        eventPublisher.publishEvent(new TaskCreatedEvent(this, savedTask));
        log.info(" [AUDIT] | Task Created | {} | [ {} ]", auditEntry.getObject(), savedTask.id());

        return savedTask;
    }

    @Transactional
    public Task completeTask(String id) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found", id));

        task.setStatus(TaskStatus.COMPLETED);

        TaskAuditEntry taskAuditEntry = new TaskAuditEntry(TaskAction.UPDATED, "Task Status Updated Completed");
        task.addLogs(taskAuditEntry);
        taskAuditEntry.setTaskEntity(task);

        taskRepo.save(task);
        taskAuditEntryRepository.save(taskAuditEntry);

        Task completedTask = taskMapper.toDomain(task);
        eventPublisher.publishEvent(new TaskCompletedEvent(this, completedTask));
        log.info(" [AUDIT] | Task Completed | {} | [ {} ]", auditEntry.getObject(), task.getId());
        return completedTask;
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks(Pageable pageable) {
        return taskRepo.findAllByColumn(pageable).stream().map(taskMapper::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks(TaskStatus status, Pageable pageable) {
        return taskRepo.findAllByStatus(status, pageable).stream().map(taskMapper::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(String id) {
        return taskRepo.findById(id).stream().map(taskMapper::toDomain).findAny();
    }

    @Transactional
    public void deleteTask(String id) {
        TaskEntity taskEntity = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found", id));
        TaskAuditEntry taskAuditEntry = new TaskAuditEntry(TaskAction.UPDATED, "Task is deleted: " + taskEntity);
        taskAuditEntry.setTaskEntity(taskEntity);
        taskAuditEntryRepository.save(taskAuditEntry);
        taskRepo.deleteById(id);
    }

    @Transactional
    public Task updateTask(String id, String title, String description) {
        TaskEntity t = taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found", id));
        if (description != null && !description.isBlank()) t.setDescription(description);
        t.setTitle(title);
        TaskAuditEntry taskAuditEntry = new TaskAuditEntry(TaskAction.UPDATED, "Task is updated");
        t.addLogs(taskAuditEntry);
        log.info("Task is updated...");
        taskAuditEntryRepository.save(taskAuditEntry);
        return taskMapper.toDomain(taskRepo.save(t));
    }
}