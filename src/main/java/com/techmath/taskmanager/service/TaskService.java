package com.techmath.taskmanager.service;

import com.techmath.taskmanager.dto.TaskRequest;
import com.techmath.taskmanager.dto.TaskResponse;
import com.techmath.taskmanager.exception.TaskNotFoundException;
import com.techmath.taskmanager.model.Task;
import com.techmath.taskmanager.model.TaskStatus;
import com.techmath.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(getValidatedStatus(request.getStatus()));

        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return new TaskResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(getValidatedStatus(request.getStatus()));

        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private TaskStatus getValidatedStatus(TaskStatus status) {
        return status != null ? status : TaskStatus.PENDING;
    }

}
