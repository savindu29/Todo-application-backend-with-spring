package com.savindu.Todo.Application.controller;

import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.dto.response.ErrorResponseDto;
import com.savindu.Todo.Application.util.JwtUtil;
import com.savindu.Todo.Application.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<Object>> createTodo(@Valid @RequestBody TodoRequest todoRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        HashMap<String, Object> response = todoService.createTodo(todoRequest);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppResponse<Object>> handleRuntimeException(RuntimeException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Unexpected Error")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(AppResponse.<Object>builder().error(errorResponse).build());
    }
}


