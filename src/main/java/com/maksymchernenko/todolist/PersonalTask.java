package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PersonalTask extends Task {
    private Tag tag;

    public PersonalTask() {}

    public PersonalTask(String title, String description, LocalDateTime deadline, boolean done, Tag tag) {
        super(title, description, deadline, done);
        this.tag = tag;
    }
}
