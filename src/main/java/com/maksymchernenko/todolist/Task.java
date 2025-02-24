package com.maksymchernenko.todolist;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a base class for different types of tasks.
 * <p>
 * This abstract class defines common properties and behavior for all task types,
 * including {@link WorkTask}, {@link PersonalTask}, and {@link RecurrentTask}.
 * </p>
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WorkTask.class, name = "work"),
        @JsonSubTypes.Type(value = PersonalTask.class, name = "personal"),
        @JsonSubTypes.Type(value = RecurrentTask.class, name = "recurrent")})
public abstract class Task {
    /**
     * Counter for tracking the number of created tasks.
     * <p>
     * Used to generate a unique identifier for each task.
     * <b>Note:</b> This counter is not thread-safe.
     * </p>
     */
    protected static int counter = 0;
    /**
     * The unique identifier of the task.
     */
    protected int number;
    /**
     * The title of the task.
     */
    protected String title;
    /**
     * The description of the task.
     */
    protected String description;
    /**
     * The deadline by which the task should be completed.
     */
    protected LocalDateTime deadline;
    /**
     * Indicates whether the task is completed.
     */
    protected boolean done;

    /**
     * Default constructor.
     * <p>
     * Required for deserialization.
     * </p>
     */
    public Task() {}

    /**
     * Constructs a new Task with the specified parameters.
     * <p>
     * The task number is assigned automatically based on the {@code counter}.
     * </p>
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param deadline    the deadline by which the task should be completed
     * @param done        whether the task is completed or not
     */
    public Task(String title, String description, LocalDateTime deadline, boolean done) {
        this.number = ++counter;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
    }
}
