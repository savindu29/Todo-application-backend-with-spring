package com.savindu.Todo.Application.repository;

import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
