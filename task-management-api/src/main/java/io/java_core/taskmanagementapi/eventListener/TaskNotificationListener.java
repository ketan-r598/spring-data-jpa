package io.java_core.taskmanagementapi.eventListener;

import io.java_core.taskmanagementapi.event.TaskCompletedEvent;
import io.java_core.taskmanagementapi.event.TaskCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "task.notification.enabled", havingValue = "true")
@Slf4j
public class TaskNotificationListener {

    @EventListener
    public void onTaskCreated(TaskCreatedEvent taskCreatedEvent) {
        log.info(" >>> TaskCreatedEvent is triggered...");
        log.info(" >>> New Task Created {}", taskCreatedEvent.getTask());
    }

    @EventListener
    public void onTaskCompleted(TaskCompletedEvent taskCompletedEvent) {
        log.info(" >>> TaskCompletedEvent is triggered...");
        log.info(" >>> Task Completed {}", taskCompletedEvent.getTask());
    }
}