package com.savindu.Todo.Application.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data

public class TodoResponse {
    private Long id;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate dueDate;
    private String description;
    private String title;
    private PriorityResponse priority;
}
