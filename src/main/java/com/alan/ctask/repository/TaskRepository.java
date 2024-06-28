package com.alan.ctask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alan.ctask.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
 
    List<Task> findTaskByStatus(String status);
}
