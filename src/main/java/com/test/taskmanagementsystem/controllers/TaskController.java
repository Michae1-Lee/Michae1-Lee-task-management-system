package com.test.taskmanagementsystem.controllers;

import com.test.taskmanagementsystem.dto.TaskDto;
import com.test.taskmanagementsystem.exceptions.AuthorNotFound;
import com.test.taskmanagementsystem.exceptions.ExecutorNotFound;
import com.test.taskmanagementsystem.exceptions.TaskNotFound;
import com.test.taskmanagementsystem.exceptions.UserNotFound;
import com.test.taskmanagementsystem.mappers.Mapper;
import com.test.taskmanagementsystem.models.Task;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.services.TaskService;
import com.test.taskmanagementsystem.services.UserService;
import com.test.taskmanagementsystem.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final UserService userService;
    private final TaskService taskService;
    private final Mapper mapper = new Mapper();

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @RequestParam(value = "author", required = false) String au,
            @RequestParam(value = "executor", required = false) String ex,
            @RequestParam(value = "page", required = false) String p,
            @RequestParam(value = "tasks_per_page",required = false) String tasksPerPage) {
        User author = null;
        User executor = null;

        if (au != null) {
            try {
                author = userService.getUserByUsername(au);
            } catch (UserNotFound e) {
                throw new AuthorNotFound();
            }
        }

        if (ex != null) {
            try {
                executor = userService.getUserByUsername(ex);
            } catch (UserNotFound e) {
                throw new ExecutorNotFound();
            }
        }

        List<Task> tasks = taskService.getTasks(author, executor, p,tasksPerPage);
        List<TaskDto> taskDtos = tasks.stream()
                .map(mapper::convertToTaskDto)
                .toList();
        return ResponseEntity.ok(taskDtos);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        TaskDto taskDto = mapper.convertToTaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{taskId}/update")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto));
    }

    @PutMapping("/{taskId}/update_status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam String status) {

        String username = SecurityUtil.getCurrentUsername();
        Task updatedTask = taskService.updateTaskStatus(taskId, status, username);
        return ResponseEntity.ok(updatedTask);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(AuthorNotFound e){
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(ExecutorNotFound e){
        return new ResponseEntity<>("Executor not found", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(TaskNotFound e){
        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(AccessDeniedException e){
        return new ResponseEntity<>("You are not the executor of this task", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid json format", HttpStatus.BAD_REQUEST);
    }
}
