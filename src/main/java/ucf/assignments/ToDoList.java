package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class ToDoList {

    private SimpleStringProperty ToDoListName;
    private ObservableList<Item> ToDoListItems;

    public ToDoList(String ToDoListName, ObservableList<Item> ToDoListItems) {
        this.ToDoListName = new SimpleStringProperty(ToDoListName);
        this.ToDoListItems = ToDoListItems;
    }

    public String getToDoListName() {
        return ToDoListName.get();
    }

    public void setToDoListName(String ToDoListName) {
        this.ToDoListName = new SimpleStringProperty(ToDoListName);
    }

    public ObservableList<Item> getToDoListItems(){
        return ToDoListItems;
    }

    public void setToDoListItems(ObservableList<Item> ToDoListItems){
        this.ToDoListItems = ToDoListItems;
    }


}

