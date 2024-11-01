package com.savindu.Todo.Application.controller;

import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.service.JwtService;
import com.savindu.Todo.Application.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public String createTodo(@Valid @RequestBody TodoRequest todoRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            System.out.println("Authorization header: " + jwt.getTokenValue());  // Print the raw JWT token
            Long userId = jwtService.getUserIdFromToken(jwt.getTokenValue()); // Get userId from the JWT

            return todoService.createTodo(todoRequest, userId);
        }
        throw new RuntimeException("Authentication object is not of type Jwt");
    }
}
