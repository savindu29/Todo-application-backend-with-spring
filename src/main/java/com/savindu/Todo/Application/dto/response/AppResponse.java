package com.savindu.Todo.Application.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppResponse<T>{
    private T data;
    private ErrorResponseDto error;
    private PagingResponseDto paging;
}
