package com.savindu.Todo.Application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;



}
