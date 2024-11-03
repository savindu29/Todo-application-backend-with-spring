package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.entity.Priority;

import java.sql.Date;
import java.util.HashMap;

public interface TodoService {
    HashMap<String, Object> createTodo(TodoRequest todoRequest);
    HashMap<String, Object> updateTodo(TodoRequest todoRequest, Long id);

    String deleteTodoById(Long id);

    HashMap<String, Object> searchTodos(String title, Priority priority, boolean completed, Date dueDate);
}
