package io.java_core.taskmanagementapi.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Scope("prototype")
public class AuditEntry {

    private final String instanceId;
    private final String timestamp;

    public AuditEntry() {
        instanceId = UUID.randomUUID().toString();
        timestamp = Instant.now().toString();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AuditEntry{" +
                "instanceId=" + instanceId +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
