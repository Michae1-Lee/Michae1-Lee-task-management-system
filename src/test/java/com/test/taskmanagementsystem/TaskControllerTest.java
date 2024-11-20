package com.test.taskmanagementsystem;

import com.test.taskmanagementsystem.controllers.TaskController;
import com.test.taskmanagementsystem.dto.TaskDto;
import com.test.taskmanagementsystem.exceptions.TaskNotFound;
import com.test.taskmanagementsystem.models.Task;
import com.test.taskmanagementsystem.services.TaskService;
import com.test.taskmanagementsystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = new TaskDto();
        when(taskService.createTask(taskDto)).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.createTask(taskDto);

        assertEquals(200, response.getStatusCodeValue());
        verify(taskService, times(1)).createTask(taskDto);
    }

    @Test
    void testGetAllTasksWithPagination() {
        Task task = new Task();
        task.setId(1L);
        List<Task> tasks = List.of(task);
        Page<Task> page = new PageImpl<>(tasks);

        when(taskService.getTasks(null, null, "0", "10")).thenReturn(page.getContent());

        ResponseEntity<List<TaskDto>> response = taskController.getAllTasks(null, null, "0", "10");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(taskService, times(1)).getTasks(null, null, "0", "10");
    }

    @Test
    void testGetAllTasksWithoutPagination() {
        Task task = new Task();
        task.setId(1L);
        List<Task> tasks = List.of(task);

        when(taskService.getTasks(null, null, null, null)).thenReturn(tasks);

        ResponseEntity<List<TaskDto>> response = taskController.getAllTasks(null, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(taskService, times(1)).getTasks(null, null, null, null);
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<TaskDto> response = taskController.getTaskById(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testHandleTaskNotFound() {
        ResponseEntity<String> response = taskController.handleException(new TaskNotFound());
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Task not found", response.getBody());
    }
}
