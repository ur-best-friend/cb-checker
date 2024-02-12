package com.example.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
@Getter
public class TaskConfig {
    @Value("${task.max-checks}")
    private int maxChecksCount;
    @Value("${task.check-url}")
    private String checkUrl;
    @Value("${task.cleanup-delay-min}")
    private int cleanupDelayMin;
    @Value("${task.check-interval-ms}")
    private int checkIntervalMs;
}
