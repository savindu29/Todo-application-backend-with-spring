package com.savindu.Todo.Application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private String status;

    private String source;

    private String title;

    private String code;

    private String detail;
}
