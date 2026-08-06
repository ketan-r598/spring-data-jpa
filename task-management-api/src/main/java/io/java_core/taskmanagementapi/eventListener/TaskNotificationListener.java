package io.java_core.taskmanagementapi.eventListener;

import io.java_core.taskmanagementapi.event.TaskCompletedEvent;
import io.java_core.taskmanagementapi.event.TaskCreatedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "task.notification.enabled", havingValue = "true")
public class TaskNotificationListener {

    @EventListener
    public void onTaskCreated(TaskCreatedEvent taskCreatedEvent) {
        System.out.println();
        System.out.println(" >>> TaskCreatedEvent is triggered...");
        System.out.println(" >>> New Task Created " + taskCreatedEvent.getTask());
        System.out.println();
    }

    @EventListener
    public void onTaskCompleted(TaskCompletedEvent taskCompletedEvent) {
        System.out.println();
        System.out.println(" >>> TaskCompletedEvent is triggered...");
        System.out.println(" >>> Task Completed " + taskCompletedEvent.getTask());
        System.out.println();
    }
}