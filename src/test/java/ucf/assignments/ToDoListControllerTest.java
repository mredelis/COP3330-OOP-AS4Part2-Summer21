package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

class ToDoListControllerTest {
    ToDoListController controller;

    @BeforeEach
    void setUp() {
        controller = new ToDoListController();
    }

//    @Test
//    void loadFile() {
//        ToDoListController controller = new ToDoListController();
//
//        ObservableList<Task> expResult = FXCollections.observableArrayList();
//        expResult.add(new Task("Buy round trip flight tickets to Lima", LocalDate.of(2021, Month.JULY, 3), "Completed"));
//        expResult.add(new Task("Reserve hotel room", LocalDate.of(2021, Month.JULY, 4), "Incompleted"));
//
//        ObservableList<Task> actualResult = controller.loadFile(new File("C:\\Users\\EDELITA\\Desktop\\OOPExercises\\assignment4part2\\inputFiles\\test.txt"));
//
//        assertEquals(expResult, actualResult);
//    }

    @Test
    void saveFile() {
        ObservableList<Task> list = FXCollections.observableArrayList();
        list.add(new Task("Task1", LocalDate.of(2021, Month.JULY, 3), "Completed"));

        controller.saveFile(list, new File("C:\\Users\\EDELITA\\Desktop\\OOPExercises\\assignment4part2\\outputFiles\\out.txt"));

        // After writing a Task to the file, the writer writes a new line
        String expectedStr = "Task1,2021-07-03,Completed"+System.lineSeparator();

        // Read in file content
        String actualStr = "";
        try {
            actualStr = new String(Files.readAllBytes(Paths.get("C:\\Users\\EDELITA\\Desktop\\OOPExercises\\assignment4part2\\outputFiles\\out.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expectedStr, actualStr);
    }

//    @Test
//    void clearObservableList() {
//    }
//
//    @Test
//    void addButtonClicked() {
//    }
//
//    @Test
//    void removeButtonClicked() {
//    }
//
//    @Test
//    void updateTaskButtonClicked() {
//    }
}