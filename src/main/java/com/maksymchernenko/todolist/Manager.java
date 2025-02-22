package com.maksymchernenko.todolist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager implements Comparable<Manager> {
    private String firstName;
    private String lastName;
    private String email;

    @Override
    public int compareTo(Manager o) {
        return lastName.compareTo(o.lastName);
    }
}
