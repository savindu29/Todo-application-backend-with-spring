package com.savindu.Todo.Application.controller;

import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.Exception.UnauthorizedAccessException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.dto.response.ErrorResponseDto;
import com.savindu.Todo.Application.entity.Priority;
import com.savindu.Todo.Application.service.TodoService;
import com.savindu.Todo.Application.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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

    @PatchMapping("/update/{id}")
    public ResponseEntity<AppResponse<Object>> updateTodo(@Valid @RequestBody TodoRequest todoRequest, @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        HashMap<String, Object> response = todoService.updateTodo(todoRequest, id);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse<Object>> deleteTodo(@PathVariable Long id) {
        String response = todoService.deleteTodoById(id);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }
    @GetMapping("/search/{title}/{priority}/{completed}/{dueDate}")
    public ResponseEntity<AppResponse<Object>> searchTodo(
            @PathVariable String title,
            @PathVariable Priority priority,
            @PathVariable boolean completed,
            @PathVariable Date dueDate) {
        HashMap<String, Object> response = todoService.searchTodos(title, priority, completed, dueDate);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<AppResponse<Object>> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Unauthorized Access")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(403).body(AppResponse.<Object>builder().error(errorResponse).build());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Resource Not Found")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(404).body(AppResponse.<Object>builder().error(errorResponse).build());
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


