package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a personal task that includes a category tag to specify its type.
 * <p>
 * This class extends {@link Task} and introduces an additional property: {@link Tag},
 * which categorizes the task.
 * </p>
 */
@Getter @Setter
public class PersonalTask extends Task {
    /**
     * The category assigned to this personal task.
     */
    private Tag tag;

    /**
     * Default constructor.
     * <p>
     * Required for deserialization.
     * </p>
     */
    public PersonalTask() {}

    /**
     * Constructs a new Personal task with the specified parameters.
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param deadline    deadline by which the task should be completed
     * @param done        whether the task is completed or not
     * @param tag         the category assigned to this task
     */
    public PersonalTask(String title, String description, LocalDateTime deadline, boolean done, Tag tag) {
        super(title, description, deadline, done);
        this.tag = tag;
    }
}
