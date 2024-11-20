package com.test.taskmanagementsystem.exceptions;

public class ExecutorNotFound extends RuntimeException {
    public ExecutorNotFound() {
        super("Executor not found");
    }
}