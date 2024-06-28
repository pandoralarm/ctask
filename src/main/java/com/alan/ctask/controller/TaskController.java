package com.alan.ctask.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alan.ctask.model.Task;
import com.alan.ctask.repository.TaskRepository;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/addTask")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        try {
            Task taskObj = taskRepository.save(task);
            return new ResponseEntity<>(taskObj, HttpStatus.CREATED);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> taskList = taskRepository.findAll();

        if (taskList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }
 

    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()){
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/updateTaskStatus/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody Task newTaskData){
        Optional<Task> oldTaskData = taskRepository.findById(id);

        if (oldTaskData.isPresent()){
            Task updatedTaskData = oldTaskData.get();
            updatedTaskData.setStatus(newTaskData.getStatus());

            Task taskObj = taskRepository.save(updatedTaskData);
            return new ResponseEntity<>(taskObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getTaskByStatus/{status}")
    public ResponseEntity<List<Task>> getTaskByStatus(@PathVariable String status){
        try {
            List<Task> taskByStatus = taskRepository.findTaskByStatus(status);

            if (taskByStatus.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(taskByStatus, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
 
    @DeleteMapping("/deleteTaskById")
    public ResponseEntity<List<Task>> deleteTaskById(@RequestParam Long id){
        try {
            taskRepository.deleteById(id);

            List<Task> remainingTasks = taskRepository.findAll();

            return new ResponseEntity<>(remainingTasks, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAllTasks")
    public ResponseEntity<HttpStatus> deleteAllTasks(){
        try {
            taskRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
