package com.example.fypapplication1.Models;

public class ToDo {
   //Variables
    public String taskTitle, taskDescription, taskDeadline, id;

    //Empty Constructor
    public ToDo() {

    }

    //Constructor
    public ToDo(String taskTitle, String taskDescription, String taskDeadline) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.id = id;
    }

    //Getter & Setter Methods
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
