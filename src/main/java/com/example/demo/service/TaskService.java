package com.example.demo.service;

import com.example.demo.config.TaskConfig;
import com.example.demo.domain.Task;
import com.example.demo.domain.TaskCheckResult;
import com.example.demo.dto.CreateTaskDto;
import com.example.demo.enums.TASK_STATUS;
import com.example.demo.service.taskFetcher.ITaskFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskConfig taskConfig;
    private final ITaskFetcher taskFetcher;
    private final TaskScheduler taskScheduler;
    private Map<String, Task> tasksMap = new ConcurrentHashMap<>();
    public Task getTaskById(String id) {
        return tasksMap.get(id);
    }

    public Task addNewTask(CreateTaskDto createTaskDto) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, createTaskDto.getCurrency());
        taskScheduler.schedule(() -> updateTask(task), Instant.now());
        tasksMap.put(id, task);
        return task;
    }

    private void updateTask(Task task) {
        try {
            TaskCheckResult checkResult = taskFetcher.performCheck(task);
            task.addCheckResult(checkResult);
            log.info("Successful "+checkResult+" for "+task);
            if (task.getTotalChecks() >= taskConfig.getMaxChecksCount()) {
                task.setStatus(TASK_STATUS.COMPLETE);
                cleanupTask(task);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            task.setStatus(TASK_STATUS.FAILED);
            cleanupTask(task);
            return;
        }
        taskScheduler.schedule(()->updateTask(task), Instant.now().plusMillis(taskConfig.getCheckIntervalMs()));
    }

    private void cleanupTask(Task task) {
        taskScheduler.schedule(()-> {  tasksMap.remove(task.getId()); }, Instant.now().plusSeconds(taskConfig.getCleanupDelayMin() * 60L) );
    }

    @Scheduled(fixedRate = 5000L)
    public void demoLogTasksGC() {
        log.info(String.format("[Before GC] Runtime memory usage: ~%s, Total tasks stored in memory: %d",
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
                tasksMap.size()));
        System.gc();
        log.info(String.format("[After GC]  Runtime memory usage: ~%s, Total tasks stored in memory: %d",
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
                tasksMap.size()));
    }
}
