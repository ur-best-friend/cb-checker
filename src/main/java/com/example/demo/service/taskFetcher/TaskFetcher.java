package com.example.demo.service.taskFetcher;

import com.example.demo.config.TaskConfig;
import com.example.demo.domain.Task;
import com.example.demo.domain.TaskCheckResult;
import com.example.demo.exceptions.TaskFetcherException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "task.fetcher-type", havingValue = "coinbase")
public class TaskFetcher implements ITaskFetcher {
    private final TaskConfig taskConfig;
    @Override
    public TaskCheckResult performCheck(Task task) throws TaskFetcherException, JsonProcessingException {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity(taskConfig.getCheckUrl(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        String currency = task.getCurrencyName();
        JsonNode rateNode = mapper.readTree(response.getBody()).path("bpi").path(currency).path("rate_float");
        if (!rateNode.isDouble()) throw new TaskFetcherException("Unable to extract rate_float for currency "+currency);
        return new TaskCheckResult(rateNode.asDouble());
    }
}
