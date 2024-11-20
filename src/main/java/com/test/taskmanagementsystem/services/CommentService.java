package com.test.taskmanagementsystem.services;

import com.test.taskmanagementsystem.dto.CommentDto;
import com.test.taskmanagementsystem.exceptions.TaskNotFound;
import com.test.taskmanagementsystem.models.Comment;
import com.test.taskmanagementsystem.repositories.CommentRepository;
import com.test.taskmanagementsystem.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public void addComment(Long taskId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setTask(taskRepository.findById(taskId).orElseThrow(TaskNotFound::new));
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
