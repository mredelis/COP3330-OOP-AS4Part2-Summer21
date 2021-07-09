package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Item {
    SimpleStringProperty itemDescription;
    LocalDate dueDate;
    SimpleStringProperty status;

    public Item(String itemDescription, LocalDate dueDate, String status) {
        this.itemDescription = new SimpleStringProperty(itemDescription);
        this.status = new SimpleStringProperty(status);
        this.dueDate = dueDate;
    }

    /*
     Getter and Setters
     */

    // Item Description
    public String getItemDescription(){
        return itemDescription.get();
    }

    public void setItemDescription(String itemDescription){
        this.itemDescription = new SimpleStringProperty(itemDescription);
    }

    // Status (Complete or Incomplete)
    public String getStatus(){
        return status.get();
    }

    public void setStatus(String status){
        this.status = new SimpleStringProperty(status);
    }

    // Due date (DatePicker)
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
