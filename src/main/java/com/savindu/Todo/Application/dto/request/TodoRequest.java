package com.savindu.Todo.Application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savindu.Todo.Application.entity.Priority;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoRequest {
    @NotEmpty
    @JsonProperty("title")
    private String title;
    @NotEmpty
    @JsonProperty("description")
    private String description;
    @NotNull(message = "Priority is required")
    @JsonProperty("priority")
    private Priority priority;
    @JsonProperty("due_date")
    private LocalDateTime dueDate;
    @JsonProperty("completed")
    private boolean completed = false;



}
