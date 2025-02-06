package com.maksymchernenko.todolist;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ToDoListApp {
    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "------------------------\n" +
                    "ToDoList - Main menu\n" +
                    "------------------------\n" +
                    "1 Add new task\n" +
                    "2 Delete task\n" +
                    "3 Edit task\n" +
                    "4 View all tasks\n" +
                    "5 View uncompleted tasks\n" +
                    "6 View completed tasks\n" +
                    "7 View work tasks\n" +
                    "8 View personal tasks\n" +
                    "9 View recurrent tasks\n" +
                    "------------------------\n" +
                    "0 Exit program\n" +
                    "------------------------\n" +
                    "Enter your choice (number):"
            );

            int choice = scanner.nextInt();
            switch (choice) {
                case 0 -> System.exit(0);
                case 1 -> {
                    System.out.println("Enter task title:");
                    String title = scanner.next();
                    scanner.nextLine();
                    System.out.println("Enter task description:");
                    String description = scanner.nextLine();
                    System.out.println("Enter task deadline as HH:mm yyyy-MM-dd:");
                    String deadline = scanner.nextLine();
                    System.out.println("Is it a work, personal or recurrent task?");
                    String type = scanner.next();

                    switch (type) {
                        case "work" -> {
                            System.out.println("Enter task manager first name:");
                            String managerFirstName = scanner.next();
                            System.out.println("Enter task manager last name:");
                            String managerLastName = scanner.next();
                            System.out.println("Enter task manager email:");
                            String managerEmail = scanner.next();

                            toDoList.addTask(new WorkTask(
                                    title,
                                    description,
                                    LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")),
                                    false,
                                    new Manager(managerFirstName, managerLastName, managerEmail))
                            );
                        }

                        case "personal" -> {
                            System.out.println("Enter task tag:");
                            String tag = scanner.next();

                            toDoList.addTask(new PersonalTask(
                                    title,
                                    description,
                                    LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")),
                                    false,
                                    Tag.fromString(tag))
                            );
                        }

                        case "recurrent" -> {
                            System.out.println("Enter task frequency in hours:");
                            int frequency = scanner.nextInt();
                            System.out.println("Enter number of tasks:");
                            int number = scanner.nextInt();

                            for (int i = 0; i < number; i++) {
                                toDoList.addTask(new RecurrentTask(title,
                                        description,
                                        LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")).plusHours((long) frequency * i),
                                        false,
                                        Duration.ofHours(frequency)));
                            }
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Enter task number:");
                    int number = scanner.nextInt();

                    toDoList.removeTask(number);
                }
                case 3 -> {
                    System.out.println("Enter task number:");
                    int number = scanner.nextInt();
                    Task task = toDoList.getTask(number);
                    System.out.println(
                            "------------------------\n" +
                                    "Task editor\n" +
                                    task.toString() + "\n" +
                                    "------------------------\n" +
                                    "1 Edit title\n" +
                                    "2 Edit description\n" +
                                    "3 Edit deadline\n" +
                                    "4 Mark as done");

                    switch (task) {
                        case WorkTask _ -> System.out.println(
                                "5 Edit manager first name\n" +
                                "6 Edit manager last name\n" +
                                "7 Edit manager email");
                        case PersonalTask _ -> System.out.println("5 Edit tag");
                        case RecurrentTask _ -> System.out.println("5 Edit frequency");
                        default -> throw new IllegalStateException("Unexpected value: " + task);
                    }

                    System.out.println("------------------------\n" +
                            "0 Return to main menu\n" +
                            "------------------------\n" +
                            "Enter your choice (number):");


                }
                case 4 -> {
                    List<Task> tasks = toDoList.getAllTasks();
                    printToDoList(tasks);
                }
                case 5 -> {
                    List<Task> tasks = toDoList.getUncompletedTasks();
                    printToDoList(tasks);
                }
                case 6 -> {
                    List<Task> tasks = toDoList.getCompletedTasks();
                    printToDoList(tasks);
                }
                case 7 -> {
                    List<Task> tasks = toDoList.getWorkTasks();
                    printToDoList(tasks);
                }
                case 8 -> {
                    List<Task> tasks = toDoList.getPersonalTasks();
                    printToDoList(tasks);
                }
                case 9 -> {
                    List<Task> tasks = toDoList.getRecurrentTasks();
                    printToDoList(tasks);
                }
            }
        }
    }

    static void printToDoList(List<Task> tasks) {
        System.out.println("Number\tTitle\tDescription\tDeadline\tDone\tAddition");
        for (Task task : tasks) {
            System.out.print(task.getNumber() + "\t" + task.getTitle() + "\t" + task.getDescription() + "\t" + task.getDeadline().format(DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")) + "\t" + (task.isDone() ? "Yes" : "No") + "\t");
            switch (task) {
                case WorkTask workTask -> {
                    Manager manager = workTask.getManager();
                    System.out.println("Manager: " + manager.getFirstName() + " " + manager.getLastName() + ", " + manager.getEmail());
                }
                case PersonalTask personalTask -> System.out.println("Tag: " + personalTask.getTag());
                case RecurrentTask recurrentTask -> System.out.println("Interval (h): " + recurrentTask.getInterval().toHours());
                default -> throw new IllegalStateException("Unexpected value: " + task);
            }
        }
    }
}
