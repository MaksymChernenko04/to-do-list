package com.maksymchernenko.todolist;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a to-do list that manages a collection of tasks.
 * <p>
 * This class provides methods to add, edit, remove, and retrieve different types of tasks,
 * as well as filter tasks based on their completion status or category.
 * </p>
 */
@Getter
public class ToDoList {
    /**
     * The collection of the tasks.
     */
    private final List<Task> allTasks;
    /**
     * Stores the number of the last added task.
     */
    @Setter
    private int lastTaskNumber;

    /**
     * Constructs an empty to-do list
     */
    public ToDoList() {
        this.lastTaskNumber = 0;
        this.allTasks = new ArrayList<>();
    }

    /**
     * Creates a to-do list with a predefined set of tasks.
     *
     * @param taskList predefined set of tasks
     */
    public ToDoList(List<Task> taskList) { allTasks = new ArrayList<>(taskList); }

    /**
     * Adds a new task to the to-do list.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        allTasks.add(task);
    }

    /**
     * Edits an existing task in the to-do list based on the provided updates.
     *
     * @param number the unique number of the task to edit
     * @param updates a map containing field names as keys and updated values as values
     * @throws IllegalArgumentException if the task with the given number is not found
     */
    public void editTask(int number, Map<String, String> updates) {
        Task task = getTask(number);
        if (updates.containsKey("title")) {
            task.setTitle(updates.get("title"));
        }
        if (updates.containsKey("description")) {
            task.setDescription(updates.get("description"));
        }
        if (updates.containsKey("deadline")) {
            task.setDeadline(LocalDateTime.parse(updates.get("deadline"), DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")));
        }
        if (updates.containsKey("done")) {
            task.setDone(Boolean.parseBoolean(updates.get("done")));
        }
        if (updates.containsKey("managerFirstName")) {
            ((WorkTask) task).getManager().setFirstName(updates.get("managerFirstName"));
        }
        if (updates.containsKey("managerLastName")) {
            ((WorkTask) task).getManager().setLastName(updates.get("managerLastName"));
        }
        if (updates.containsKey("managerEmail")) {
            ((WorkTask) task).getManager().setEmail(updates.get("managerEmail"));
        }
        if (updates.containsKey("tag")) {
            ((PersonalTask) task).setTag(Tag.fromString(updates.get("tag")));
        }
        if (updates.containsKey("frequency")) {
            Duration newInterval = Duration.ofHours(Long.parseLong(updates.get("frequency")));
            ((RecurrentTask) task).setInterval(newInterval);
            int count = 0;
            for (Task t : allTasks) {
                if (t instanceof RecurrentTask recurrentTask && recurrentTask.getTitle().equals(task.getTitle()) && recurrentTask.getDeadline().isAfter(task.getDeadline())) {
                    recurrentTask.setInterval(newInterval);
                    recurrentTask.setDeadline(task.getDeadline().plus(newInterval.multipliedBy(++count)));
                }
            }
        }
    }

    /**
     * Removes a task from the list.
     *
     * @param number the unique number of the task to remove
     * @throws IllegalArgumentException if the task with the given number is not found
     */
    public void removeTask(int number) {
        allTasks.remove(getTask(number));
    }

    /**
     * Retrieves a task from the list.
     *
     * @param number the unique number of the task to retrieve
     * @return the task associated with the given number
     * @throws IllegalArgumentException if the task with the given number is not found
     */
    public Task getTask(int number) {
        for (Task task : allTasks) {
            if (task.getNumber() == number) {
                return task;
            }
        }

        throw new IllegalArgumentException("No task with number " + number + " in this list");
    }

    /**
     * Retrieves all completed tasks from the list sorted by deadline.
     *
     * @return a list of completed tasks sorted by deadline
     */
    public List<Task> getCompletedTasks() {
        return allTasks.stream()
                .filter(Task::isDone)
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all uncompleted tasks from the list sorted by deadline.
     *
     * @return a list of uncompleted tasks sorted by deadline
     */
    public List<Task> getUncompletedTasks() {
        return allTasks.stream()
                .filter(task -> !task.isDone())
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all work tasks from the list sorted by manager.
     *
     * @return a list of work tasks sorted by manager
     */
    public List<Task> getWorkTasks() {
        List<WorkTask> workTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task instanceof WorkTask) {
                workTasks.add((WorkTask) task);
            }
        }

        return workTasks.stream()
                .sorted(Comparator.comparing(WorkTask::getManager))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all personal tasks from the list sorted by tag.
     *
     * @return a list of personal tasks sorted by tag
     */
    public List<Task> getPersonalTasks() {
        List<PersonalTask> personalTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task instanceof PersonalTask) {
                personalTasks.add((PersonalTask) task);
            }
        }

        return personalTasks.stream()
                .sorted(Comparator.comparing(PersonalTask::getTag))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all recurrent tasks from the list sorted by deadline.
     *
     * @return a list of recurrent tasks sorted by deadline
     */
    public List<Task> getRecurrentTasks() {
        List<RecurrentTask> recurrentTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task instanceof RecurrentTask) {
                recurrentTasks.add((RecurrentTask) task);
            }
        }

        return recurrentTasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }
}
