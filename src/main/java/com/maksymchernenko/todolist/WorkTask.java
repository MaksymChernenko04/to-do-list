package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class WorkTask extends Task {

    private Manager manager;

    public WorkTask(String title, String description, LocalDateTime deadline, boolean done, Manager manager) {
        super(title, description, deadline, done);
        this.manager = manager;
    }
}
