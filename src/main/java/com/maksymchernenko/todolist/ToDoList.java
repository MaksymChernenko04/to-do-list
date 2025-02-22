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

@Getter
public class ToDoList {
    private final List<Task> allTasks;
    @Setter
    private int lastTaskNumber;

    public ToDoList() {
        this.lastTaskNumber = 0;
        this.allTasks = new ArrayList<>();
    }

    public ToDoList(List<Task> taskList) { allTasks = new ArrayList<>(taskList); }

    public void addTask(Task task) {
        allTasks.add(task);
    }

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

    public void removeTask(int number) {
        allTasks.remove(getTask(number));
    }

    public Task getTask(int number) {
        for (Task task : allTasks) {
            if (task.getNumber() == number) {
                return task;
            }
        }

        throw new IllegalArgumentException("No task with number " + number + " in this list");
    }

    public List<Task> getCompletedTasks() {
        return allTasks.stream()
                .filter(Task::isDone)
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    public List<Task> getUncompletedTasks() {
        return allTasks.stream()
                .filter(task -> !task.isDone())
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

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
