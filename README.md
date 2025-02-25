
# To Do List

A simple console application for managing tasks.

## Description

- There are three types of tasks:
    - **Work Task**: Includes a manager's details.
    - **Personal Task**: Can be categorized using a tag.
    - **Recurrent Task**: Supports periodic recurrence with a specified interval.
- Each task has:
    - A unique number.
    - A title and description.
    - A deadline.
    - A status indicator (completed or not).

## Features
- Add, edit, or remove tasks from the list.
- Display all existing tasks: completed, uncompleted, only work-related, only personal or only recurrent tasks.
- Data persistence through a JSON file.
- Log4j2 integration for logging actions.

## Installation
### Prerequisites
- Java 22+ installed
- Maven installed

### Clone the Repository
```
git clone https://github.com/MaksymChernenko04/to-do-list
cd todolist
```
### Build the Project
```
mvn clean install
```

### Run the Application
```
java -jar ToDoList-1.0-SNAPSHOT-jar-with-dependencies.jar
```
