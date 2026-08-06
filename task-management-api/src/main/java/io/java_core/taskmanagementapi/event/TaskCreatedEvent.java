package io.java_core.taskmanagementapi.event;

import io.java_core.taskmanagementapi.model.Task;
import org.springframework.context.ApplicationEvent;

public class TaskCreatedEvent extends ApplicationEvent {
    private final Task task;

    public TaskCreatedEvent(Object source, Task task) {
        super(source);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
