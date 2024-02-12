package com.example.demo.service.taskFetcher;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskCheckResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@ConditionalOnProperty(name = "task.fetcher-type", havingValue = "mock")
public class MockTaskFetcher implements ITaskFetcher {

    @Override
    public TaskCheckResult performCheck(Task task) {
        return new TaskCheckResult(2000 * new Random().nextDouble());
    }
}
