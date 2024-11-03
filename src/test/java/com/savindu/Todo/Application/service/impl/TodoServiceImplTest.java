package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
import com.savindu.Todo.Application.Exception.UnauthorizedAccessException;
import com.savindu.Todo.Application.dto.request.TodoRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Priority;
import com.savindu.Todo.Application.entity.Todo;
import com.savindu.Todo.Application.repository.PriorityRepository;
import com.savindu.Todo.Application.repository.TodoRepository;
import com.savindu.Todo.Application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PriorityRepository priorityRepository;

    private AppUser mockUser1;
    private AppUser mockUser2;

    private Priority mockPriority;
    private TodoRequest todoRequest;
    private TodoRequest updateTodoRequest;
    private Todo mockTodo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockUser1 = new AppUser();
        mockUser1.setId(1L);
        mockUser2 = new AppUser();
        mockUser2.setId(2L);
        mockPriority = new Priority();
        mockPriority.setId(1);
        todoRequest = new TodoRequest();
        todoRequest.setTitle("Test Todo");
        todoRequest.setDescription("Description of Test Todo");
        todoRequest.setPriority(1);
        java.sql.Date dueDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        todoRequest.setDueDate(dueDate);
        todoRequest.setStatus(1);
        updateTodoRequest = new TodoRequest();
        updateTodoRequest.setTitle("Update Todo");
        updateTodoRequest.setDescription("Update  Test Todo");
        updateTodoRequest.setPriority(1);
        java.sql.Date dueDate1 = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        updateTodoRequest.setDueDate(dueDate);
        updateTodoRequest.setStatus(1);
        mockTodo = new Todo();
        mockTodo.setId(1L);
        mockTodo.setUser(mockUser1);
        mockTodo.setPriority(mockPriority);
    }



    @Test
    void testCreateTodoSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(priorityRepository.findById(1)).thenReturn(Optional.of(mockPriority));
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);
        AppResponse<Object> response = todoService.createTodo(todoRequest, 1L);
        @SuppressWarnings("unchecked")
        Map<String, Object> responseData = (Map<String, Object>) response.getData();
        assertEquals("success", responseData.get("status"));
        assertEquals("Todo created successfully", responseData.get("message"));
        verify(todoRepository, times(1)).save(any(Todo.class));
    }


    @Test
    void createTodoUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> todoService.createTodo(todoRequest, 1L));
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void createTodoPriorityNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(priorityRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> todoService.createTodo(todoRequest, 1L));
        verify(todoRepository, never()).save(any(Todo.class));
    }



    @Test
    void updateTodoSuccessful() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(priorityRepository.findById(1)).thenReturn(Optional.of(mockPriority));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);
        AppResponse<Object> response = todoService.updateTodo(todoRequest, 1L, 1L);


        @SuppressWarnings("unchecked")
        Map<String, Object> responseData = (Map<String, Object>) response.getData();
        assertEquals("success", responseData.get("status"));
        assertEquals("Todo updated successfully", responseData.get("message"));
        verify(todoRepository, times(1)).save(any(Todo.class));
    }


    @Test
    void updateTodoUnauthorizedAccess() {

        mockTodo.setUser(mockUser1);
        when(priorityRepository.findById(1)).thenReturn(Optional.of(mockPriority));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        assertThrows(UnauthorizedAccessException.class, () -> todoService.updateTodo(updateTodoRequest, 1L, 2L));


        verify(todoRepository, never()).save(any(Todo.class));
    }




    @Test
    void updateTodoNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        assertThrows(ResourceNotFoundException.class, () -> todoService.updateTodo(todoRequest, 1L, 1L));
        verify(todoRepository, never()).save(any(Todo.class));
    }


    @Test
    void testDeleteTodoByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        AppResponse<Object> response = todoService.deleteTodoById(1L, 1L);
        assertEquals("Todo deleted successfully", response.getData());
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTodoByIdUnauthorizedAccess() {
        mockTodo.setUser(mockUser1);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        assertThrows(UnauthorizedAccessException.class, () -> todoService.deleteTodoById(1L, 2L));
        verify(todoRepository, never()).deleteById(anyLong());
    }

}
