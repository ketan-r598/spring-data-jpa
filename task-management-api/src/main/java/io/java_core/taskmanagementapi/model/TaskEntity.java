package io.java_core.taskmanagementapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TaskEntity extends BaseEntity {

    protected TaskEntity() {
    }

    public TaskEntity(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "task_id")
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @BatchSize(size=20)
    @OneToMany(mappedBy = "taskEntity",orphanRemoval = false)
    private List<TaskAuditEntry> logs = new ArrayList<>();

    public void addLogs(TaskAuditEntry taskAuditEntry) {
        taskAuditEntry.setTaskEntity(this);
        logs.add(taskAuditEntry);
    }

}
