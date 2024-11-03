package com.savindu.Todo.Application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriorityResponse {
    private Integer id;
    private String name;
    private String code;
}
