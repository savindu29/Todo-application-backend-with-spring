package com.savindu.Todo.Application.mapper;

import com.savindu.Todo.Application.dto.response.PriorityResponse;
import com.savindu.Todo.Application.dto.response.TodoResponse;
import com.savindu.Todo.Application.entity.Todo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoMapper {
    public static TodoResponse todoResponseMapper(Todo todo) {
        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setId(todo.getId());
        todoResponse.setTitle(todo.getTitle());
        todoResponse.setDescription(todo.getDescription());
        todoResponse.setDueDate(todo.getDueDate());
        todoResponse.setStatus(todo.getStatus());
        todoResponse.setCreatedAt(todo.getCreatedAt());
        todoResponse.setUpdatedAt(todo.getUpdatedAt());
        todoResponse.setPriority(new PriorityResponse(
                todo.getPriority().getId(),
                todo.getPriority().getName(),
                todo.getPriority().getCode()
        ));

        return todoResponse;

    }
    public static List<TodoResponse> todoResponseMapper(List<Todo> todos) {
        return todos.stream().map(TodoMapper::todoResponseMapper).collect(Collectors.toList());
    }
}
