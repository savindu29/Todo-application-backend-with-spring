package com.savindu.Todo.Application.controller;

import com.savindu.Todo.Application.dto.request.AuthenticationRequest;
import com.savindu.Todo.Application.dto.request.RegisterRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.dto.response.ErrorResponseDto;
import com.savindu.Todo.Application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse<Object>> register(
            @Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        HashMap<String, Object> response = userService.registerUser(registerRequest);
        return ResponseEntity.ok(AppResponse.builder().data(response).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse<Object>> login(
            @Valid @RequestBody AuthenticationRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .status("error")
                    .title("Validation Error")
                    .detail(bindingResult.getAllErrors().toString())
                    .build();
            return ResponseEntity.badRequest().body(AppResponse.<Object>builder().error(errorResponse).build());
        }

        HashMap<String, Object> response = userService.login(request);
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
