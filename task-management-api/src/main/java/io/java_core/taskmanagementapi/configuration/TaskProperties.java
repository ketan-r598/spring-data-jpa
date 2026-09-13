package io.java_core.taskmanagementapi.configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "task")
@Validated
public class TaskProperties {

    @Valid
    private final Limits limits = new Limits();

    @Valid
    private final Storage storage = new Storage();

    @Valid
    private final Notification notification = new Notification();

    public class Limits {

        @Min(1)
        private int maxTasks = 10;

        @Min(10)
        private int maxTitleLength = 50;

        public int getMaxTasks() {
            return maxTasks;
        }

        public int getMaxTitleLength() {
            return maxTitleLength;
        }

        public void setMaxTasks(int maxTasks) {
            this.maxTasks = maxTasks;
        }

        public void setMaxTitleLength(int maxTitleLength) {
            this.maxTitleLength = maxTitleLength;
        }
    }

    public class Storage {

        @NonNull
        @NotBlank
        private String filePath;

        @NonNull
        @Pattern(regexp = "json|xml|csv")
        private String format;

        public @NonNull String getFilePath() {
            return filePath;
        }

        public void setFilePath(@NonNull String filePath) {
            this.filePath = filePath;
        }

        public @NonNull String getFormat() {
            return format;
        }

        public void setFormat(@NonNull String format) {
            this.format = format;
        }
    }

    public class Notification {

        private boolean enabled = false;

        @NonNull
        private String channel;

        public @NonNull String getChannel() {
            return channel;
        }

        public void setChannel(@NonNull String channel) {
            this.channel = channel;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public Notification getNotification() {
        return notification;
    }

    public Limits getLimits() {
        return limits;
    }

    public Storage getStorage() {
        return storage;
    }
}
