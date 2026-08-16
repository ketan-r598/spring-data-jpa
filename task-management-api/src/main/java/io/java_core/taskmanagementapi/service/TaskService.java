package io.java_core.taskmanagementapi.service;

import io.java_core.taskmanagementapi.configuration.TaskProperties;
import io.java_core.taskmanagementapi.event.TaskCompletedEvent;
import io.java_core.taskmanagementapi.event.TaskCreatedEvent;
import io.java_core.taskmanagementapi.exception.TaskNotFoundException;
import io.java_core.taskmanagementapi.mapper.TaskMapper;
import io.java_core.taskmanagementapi.model.AuditEntry;
import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;
import io.java_core.taskmanagementapi.repository.TaskRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class TaskService {

    private final Counter taskCreatedCounter;
    private final Counter taskCompletedCounter;
    private final Timer createTaskTimer;

    private final TaskRepository taskRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final TaskProperties taskProperties;

    private final ObjectFactory<AuditEntry> auditEntry;
    private final TaskMapper taskMapper;


    public TaskService(TaskRepository taskRepo, ApplicationEventPublisher eventPublisher, ObjectFactory<AuditEntry> auditEntry, TaskProperties taskProperties, MeterRegistry meterRegistry, TaskMapper taskMapper) {
        this.taskRepo = taskRepo;
        this.eventPublisher = eventPublisher;
        this.auditEntry = auditEntry;
        this.taskProperties = taskProperties;
        this.taskCreatedCounter = meterRegistry.counter("task.created");
        this.taskCompletedCounter = meterRegistry.counter("task.completed");
        this.createTaskTimer = Timer.builder("task.created.duration")
                .description("Time taken to create a task")
                .register(meterRegistry);
        this.taskMapper = taskMapper;
    }

    public Task createTask(String title, String description) throws Exception {

        return createTaskTimer.recordCallable(() -> {
            System.out.println("Max task limit: " + taskProperties.getLimits().getMaxTasks());
            if (taskRepo.findAll().size() >= taskProperties.getLimits().getMaxTasks()) {
                throw new IllegalArgumentException("Task Limit Exceeded...");
            }

            Task savedTask = taskMapper.toDomain(taskRepo.save(taskMapper.toEntity(new Task(UUID.randomUUID().toString(), title, description, TaskStatus.PENDING))));

            eventPublisher.publishEvent(new TaskCreatedEvent(this, savedTask));

            System.out.println();
            System.out.println(" [AUDIT] | Task Created | " + auditEntry.getObject() + " | [ " + savedTask.id() + " ]");
            System.out.println();

            taskCreatedCounter.increment();
            return savedTask;
        });


    }

    public Task completeTask(String id) {
        Task oldTask = taskRepo.findById(id).map(taskMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id..."));

        taskRepo.deleteById(id);
        Task newTask = new Task(oldTask.id(), oldTask.title(), oldTask.description(), TaskStatus.COMPLETED);
        newTask = taskMapper.toDomain(taskRepo.save(taskMapper.toEntity(newTask)));

        eventPublisher.publishEvent(new TaskCompletedEvent(this, newTask));

        System.out.println();
        System.out.println(" [AUDIT] | Task Completed | " + auditEntry.getObject() + " | [ " + newTask.id() + " ]");
        System.out.println();

        taskCompletedCounter.increment();
        return newTask;
    }

//    public List<Task> getAllTasks() {
//        return taskRepo.findAll();
//    }

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
        Task t = getTaskById(id).orElseThrow(() -> new TaskNotFoundException("Task not Found", id));

        String newDescription = (description == null || description.isBlank())
                ? t.description()
                : description;

        Task updatedTask = new Task(id, title, newDescription, t.status());
        var returnTask = taskRepo.save(taskMapper.toEntity(updatedTask));

        return taskMapper.toDomain(returnTask);
    }
}