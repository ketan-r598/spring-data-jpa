package io.java_core.taskmanagementapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    String id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @Enumerated(EnumType.STRING)
    TaskStatus status;

    Instant createdAt;
    Instant updatedAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = Instant.now();
    }
}
