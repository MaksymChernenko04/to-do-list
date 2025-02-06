package com.maksymchernenko.todolist;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ToDoList {
    private final List<Task> allTasks;

    public ToDoList() {
        this.allTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        allTasks.add(task);
    }

    public void editTask(int number) {


//        Task oldTask = getTask(number);
//        oldTask.setNumber(task.getNumber());
//        oldTask.setTitle(task.getTitle());
//        oldTask.setDescription(task.getDescription());
//        oldTask.setDeadline(task.getDeadline());
//        oldTask.setDone(task.isDone());
//
//        if (oldTask.getClass() == WorkTask.class && task.getClass() == WorkTask.class) {
//            WorkTask workTask = (WorkTask) oldTask;
//            workTask.setManager(((WorkTask) task).getManager());
//        }
//
//        if (oldTask.getClass() == RecurrentTask.class && task.getClass() == RecurrentTask.class) {
//            RecurrentTask recurrentTask = (RecurrentTask) oldTask;
//            recurrentTask.setInterval(((RecurrentTask) task).getInterval());
//        }
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
