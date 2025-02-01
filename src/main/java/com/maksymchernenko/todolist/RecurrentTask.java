package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter
public class RecurrentTask extends Task {
    private Duration interval;

    public RecurrentTask(String title, String description, LocalDateTime deadline, boolean done) {
        super(title, description, deadline, done);
    }
}
