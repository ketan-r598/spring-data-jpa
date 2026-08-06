package io.java_core.taskmanagementapi.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app.cors")
public record CorsConfigurationProperties(
        @NotEmpty(message = "app.cors.allowed-origins must not be empty — the API is unreachable from any browser-based frontend without it")
        List<String> allowedOrigins) {
}