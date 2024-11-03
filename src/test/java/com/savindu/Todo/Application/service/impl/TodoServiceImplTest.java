//package com.savindu.Todo.Application.service.impl;
//
//import com.savindu.Todo.Application.Exception.ResourceNotFoundException;
//import com.savindu.Todo.Application.dto.request.TodoRequest;
//import com.savindu.Todo.Application.entity.AppUser;
//import com.savindu.Todo.Application.entity.Todo;
//import com.savindu.Todo.Application.repository.TodoRepository;
//import com.savindu.Todo.Application.repository.UserRepository;
//import com.savindu.Todo.Application.service.TodoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TodoServiceImplTest {
//
//    @Mock
//    private TodoRepository todoRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private TodoServiceImpl todoService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    void createTodoSuccessfully() {
////        // Arrange
////        TodoRequest todoRequest = new TodoRequest();
////        todoRequest.setTitle("Test Title");
////        todoRequest.setDescription("Test Description");
////        Long userId = 1L;
////        AppUser appUser = userRepository.findById(userId).get();
////
////
////
////        when(userRepository.findById(userId)).thenReturn(Optional.of(appUser));
////        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
////
////        // Act
////        String result = todoService.createTodo(todoRequest, userId);
////
////        // Assert
////        assertEquals("Todo created successfully with ID: 1", result);
////        verify(userRepository, times(1)).findById(userId);
////        verify(todoRepository, times(1)).save(any(Todo.class));
////    }
//
//    @Test
//    void createTodoThrowsResourceNotFoundException() {
//        // Arrange
//        TodoRequest todoRequest = new TodoRequest();
//        todoRequest.setTitle("Test Title");
//        todoRequest.setDescription("Test Description");
//        Long userId = 1L;
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResourceNotFoundException exception = assertThrows(
//                ResourceNotFoundException.class,
//                () -> todoService.createTodo(todoRequest, userId)
//        );
//
//        assertEquals("User not found with id: " + userId, exception.getMessage());
//        verify(userRepository, times(1)).findById(userId);
//        verify(todoRepository, never()).save(any(Todo.class));
//    }
//
//    @Test
//    void createTodoHandlesDatabaseError() {
//        // Arrange
//        TodoRequest todoRequest = new TodoRequest();
//        todoRequest.setTitle("Test Title");
//        todoRequest.setDescription("Test Description");
//        Long userId = 1L;
//
//        AppUser appUser = new AppUser();
//        appUser.setId(userId);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(appUser));
//        when(todoRepository.save(any(Todo.class))).thenThrow(new RuntimeException("Database error"));
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> todoService.createTodo(todoRequest, userId));
//        assertEquals("Database error", exception.getMessage());
//        verify(userRepository, times(1)).findById(userId);
//        verify(todoRepository, times(1)).save(any(Todo.class));
//    }
//}
