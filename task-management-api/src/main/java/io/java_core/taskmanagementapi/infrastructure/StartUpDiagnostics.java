package io.java_core.taskmanagementapi.infrastructure;

import io.java_core.taskmanagementapi.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StartUpDiagnostics implements CommandLineRunner {

    private final Environment env;
    private final TaskRepository taskRepository;

    public StartUpDiagnostics(Environment env, TaskRepository taskRepository) {
        this.env = env;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n");

        System.out.println("=== STARTUP DIAGNOSTICS ===");
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));

        System.out.println("Repository type: " + taskRepository.getClass().getSimpleName());
        System.out.println("Repository type: " + taskRepository.findAll().size());
        System.out.println("============================");

        System.out.println("\n");

    }
}
