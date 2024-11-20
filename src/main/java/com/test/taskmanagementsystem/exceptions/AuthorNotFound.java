package com.test.taskmanagementsystem.exceptions;

public class AuthorNotFound extends RuntimeException {
    public AuthorNotFound() {
        super("Author not found");
    }
}