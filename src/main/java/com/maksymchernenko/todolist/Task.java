package com.maksymchernenko.todolist;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class Task {
    protected static int counter = 0;
    protected int number;
    protected String title;
    protected String description;
    protected LocalDateTime deadline;
    protected boolean done;

    public Task(String title, String description, LocalDateTime deadline, boolean done) {
        this.number = ++counter;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
    }
}
