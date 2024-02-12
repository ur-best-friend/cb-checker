package com.example.demo.exceptions;

public class TaskFetcherException extends Exception {
    public TaskFetcherException(String reason){
        super(reason);
    }
}
