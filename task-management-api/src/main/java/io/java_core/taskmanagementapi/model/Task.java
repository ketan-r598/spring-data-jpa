package io.java_core.taskmanagementapi.model;

import java.io.Serializable;

public record Task(String id, String title, String description, TaskStatus status, int taskLogsCount) implements Serializable { }