package com.maksymchernenko.todolist;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ToDoListApp {
    private static final Logger logger = LogManager.getLogger(ToDoListApp.class);
    private static final String FILE_NAME = "toDoList.json";
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) {
        logger.info("ToDoList application started");

        ToDoList toDoList = new ToDoList();
        try {
            File file = new File(FILE_NAME);
            if (file.exists() && file.length() > 0) {
                toDoList = objectMapper.readValue(file, ToDoList.class);
            }

            Task.counter = toDoList.getLastTaskNumber();

            logger.info("To Do List successfully loaded from the file {}", FILE_NAME);
        } catch (Exception e) {
            logger.error("Error loading To Do List from the file {}", FILE_NAME, e);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(
                    """
                            ------------------------
                            ToDoList - Main menu
                            ------------------------
                            1 Add new task
                            2 Delete task
                            3 Edit task
                            4 View all tasks
                            5 View uncompleted tasks
                            6 View completed tasks
                            7 View work tasks
                            8 View personal tasks
                            9 View recurrent tasks
                            ------------------------
                            0 Save and exit program
                            ------------------------
                            Enter your choice (number):"""
            );

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                logger.info("User made a choice in the main menu: {}", choice);
            } catch (Exception e) {
                logger.error("Unexpected choice in the main menu: {}", choice, e);
            }

            switch (choice) {
                case 0 -> {
                    try {
                        toDoList.setLastTaskNumber(Task.counter);
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), toDoList);
                        logger.info("To Do List successfully saved to the file {}", FILE_NAME);
                    } catch (Exception e) {
                        logger.error("Error saving To Do List to the file {}", FILE_NAME, e);
                    }

                    logger.info("Exiting ToDoList application");
                    System.exit(0);
                }
                case 1 -> {
                    System.out.println("Enter task title:");
                    String title = scanner.nextLine();
                    System.out.println("Enter task description:");
                    String description = scanner.nextLine();
                    System.out.println("Enter task deadline as HH:mm yyyy-MM-dd:");
                    String deadline = scanner.nextLine();
                    System.out.println("Is it a work, personal or recurrent task?");
                    String type = scanner.nextLine();

                    logger.info("Adding new task: Title={}, Type={}", title, type);

                    switch (type) {
                        case "work" -> {
                            System.out.println("Enter task manager first name:");
                            String managerFirstName = scanner.nextLine();
                            System.out.println("Enter task manager last name:");
                            String managerLastName = scanner.nextLine();
                            System.out.println("Enter task manager email:");
                            String managerEmail = scanner.nextLine();

                            try {
                                toDoList.addTask(new WorkTask(
                                        title,
                                        description,
                                        LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")),
                                        false,
                                        new Manager(managerFirstName, managerLastName, managerEmail))
                                );

                                logger.info("New work task successfully added to the list");
                            } catch (Exception e) {
                                logger.error("Error adding task to the list", e);
                            }
                        }

                        case "personal" -> {
                            System.out.println("Enter task tag:");
                            String tag = scanner.nextLine();

                            try {
                                toDoList.addTask(new PersonalTask(
                                        title,
                                        description,
                                        LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")),
                                        false,
                                        Tag.fromString(tag))
                                );

                                logger.info("New personal task successfully added to the list");
                            } catch (Exception e) {
                                logger.error("Error adding task to the list", e);
                            }
                        }

                        case "recurrent" -> {
                            System.out.println("Enter task frequency in hours:");
                            int frequency = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Enter number of tasks:");
                            int number = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                for (int i = 0; i < number; i++) {
                                    toDoList.addTask(new RecurrentTask(title,
                                            description,
                                            LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")).plusHours((long) frequency * i),
                                            false,
                                            Duration.ofHours(frequency)));
                                }

                                logger.info("All recurrent tasks successfully added to the list");
                            } catch (Exception e) {
                                logger.error("Error adding Recurrent tasks to the list", e);
                            }
                        }

                        default -> logger.error("Unexpected task type: " + type);
                    }
                }
                case 2 -> {
                    System.out.println("Enter task number:");
                    int number = 0;
                    try {
                        number = scanner.nextInt();
                        scanner.nextLine();

                        logger.info("Removing task #{}", number);

                        toDoList.removeTask(number);

                        logger.info("Task #{} successfully removed from the list", number);
                    } catch (Exception e) {
                        logger.error("Error removing task{} from the list", number, e);
                    }
                }
                case 3 -> {
                    System.out.println("Enter task number:");
                    int number = scanner.nextInt();
                    scanner.nextLine();

                    logger.info("Editing task #{}", number);

                    Task task = toDoList.getTask(number);
                    System.out.println(
                            "------------------------\n" +
                            "Task editor");
                    System.out.printf("%-10s %-" + (task.getTitle().length() + 5) + "s %-" + (task.getDescription().length() + 5) + "s %-20s %-10s %-10s %n", "Number", "Title", "Description", "Deadline", "Done", "Addition");
                    printTask(task, task.getTitle().length(), task.getDescription().length());
                    System.out.print(
                            """
                            ------------------------
                            1 Edit title
                            2 Edit description
                            3 Edit deadline
                            4 Mark as done
                            """);

                    switch (task) {
                        case WorkTask ignored -> System.out.println(
                                """
                                5 Edit manager first name
                                6 Edit manager last name
                                7 Edit manager email
                                """);
                        case PersonalTask ignored -> System.out.println("5 Edit tag");
                        case RecurrentTask ignored -> System.out.println("5 Edit frequency");
                        default -> logger.error("Unexpected task value: {}", task);
                    }

                    System.out.print(
                            """
                            ------------------------
                            0 Return to main menu
                            ------------------------
                            Enter your choice (number):
                            """);

                    choice = scanner.nextInt();
                    scanner.nextLine();
                    logger.info("User made a choice in the editing menu: {}", choice);

                    Map<String, String> updates = new HashMap<>();
                    try {
                        if (choice != 0) {
                            switch (choice) {
                                case 1 -> {
                                    System.out.println("Enter new task title:");
                                    String title = scanner.nextLine();
                                    updates.put("title", title.trim());
                                }
                                case 2 -> {
                                    System.out.println("Enter new task description:");
                                    String description = scanner.nextLine();
                                    updates.put("description", description.trim());
                                }
                                case 3 -> {
                                    System.out.println("Enter new task deadline as HH:mm yyyy-MM-dd:");
                                    String deadline = scanner.nextLine();
                                    updates.put("deadline", deadline.trim());
                                }
                                case 4 -> updates.put("done", "true");
                                case 5 -> {
                                    switch (task) {
                                        case WorkTask ignored -> {
                                            System.out.println("Enter new task manager first name:");
                                            String managerFirstName = scanner.nextLine();
                                            updates.put("managerFirstName", managerFirstName.trim());
                                        }
                                        case PersonalTask ignored -> {
                                            System.out.println("Enter new task tag:");
                                            String tag = scanner.nextLine();
                                            updates.put("tag", tag.trim());
                                        }
                                        case RecurrentTask ignored -> {
                                            System.out.println("Enter new task frequency:");
                                            String frequency = scanner.nextLine();
                                            updates.put("frequency", frequency.trim());
                                        }
                                        default -> logger.error("Unexpected task value for edit: {}", task);
                                    }
                                }
                                case 6 -> {
                                    System.out.println("Enter new task manager last name:");
                                    String managerLastName = scanner.nextLine();
                                    updates.put("managerLastName", managerLastName.trim());
                                }
                                case 7 -> {
                                    System.out.println("Enter new task manager email:");
                                    String managerEmail = scanner.nextLine();
                                    updates.put("managerEmail", managerEmail.trim());
                                }
                                default -> logger.error("Unexpected choice while editing: {}", choice);
                            }
                        }

                        toDoList.editTask(number, updates);
                        logger.info("Task #{} successfully edited", number);
                    } catch (Exception e) {
                        logger.error("Error editing task #{}", number, e);
                    }
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
                default -> logger.error("Unexpected choice: {}", choice);
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
            default -> logger.error("Unexpected value: " + task);
        }
    }
}
