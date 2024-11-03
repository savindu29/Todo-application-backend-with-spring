package com.savindu.Todo.Application.service.impl;

import com.nimbusds.jwt.util.DateUtils;
import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.Exception.UnauthorizedAccessException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.dto.response.PagingResponseDto;
import com.savindu.Todo.Application.dto.response.TodoResponse;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Priority;
import com.savindu.Todo.Application.entity.Todo;
import com.savindu.Todo.Application.mapper.TodoMapper;
import com.savindu.Todo.Application.repository.PriorityRepository;
import com.savindu.Todo.Application.repository.TodoRepository;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.TodoService;
import com.savindu.Todo.Application.util.JwtUtil;
import com.savindu.Todo.Application.util.DateTimeValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PriorityRepository priorityRepository;
    Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Override
    public AppResponse<Object> createTodo(TodoRequest todoRequest) {
        HashMap<String, Object> response = new HashMap<>();


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
            AppUser appUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            Priority priority = priorityRepository.findById(todoRequest.getPriority())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + todoRequest.getPriority()));

            Todo todo = new Todo();
            todo.setTitle(todoRequest.getTitle());
            todo.setDescription(todoRequest.getDescription());
            todo.setUser(appUser);
            todo.setPriority(priority);
            todo.setStatus(1);
            todo.setDueDate(todoRequest.getDueDate().toLocalDate());

            Todo savedTodo = todoRepository.save(todo);
            logger.info(("Todo created successfully with ID: " + savedTodo.getId()));

            response.put("status", "success");
            response.put("message", "Todo created successfully");

            response.put("data", savedTodo);

        return AppResponse.builder().data(response).build();
    }

    @Override
    public AppResponse<Object> updateTodo(TodoRequest todoRequest, Long id) {
        HashMap<String, Object> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Priority priority = priorityRepository.findById(todoRequest.getPriority())
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + todoRequest.getPriority()));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        if (!todo.getUser().getId().equals(appUser.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to modify this Todo item");
        }

        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setPriority(priority);
        todo.setStatus(todo.getStatus());
        todo.setDueDate(todoRequest.getDueDate().toLocalDate());

        Todo updatedTodo = todoRepository.save(todo);
        logger.info(("Todo updated successfully with ID: " + updatedTodo.getId()));

        response.put("status", "success");
        response.put("message", "Todo updated successfully");

        response.put("data", updatedTodo);


        return AppResponse.builder().data(response).build();
    }

    @Override
    public AppResponse<Object> deleteTodoById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        if (!todo.getUser().getId().equals(appUser.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to delete this Todo item");
        }

        todoRepository.deleteById(id);
        logger.info(("Todo deleted successfully with ID: " + id));

        return AppResponse.builder().data("Todo deleted successfully").build();
    }

    @Override
    public AppResponse<Object> searchTodos(Integer taskStatus, Integer taskPriority, Integer taskId, String sortBy, String taskCreatedFromDate, String taskCreatedToDate, String taskDueFromDate, String taskDueToDate,  Integer page, Integer size) {
        HashMap<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = jwtUtil.getUserIdFromAuthentication(authentication);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDateTime startOfDayTaskFrom = DateTimeValidateUtil.parseLocalDateTime(taskCreatedFromDate, true);
        LocalDateTime endOfDayTaskTo = DateTimeValidateUtil.parseLocalDateTime(taskCreatedToDate, false);
        LocalDate startOfDayTaskDue = DateTimeValidateUtil.parseLocalDate(taskDueFromDate);
        LocalDate endOfDayTaskDue = DateTimeValidateUtil.parseLocalDate(taskDueToDate);


        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortBy == null ? "createdAt" : sortBy));



        Page<Todo> todoEntity = todoRepository.findAllTodoTask( taskStatus, taskPriority, taskId, startOfDayTaskFrom, endOfDayTaskTo,
                startOfDayTaskDue, endOfDayTaskDue, userId, pageable
        );
        List<TodoResponse> todoResponses = TodoMapper.todoResponseMapper(todoEntity.getContent());
        PagingResponseDto pagingResponseDto = new PagingResponseDto(
                page,
                size,
                (int) todoEntity.getTotalElements(),
                todoEntity.getTotalPages()

        );
        response.put("status", "success");
        response.put("data", todoResponses);

        return AppResponse.builder().data(response).paging(pagingResponseDto).build();


    }

}
