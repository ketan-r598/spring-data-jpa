package io.java_core.taskmanagementapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "task_audit_entries")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TaskAuditEntry extends BaseEntity {

    protected TaskAuditEntry() {}

    public TaskAuditEntry(TaskAction taskAction, String description) {
        this.taskAction = taskAction;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "task_audit_entry_id")
    private String id;

    @Enumerated(EnumType.STRING)
    private TaskAction taskAction;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private TaskEntity taskEntity;
}
