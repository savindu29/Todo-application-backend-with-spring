package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.TodoRequest;

public interface TodoService {
    String createTodo(TodoRequest todoRequest,Long userId);
}
