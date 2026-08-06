package io.java_core.taskmanagementapi.exception;

import io.java_core.taskmanagementapi.dto.CreateTaskRequest;

public class TaskNotCreatedException extends RuntimeException {
    private final CreateTaskRequest task;

    public TaskNotCreatedException(String message, CreateTaskRequest createTaskRequest) {
        super(message + createTaskRequest.toString());
        task = createTaskRequest;
    }
}
