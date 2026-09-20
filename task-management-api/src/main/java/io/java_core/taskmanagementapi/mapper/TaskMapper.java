package io.java_core.taskmanagementapi.mapper;

import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomain(TaskEntity entity) {
        Task newTask = new Task(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getStatus(), entity.getLogs().size());
        return newTask;
    }

    public TaskEntity toEntity(Task task) {
        if(task.id() == null) {
            return createNewEntity(task);
        } else {
            return updateExistingentity(task);
        }
    }

    private TaskEntity createNewEntity(Task task) {
        return new TaskEntity(task.title(), task.description(), task.status());
    }

    private TaskEntity updateExistingentity(Task task) {
        TaskEntity entity = new TaskEntity(task.title(), task.description(), task.status());
        entity.setId(task.id());
        return entity;
    }
}
