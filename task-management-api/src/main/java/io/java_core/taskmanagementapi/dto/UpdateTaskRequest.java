package io.java_core.taskmanagementapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank
        @Size(min = 5, max = 50)
        String title,

        @Size(min = 10, max = 500)
        String description
) {
}
