package com.savindu.Todo.Application.controller;

import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.Exception.UnauthorizedAccessException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.dto.response.ErrorResponseDto;
import com.savindu.Todo.Application.entity.Priority;
import com.savindu.Todo.Application.service.TodoService;
import com.savindu.Todo.Application.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<Object>> createTodo(
            @Valid
            @RequestBody TodoRequest todoRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        AppResponse<Object> response = todoService.createTodo(todoRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<AppResponse<Object>> updateTodo(
            @Valid @RequestBody TodoRequest todoRequest,
            @PathVariable Long id,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        AppResponse<Object> response = todoService.updateTodo(todoRequest, id);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse<Object>> deleteTodo(@PathVariable Long id) {
        AppResponse<Object> response = todoService.deleteTodoById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getData")
    public ResponseEntity<AppResponse<Object>> getTodoData(
            @RequestParam(value = "status" ,required = false) Integer status,
            @RequestParam(value = "priority" , required = false) Integer priority,
            @RequestParam(value = "task_id" , required = false) Integer taskId,
            @RequestParam(value = "sort_by" , required = false) String sortBy,
            @RequestParam(value = "created_from_date" , required = false) String createdFromDate,
            @RequestParam(value = "created_to_date" , required = false) String createdToDate,
            @RequestParam(value = "due_from_date" , required = false) String dueFromDate,
            @RequestParam(value = "due_to_date" , required = false) String dueToDate,
            @RequestParam(value = "page", required = true)  int page,
            @RequestParam(value = "size", required = true)  int size , HttpServletResponse res
    ) {
        AppResponse<Object> response = todoService.searchTodos(status,priority,taskId,sortBy,createdFromDate,
                createdToDate,dueFromDate,dueToDate,page,size);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<AppResponse<Object>> handleDateTimeParseException(UnauthorizedAccessException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Invalid LocalDateTime format correct format is (yyyy-MM-dd)")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AppResponse.<Object>builder().error(errorResponse).build());
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<AppResponse<Object>> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Unauthorized Access")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AppResponse.<Object>builder().error(errorResponse).build());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Resource Not Found")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AppResponse.<Object>builder().error(errorResponse).build());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppResponse<Object>> handleRuntimeException(RuntimeException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .title("Unexpected Error")
                .detail(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppResponse.<Object>builder().error(errorResponse).build());
    }
}


