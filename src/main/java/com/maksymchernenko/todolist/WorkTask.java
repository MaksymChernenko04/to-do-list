package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a work-related task.
 * <p>
 * This class extends {@link Task} and introduces an additional property: {@link Manager},
 * who is responsible for overseeing the task.
 * </p>
 */
@Getter @Setter
public class WorkTask extends Task {
    /**
     * The manager responsible for overseeing this work task.
     */
    private Manager manager;

    /**
     * Default constructor.
     * <p>
     * Required for deserialization.
     * </p>
     */
    public WorkTask() {}

    /**
     * Constructs a new Work task with the specified parameters.
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param deadline    the deadline by which the task should be completed
     * @param done        whether the task is completed or not
     * @param manager     the manager who is responsible for overseeing a task
     */
    public WorkTask(String title, String description, LocalDateTime deadline, boolean done, Manager manager) {
        super(title, description, deadline, done);
        this.manager = manager;
    }
}
