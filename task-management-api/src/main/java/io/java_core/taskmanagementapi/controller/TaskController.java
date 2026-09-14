package io.java_core.taskmanagementapi.controller;

import io.java_core.taskmanagementapi.dto.ApiResponse;
import io.java_core.taskmanagementapi.dto.CreateTaskRequest;
import io.java_core.taskmanagementapi.dto.TaskResponse;
import io.java_core.taskmanagementapi.dto.UpdateTaskRequest;
import io.java_core.taskmanagementapi.exception.TaskNotCreatedException;
import io.java_core.taskmanagementapi.exception.TaskNotFoundException;
import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskStatus;
import io.java_core.taskmanagementapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Returns the empty list if no task exists")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of task / EmptyList")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "id,asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Task> taskList;
        String[] sortInfo = sort.split(",");
        Pageable pageRequest;

        if(sortInfo.length == 2) {
            if(sortInfo[1].equalsIgnoreCase("desc"))
                pageRequest = PageRequest.of(page, size,Sort.by(sortInfo[0].toLowerCase()).descending());
            else
                pageRequest = PageRequest.of(page, size,Sort.by(sortInfo[0].toLowerCase()).ascending());
        } else {
            pageRequest = PageRequest.of(page, size,Sort.by(sortInfo[0].toLowerCase()).ascending());
        }

        if (status.isBlank()) {
            taskList = taskService.getAllTasks(pageRequest);
        } else {
            TaskStatus s = TaskStatus.valueOf(status.toUpperCase());
            taskList = taskService.getAllTasks(s, pageRequest);
        }


        List<TaskResponse> taskResponsesList = taskList.stream()
                .map(TaskResponse::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(taskResponsesList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(TaskResponse::new)
                .map(ApiResponse::of)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TaskNotFoundException("Task with id - " + id + " does not exist.", id))
                ;

    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest task) {
        try {
            Task newTask = taskService.createTask(task.title(), task.description());
            TaskResponse taskResponse = new TaskResponse(newTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(taskResponse));
        } catch (IllegalArgumentException e) {
            throw new TaskNotCreatedException(e.getMessage(), task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable String id, @Valid @RequestBody UpdateTaskRequest task) {
        Task t = taskService.updateTask(id, task.title(), task.description());

        return ResponseEntity.ok(ApiResponse.of(new TaskResponse(t)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
