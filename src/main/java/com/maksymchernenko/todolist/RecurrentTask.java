package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a recurrent task that repeats at a specified interval.
 * <p>
 * This class extends {@link Task} and introduces an additional property: {@link Duration},
 * which defines how often the task should be repeated.
 * </p>
 */
@Getter @Setter
public class RecurrentTask extends Task {
    /**
     * The interval at which the task repeats.
     */
    private Duration interval;

    /**
     * Default constructor.
     * <p>
     * Required for deserialization.
     * </p>
     */
    public RecurrentTask() {}

    /**
     * Constructs a new Recurrent task with the specified parameters.
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param deadline    deadline by which the task should be completed
     * @param done        whether the task is completed or not
     * @param interval    the interval at which the task repeats
     */
    public RecurrentTask(String title, String description, LocalDateTime deadline, boolean done, Duration interval) {
        super(title, description, deadline, done);
        this.interval = interval;
    }
}
