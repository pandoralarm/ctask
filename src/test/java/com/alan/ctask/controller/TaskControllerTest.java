package com.alan.ctask.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alan.ctask.model.Task;
import com.alan.ctask.repository.TaskRepository;

class TaskControllerTest {
    // Test Unit Annotations
    @InjectMocks
    TaskController taskController;
    @Mock
    TaskRepository taskRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //



    @Test
    void testAddTask() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Contoh Task");

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        ResponseEntity<Task> response = taskController.addTask(task);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Contoh GetAll Task");
        tasks.add(task1);

        when(taskRepository.findAll()).thenReturn(tasks);
        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
    }

    // GetById
    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Contoh Task GETBYID");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        ResponseEntity<Task> response = taskController.getTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    // Update 1 Status
    @Test
    void testUpdateTaskStatus() {
        Task oldTask = new Task();
        oldTask.setId(1L);
        oldTask.setName("Task Pending");
        oldTask.setStatus("Pending");

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setName("Task Selesai");
        updatedTask.setStatus("Completed");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(oldTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        ResponseEntity<Task> response = taskController.updateTaskStatus(1L, updatedTask);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTask, response.getBody());
    }

    // GetByStatus
    @Test
    void testGetTaskByStatus() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task Get Pending");
        task1.setStatus("Pending");
        tasks.add(task1);

        when(taskRepository.findTaskByStatus("Pending")).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getTaskByStatus("Pending");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
    }

    // Delete 1 By Id
    @Test
    void testDeleteTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Task Delete 1");

        List<Task> remainingTasks = new ArrayList<>();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doAnswer(invocation -> {
            remainingTasks.remove(task);
            return null;
        }).when(taskRepository).deleteById(1L);
        when(taskRepository.findAll()).thenReturn(remainingTasks);
         ResponseEntity<List<Task>> response = taskController.deleteTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(remainingTasks, response.getBody());
    }


    // Delete All
    @Test
    void testDeleteAllTasks() {
        doNothing().when(taskRepository).deleteAll();

        ResponseEntity<HttpStatus> response = taskController.deleteAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Simulate Gagal Delete
    @Test
    void testDeleteNonExistentTaskById() {
         when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
         ResponseEntity<List<Task>> response = taskController.deleteTaskById(999L);
         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
