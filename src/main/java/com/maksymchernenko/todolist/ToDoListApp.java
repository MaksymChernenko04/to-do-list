package com.maksymchernenko.todolist;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                            "Task editor\n");
                    System.out.printf("%-10s %-" + (task.getTitle().length() + 5) + "s %-" + (task.getDescription().length() + 5) + "s %-20s %-10s %-10s %n", "Number", "Title", "Description", "Deadline", "Done", "Addition");
                    printTask(task, task.getTitle().length(), task.getDescription().length());
                    System.out.println("------------------------\n" +
                            "1 Edit title\n" +
                            "2 Edit description\n" +
                            "3 Edit deadline\n" +
                            "4 Mark as done");

                    switch (task) {
                        case WorkTask workTask -> System.out.println(
                                "5 Edit manager first name\n" +
                                "6 Edit manager last name\n" +
                                "7 Edit manager email");
                        case PersonalTask personalTask -> System.out.println("5 Edit tag");
                        case RecurrentTask recurrentTask -> System.out.println("5 Edit frequency");
                        default -> throw new IllegalStateException("Unexpected task value: " + task);
                    }

                    System.out.println("------------------------\n" +
                            "0 Return to main menu\n" +
                            "------------------------\n" +
                            "Enter your choice (number):");

                    choice = scanner.nextInt();
                    Map<String, String> updates = new HashMap<>();
                    if (choice != 0) {
                        switch (choice) {
                            case 1 -> {
                                System.out.println("Enter new task title:");
                                String title = scanner.next();
                                updates.put("title", title.trim());
                            }
                            case 2 -> {
                                System.out.println("Enter new task description:");
                                String description = scanner.next();
                                updates.put("description", description.trim());
                            }
                            case 3 -> {
                                System.out.println("Enter new task deadline as HH:mm yyyy-MM-dd:");
                                String deadline = scanner.next();
                                updates.put("deadline", deadline.trim());
                            }
                            case 4 -> updates.put("done", "true");
                            case 5 -> {
                                switch (task) {
                                    case WorkTask workTask -> {
                                        System.out.println("Enter new task manager first name:");
                                        String managerFirstName = scanner.next();
                                        updates.put("managerFirstName", managerFirstName.trim());
                                    }
                                    case PersonalTask personalTask -> {
                                        System.out.println("Enter new task tag:");
                                        String tag = scanner.next();
                                        updates.put("tag", tag.trim());
                                    }
                                    case RecurrentTask recurrentTask -> {
                                        System.out.println("Enter new task frequency:");
                                        String frequency = scanner.next();
                                        updates.put("frequency", frequency.trim());
                                    }
                                    default -> throw new IllegalStateException("Unexpected task value: " + task);
                                }
                            }
                            case 6 -> {
                                System.out.println("Enter new task manager last name:");
                                String managerLastName = scanner.next();
                                updates.put("managerLastName", managerLastName.trim());
                            }
                            case 7 -> {
                                System.out.println("Enter new task manager email:");
                                String managerEmail = scanner.next();
                                updates.put("managerEmail", managerEmail.trim());
                            }
                            default -> throw new IllegalStateException("Unexpected choice: " + choice);
                        }
                    }

                    toDoList.editTask(number, updates);
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
                default -> throw new IllegalStateException("Unexpected choice: " + choice);
            }
        }
    }

    static void printToDoList(List<Task> tasks) {
        int maxDescriptionLength = tasks.stream()
                .mapToInt(task -> task.getDescription().length())
                .max()
                .orElse(0);

        int maxTitleLength = tasks.stream()
                .mapToInt(task -> task.getTitle().length())
                .max()
                .orElse(0);

        if (tasks.isEmpty()) {
            System.out.println("No tasks in the list");
        } else {
            System.out.printf("%-10s %-" + (maxTitleLength + 5) + "s %-" + (maxDescriptionLength + 5) + "s %-20s %-10s %-10s %n", "Number", "Title", "Description", "Deadline", "Done", "Addition");

            for (Task task : tasks) {
                printTask(task, maxTitleLength, maxDescriptionLength);
            }
        }
    }

    static void printTask(Task task, int maxTitleLength, int maxDescriptionLength) {
        System.out.printf("%-10s %-" + (maxTitleLength + 5) + "s %-" + (maxDescriptionLength + 5) + "s %-20s %-10s",
                task.getNumber(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline().format(DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")),
                (task.isDone() ? "Yes" : "No"));

        switch (task) {
            case WorkTask workTask -> {
                Manager manager = workTask.getManager();
                System.out.printf("%-10s %n", "Manager: " + manager.getFirstName() + " " + manager.getLastName() + ", " + manager.getEmail());
            }
            case PersonalTask personalTask -> System.out.printf("%-10s %n", "Tag: " + personalTask.getTag());
            case RecurrentTask recurrentTask -> System.out.printf("%-10s %n", "Interval (h): " + recurrentTask.getInterval().toHours());
            default -> throw new IllegalStateException("Unexpected value: " + task);
        }
    }
}
