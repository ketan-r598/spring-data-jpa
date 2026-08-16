package io.java_core.taskmanagementapi.mapper;

import io.java_core.taskmanagementapi.model.Task;
import io.java_core.taskmanagementapi.model.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomain(TaskEntity entity) {
        Task newTask = new Task(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getStatus());
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
        TaskEntity entity = new TaskEntity();
        entity.setTitle(task.title());
        entity.setStatus(task.status());
        entity.setDescription(task.description());
        return entity;
    }

    private TaskEntity updateExistingentity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.id());
        entity.setTitle(task.title());
        entity.setStatus(task.status());
        entity.setDescription(task.description());
        return entity;
    }
}
