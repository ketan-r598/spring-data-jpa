package io.java_core.taskmanagementapi.dto;

import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;

public record TaskResponse(String id, String title, String description, TaskStatus  status, int taskLogsCount) {

    public TaskResponse(Task task) {
        this(task.id(), task.title(), task.description(), task.status(), task.taskLogsCount());
    }
}
