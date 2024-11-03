package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.TodoRequest;

import java.util.HashMap;

public interface TodoService {
    HashMap<String, Object> createTodo(TodoRequest todoRequest);
    HashMap<String, Object> updateTodo(TodoRequest todoRequest, Long id);
}
