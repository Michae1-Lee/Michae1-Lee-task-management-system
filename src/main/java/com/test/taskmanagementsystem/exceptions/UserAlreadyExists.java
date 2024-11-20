package com.test.taskmanagementsystem.exceptions;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists() {
        super("User already exists.");
    }

    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
