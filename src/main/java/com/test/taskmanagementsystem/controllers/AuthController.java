package com.test.taskmanagementsystem.controllers;

import com.test.taskmanagementsystem.dto.UserDto;
import com.test.taskmanagementsystem.exceptions.UserAlreadyExists;
import com.test.taskmanagementsystem.jwt.LoginResponse;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.services.AuthService;
import com.test.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final UserService userService;

    private final AuthService authenticationService;
    @Autowired
    public AuthController(UserService userService, AuthService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDto user,
                         @RequestParam String role) {
        //role == "USER" или "ADMIN"
        return userService.register(user, role);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto) {
        try {
            LoginResponse response = authenticationService.authenticate(userDto);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(UserAlreadyExists e){
        return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid json format", HttpStatus.CONFLICT);
    }
}
