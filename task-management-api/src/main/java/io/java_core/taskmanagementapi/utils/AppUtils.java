package io.java_core.taskmanagementapi.utils;

import io.java_core.taskmanagementapi.exception.InvalidSortFieldException;
import io.java_core.taskmanagementapi.model.Task;

import java.util.Comparator;

public class AppUtils {

    public static Comparator getTaskComparator(String fieldName, String sortDirection) {
        Comparator comparator = switch (fieldName) {
            case "title" -> Comparator.comparing(Task::title);
            case "description" -> Comparator.comparing(Task::description);
            case "id" -> Comparator.comparing(Task::id);
            case "status" -> Comparator.comparing(Task::status);
            default -> throw new InvalidSortFieldException("Invalid Sorting Field: " + fieldName);
        };

        if (sortDirection.equalsIgnoreCase("desc")) return comparator.reversed();
        return comparator;
    }
}
