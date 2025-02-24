package com.maksymchernenko.todolist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a manager who is responsible for overseeing a work task.
 * This class includes basic information such as first name, last name, and email.
 * Implements {@link Comparable} to allow sorting by last name.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager implements Comparable<Manager> {
    /**
     * The first name of the manager.
     */
    private String firstName;
    /**
     * The last name of the manager.
     */
    private String lastName;
    /**
     * The email of the manager.
     */
    private String email;

    /**
     * Compares this manager with another manager based on the last name.
     *
     * @param o the other manager to compare with
     * @return a negative integer, zero, or a positive integer as this manager's last name
     * is less than, equal to, or greater than the specified manager's last name
     */
    @Override
    public int compareTo(Manager o) {
        return lastName.compareTo(o.lastName);
    }
}
