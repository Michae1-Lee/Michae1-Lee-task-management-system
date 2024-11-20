package com.test.taskmanagementsystem.exceptions;

public class TaskNotFound extends RuntimeException {
    public TaskNotFound() {
        super("Task not found");
    }
}
