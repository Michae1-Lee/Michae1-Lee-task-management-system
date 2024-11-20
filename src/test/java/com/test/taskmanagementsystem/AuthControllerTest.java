package com.test.taskmanagementsystem;

import com.test.taskmanagementsystem.controllers.AuthController;
import com.test.taskmanagementsystem.dto.UserDto;
import com.test.taskmanagementsystem.exceptions.UserAlreadyExists;
import com.test.taskmanagementsystem.jwt.LoginResponse;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.services.AuthService;
import com.test.taskmanagementsystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserDto userDto = new UserDto();
        User user = new User();
        String role = "USER";

        when(userService.register(userDto, role)).thenReturn(user);

        User response = authController.register(userDto, role);

        assertEquals(user, response);
        verify(userService, times(1)).register(userDto, role);
    }

    @Test
    void testAuthenticateUser() {
        UserDto userDto = new UserDto();
        LoginResponse loginResponse = new LoginResponse("test", List.of("ROLE_USER"), "token");

        when(authService.authenticate(userDto)).thenReturn(loginResponse);

        ResponseEntity<?> response = authController.authenticateUser(userDto);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).authenticate(userDto);
    }

    @Test
    void testHandleUserAlreadyExists() {
        ResponseEntity<String> response = authController.handleException(new UserAlreadyExists());
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testHandleInvalidJsonFormat() {
        ResponseEntity<String> response = authController.handleException(new HttpMessageNotReadableException("Invalid json format"));
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Invalid json format", response.getBody());
    }
}
