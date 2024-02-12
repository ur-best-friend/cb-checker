package com.example.demo.service.taskFetcher;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskCheckResult;
import com.example.demo.exceptions.TaskFetcherException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ITaskFetcher {
    TaskCheckResult performCheck(Task task) throws JsonProcessingException, TaskFetcherException;
}
