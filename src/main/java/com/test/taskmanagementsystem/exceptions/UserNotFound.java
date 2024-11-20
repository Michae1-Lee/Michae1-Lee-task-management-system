package com.test.taskmanagementsystem.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("User not found");
    }
}
