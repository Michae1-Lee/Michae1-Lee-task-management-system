package com.test.taskmanagementsystem.mappers;

import com.test.taskmanagementsystem.dto.CommentDto;
import com.test.taskmanagementsystem.dto.TaskDto;
import com.test.taskmanagementsystem.models.Comment;
import com.test.taskmanagementsystem.models.Task;

import java.util.Collections;
import java.util.List;

public class Mapper {

    public CommentDto convertToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getText(),
                comment.getTask() != null && comment.getTask().getAuthor() != null
                        ? comment.getTask().getAuthor().getUsername()
                        : null
        );
    }

    public TaskDto convertToTaskDto(Task task) {
        List<CommentDto> comments = task.getComments() != null
                ? task.getComments().stream()
                .map(this::convertToCommentDto)
                .toList()
                : Collections.emptyList();

        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAuthor() != null ? task.getAuthor().getUsername() : null,
                task.getExecutor() != null ? task.getExecutor().getUsername() : null,
                comments
        );
    }
}
