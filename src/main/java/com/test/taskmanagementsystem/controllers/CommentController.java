package com.test.taskmanagementsystem.controllers;
import com.test.taskmanagementsystem.dto.CommentDto;
import com.test.taskmanagementsystem.dto.TaskDto;
import com.test.taskmanagementsystem.exceptions.TaskNotFound;
import com.test.taskmanagementsystem.mappers.Mapper;
import com.test.taskmanagementsystem.models.Comment;
import com.test.taskmanagementsystem.services.CommentService;
import com.test.taskmanagementsystem.services.TaskService;
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
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final TaskService taskService;
    private final Mapper mapper = new Mapper();

    @Autowired
    public CommentController(CommentService commentService, TaskService taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/add/{taskId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long taskId, @RequestBody CommentDto commentDto) {
        String role = SecurityUtil.getCurrentRole();
        String username = SecurityUtil.getCurrentUsername();
        if(role.equals("ROLE_USER")) {
            if(username.equals(taskService.getTaskById(taskId).getExecutor().getUsername())){
                commentService.addComment(taskId, commentDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
            }
            else{
                throw new AccessDeniedException("");
            }
        }
        commentService.addComment(taskId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{taskId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTask(@PathVariable Long taskId) {
        List<CommentDto> comments = commentService.getCommentsByTaskId(taskId).stream()
                .map(mapper::convertToCommentDto).toList();
        return ResponseEntity.ok(comments);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(TaskNotFound e){
        return new ResponseEntity<>("Task not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid json format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(AccessDeniedException e){
        return new ResponseEntity<>("You are not the executor of this task", HttpStatus.CONFLICT);
    }
}