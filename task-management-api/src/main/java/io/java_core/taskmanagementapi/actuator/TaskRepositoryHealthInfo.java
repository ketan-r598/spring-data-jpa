package io.java_core.taskmanagementapi.actuator;

import io.java_core.taskmanagementapi.repository.TaskRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TaskRepositoryHealthInfo implements HealthIndicator {

    public final TaskRepository taskRepository;

    public TaskRepositoryHealthInfo(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public @Nullable Health health() {
        try {
            int taskCount = taskRepository.findAll().size();
            return Health.up()
                    .withDetail("taskCount",taskCount)
                    .withDetail("repositoryType", taskRepository.getClass().getSimpleName())
                    .build();
        }catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
