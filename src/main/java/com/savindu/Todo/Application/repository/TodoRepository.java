package com.savindu.Todo.Application.repository;

import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@EnableJpaRepositories
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE " +
            "(:taskStatus IS NULL OR t.status = :taskStatus) AND " +
            "(:taskPiority IS NULL OR t.priority.id = :taskPiority) AND " +
            "(:taskId IS NULL OR t.id = :taskId) AND " +
            "(:startOfDayTaskFrom IS NULL OR t.createdAt >= :startOfDayTaskFrom) AND " +
            "(:endOfDayTaskTo IS NULL OR t.createdAt <= :endOfDayTaskTo) AND " +
            "(:startOfDayTaskDue IS NULL OR t.dueDate >= :startOfDayTaskDue) AND " +
            "(:endOfDayTaskDue IS NULL OR t.dueDate <= :endOfDayTaskDue) AND " +
            "(:userId IS NULL OR t.user.id = :userId)")
    Page<Todo> findAllTodoTask(Integer taskStatus, Integer taskPiority, Integer taskId, LocalDateTime startOfDayTaskFrom, LocalDateTime endOfDayTaskTo,
                               LocalDate startOfDayTaskDue, LocalDate endOfDayTaskDue, Long userId, Pageable pageable);

}
