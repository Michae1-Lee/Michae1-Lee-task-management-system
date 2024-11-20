package com.test.taskmanagementsystem;

import com.test.taskmanagementsystem.controllers.CommentController;
import com.test.taskmanagementsystem.dto.CommentDto;
import com.test.taskmanagementsystem.models.Comment;
import com.test.taskmanagementsystem.models.Task;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.services.CommentService;
import com.test.taskmanagementsystem.services.TaskService;
import com.test.taskmanagementsystem.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCommentAsExecutor() {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            CommentDto commentDto = new CommentDto();
            Task task = new Task();
            User executor = new User();
            executor.setUsername("testUser");
            task.setExecutor(executor);

            mockedSecurityUtil.when(SecurityUtil::getCurrentRole).thenReturn("ROLE_USER");
            mockedSecurityUtil.when(SecurityUtil::getCurrentUsername).thenReturn("testUser");
            when(taskService.getTaskById(1L)).thenReturn(task);
            doNothing().when(commentService).addComment(1L, commentDto);

            ResponseEntity<CommentDto> response = commentController.addComment(1L, commentDto);

            assertEquals(201, response.getStatusCodeValue());
            verify(commentService, times(1)).addComment(1L, commentDto);
        }
    }

    @Test
    void testAddCommentAsUnauthorizedUser() {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            CommentDto commentDto = new CommentDto();
            Task task = new Task();
            User executor = new User();
            executor.setUsername("testExecutor");
            task.setExecutor(executor);

            mockedSecurityUtil.when(SecurityUtil::getCurrentRole).thenReturn("ROLE_USER");
            mockedSecurityUtil.when(SecurityUtil::getCurrentUsername).thenReturn("unauthorizedUser");
            when(taskService.getTaskById(1L)).thenReturn(task);

            assertThrows(AccessDeniedException.class, () -> commentController.addComment(1L, commentDto));
        }
    }

    @Test
    void testGetCommentsByTask() {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentService.getCommentsByTaskId(1L)).thenReturn(List.of(comment));

        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByTask(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(commentService, times(1)).getCommentsByTaskId(1L);
    }
}
