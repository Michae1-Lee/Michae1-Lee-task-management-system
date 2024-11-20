package com.test.taskmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String author;
    private String executor;
    private List<CommentDto> comments;
}
