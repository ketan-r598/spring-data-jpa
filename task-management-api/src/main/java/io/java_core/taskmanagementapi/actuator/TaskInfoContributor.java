package io.java_core.taskmanagementapi.actuator;

import io.java_core.taskmanagementapi.configuration.TaskProperties;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class TaskInfoContributor implements InfoContributor {

    private final TaskProperties taskProperties;

    public TaskInfoContributor(TaskProperties taskProperties) {
        this.taskProperties = taskProperties;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("taskLimits", taskProperties.getLimits().getMaxTasks())
                .withDetail("storageFormat", taskProperties.getStorage().getFormat())
                .withDetail("notificationEnabled", taskProperties.getNotification().isEnabled())
                .build();
    }
}
