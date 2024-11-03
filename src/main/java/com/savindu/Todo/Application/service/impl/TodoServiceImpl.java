package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.Exception.UnauthorizedAccessException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Todo;
import com.savindu.Todo.Application.repository.TodoRepository;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.TodoService;
import com.savindu.Todo.Application.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Override
    public HashMap<String, Object> createTodo(TodoRequest todoRequest) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
            AppUser appUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            Todo todo = new Todo();
            todo.setTitle(todoRequest.getTitle());
            todo.setDescription(todoRequest.getDescription());
            todo.setUser(appUser);
            todo.setPriority(todoRequest.getPriority());
            todo.setDueDate(todoRequest.getDueDate());

            Todo savedTodo = todoRepository.save(todo);
            logger.info(("Todo created successfully with ID: " + savedTodo.getId()));

            response.put("status", "success");
            response.put("message", "Todo created successfully");

            response.put("todoId", savedTodo);
        } catch (DataAccessException e) {
            logger.error("Database error occurred while creating a Todo", e);
            throw new RuntimeException("Database error occurred while creating a Todo", e);
        } catch (RuntimeException e) {
            logger.error("An unexpected error occurred while creating a Todo", e);
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating a Todo", e);
            throw new RuntimeException("An unexpected error occurred while creating a Todo", e);
        }

        return response;
    }

    @Override
    public HashMap<String, Object> updateTodo(TodoRequest todoRequest, Long id) {
        HashMap<String, Object> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        if (!todo.getUser().getId().equals(appUser.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to modify this Todo item");
        }

        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setPriority(todoRequest.getPriority());
        todo.setDueDate(todoRequest.getDueDate());

        Todo updatedTodo = todoRepository.save(todo);
        logger.info(("Todo updated successfully with ID: " + updatedTodo.getId()));

        response.put("status", "success");
        response.put("message", "Todo updated successfully");

        response.put("todoId", updatedTodo);


        return response;
    }

}
