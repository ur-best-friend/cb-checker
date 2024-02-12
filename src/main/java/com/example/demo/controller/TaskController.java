package com.example.demo.controller;

import com.example.demo.domain.Task;
import com.example.demo.dto.CreateTaskDto;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/task/{taskId}")
    public Task getTaskStatus(@PathVariable(name = "taskId") String taskId) {
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task with id wasn't found");
        return task;
    }

    @PostMapping(value = "/task")
    public Task postTaskStatus(@RequestBody CreateTaskDto createTaskDto) {
        return this.taskService.addNewTask(createTaskDto);
    }
}
