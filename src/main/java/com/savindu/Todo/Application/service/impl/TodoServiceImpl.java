package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Todo;
import com.savindu.Todo.Application.repository.TodoRepository;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createTodo(TodoRequest todoRequest, Long userId) {

        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));


        Todo todo = new Todo();
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setUser(appUser);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());


        todoRepository.save(todo);


        return "Todo created successfully with ID: " + todo.getId();
    }
}
