package com.savindu.Todo.Application.repository;

import com.savindu.Todo.Application.entity.Priority;
import com.savindu.Todo.Application.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@EnableJpaRepositories
public interface PriorityRepository extends JpaRepository<Priority, Integer> {
	


}
