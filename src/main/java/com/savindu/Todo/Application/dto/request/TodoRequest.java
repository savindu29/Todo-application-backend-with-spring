package com.savindu.Todo.Application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savindu.Todo.Application.entity.Priority;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;


@Data
public class TodoRequest {
    @NotNull
    @JsonProperty("title")
    private String title;
    @NotNull
    @JsonProperty("description")
    private String description;
    @NotNull(message = "1 for Urgent ,2 for High ,3 for Normal and 4 for Low ")
    private Integer priority;
    @NotNull(message = "yyyy-mm-dd")
    @JsonProperty("due_date")
    private Date dueDate;
    @NotNull(message = "1 for Complete and 0 for Incomplete")
    private Integer status;



}
