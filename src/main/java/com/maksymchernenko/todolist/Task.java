package com.maksymchernenko.todolist;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WorkTask.class, name = "work"),
        @JsonSubTypes.Type(value = PersonalTask.class, name = "personal"),
        @JsonSubTypes.Type(value = RecurrentTask.class, name = "recurrent")
})
public abstract class Task {
    protected static int counter = 0;
    protected int number;
    protected String title;
    protected String description;
    protected LocalDateTime deadline;
    protected boolean done;

    public Task() {}

    public Task(String title, String description, LocalDateTime deadline, boolean done) {
        this.number = ++counter;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
    }
}
