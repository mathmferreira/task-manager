package com.techmath.taskmanager.controller;

import com.techmath.taskmanager.model.Task;
import com.techmath.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("PENDING");
        }

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id).get();
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id).get();

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());

        if (taskDetails.getStatus() == null || taskDetails.getStatus().isEmpty()) {
            task.setStatus("PENDING");
        } else {
            task.setStatus(taskDetails.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
