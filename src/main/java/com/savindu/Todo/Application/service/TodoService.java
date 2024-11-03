package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.entity.Priority;

import java.sql.Date;
import java.util.HashMap;

public interface TodoService {
    AppResponse<Object> createTodo(TodoRequest todoRequest,Long userId);
    AppResponse<Object>updateTodo(TodoRequest todoRequest, Long id,Long userId);

    AppResponse<Object> deleteTodoById(Long id,Long userId);

    AppResponse<Object> searchTodos(
            Integer taskStatus, Integer taskPriority, Integer taskId, String sortBy,String taskCreatedFromDate,
            String taskCreatedToDate, String taskDueFromDate, String taskDueToDate,Long userId, Integer page, Integer size
    );
}
