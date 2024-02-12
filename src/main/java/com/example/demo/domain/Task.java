package com.example.demo.domain;

import com.example.demo.enums.TASK_STATUS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log
@JsonSerialize(as = Task.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT) // NON_ABSENT ignores Optional.empty(), unlike NON_NULL
public class Task {
    public Task(String id, String currencyName) {
        this.id = id;
        this.currencyName = currencyName;
    }
    @Getter
    private final String id;
    @Getter
    @JsonIgnore
    private final String currencyName;
    @Getter
    @Setter
    private TASK_STATUS status = TASK_STATUS.PROCESSING;
    /** Result won't be available until task is completed */
    public Optional<Double> getResult() {
        if (status != TASK_STATUS.COMPLETE) return Optional.empty();
        double avg = checkResults.stream().mapToDouble(TaskCheckResult::bpiValue).sum() / checkResults.size();
        return Optional.of(Math.round(avg *10000.0)/10000.0);
    }
    private final List<TaskCheckResult> checkResults = new LinkedList<>();
    public void addCheckResult(TaskCheckResult checkResult) { checkResults.add(checkResult); }
    @JsonIgnore
    public int getTotalChecks() { return checkResults.size(); }

    @Override
    protected void finalize() throws Throwable {
        log.info("Finalizing "+this);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", totalChecks=" + getTotalChecks() +
                ", currencyName=" + getCurrencyName() +
                getResult().map((v)->", avgBpi=" +v).orElse("")+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id) && Objects.equals(currencyName, task.currencyName) && status == task.status && Objects.equals(checkResults, task.checkResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyName, status, checkResults);
    }
}
