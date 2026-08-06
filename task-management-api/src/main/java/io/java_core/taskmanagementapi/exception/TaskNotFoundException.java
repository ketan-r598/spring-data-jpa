package io.java_core.taskmanagementapi.exception;

public class TaskNotFoundException extends RuntimeException {

    private final String taskId;

    public TaskNotFoundException(String message, String taskId) {
        super(message + ": " + taskId);
        this.taskId = taskId;
    }
}
