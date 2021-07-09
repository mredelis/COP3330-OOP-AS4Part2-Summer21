package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Task {
    SimpleStringProperty taskDescription;
    LocalDate dueDate;
    SimpleStringProperty status;

    public Task(String taskDescription, LocalDate dueDate, String status) {
        this.taskDescription = new SimpleStringProperty(taskDescription);
        this.status = new SimpleStringProperty(status);
        this.dueDate = dueDate;
    }


    public String getTaskDescription(){
        return taskDescription.get();
    }

    public void setTaskDescription(String taskDescription){
        this.taskDescription = new SimpleStringProperty(taskDescription);
    }

    public String getStatus(){
        return status.get();
    }

    public void setStatus(String status){
        this.status = new SimpleStringProperty(status);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String toString(){
        return getTaskDescription()+getDueDate()+getStatus();
    }

}
