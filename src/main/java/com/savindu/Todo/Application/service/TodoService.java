package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.entity.Priority;

import java.sql.Date;
import java.util.HashMap;

public interface TodoService {
    AppResponse<Object> createTodo(TodoRequest todoRequest);
    AppResponse<Object>updateTodo(TodoRequest todoRequest, Long id);

    AppResponse<Object> deleteTodoById(Long id);

    AppResponse<Object> searchTodos(
            Integer taskStatus, Integer taskPriority, Integer taskId, String sortBy,String taskCreatedFromDate,
            String taskCreatedToDate, String taskDueFromDate, String taskDueToDate, Integer page, Integer size
    );
}
