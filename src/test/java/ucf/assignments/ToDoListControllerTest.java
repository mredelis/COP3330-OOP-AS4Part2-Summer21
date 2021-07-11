package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

class ToDoListControllerTest {
    private ObservableList<Task> testTaskList;

    @BeforeEach
    void setUp() {
        ToDoListController controller = new ToDoListController();
    }

    @Test
    void loadFile() {
        ToDoListController controller = new ToDoListController();

        ObservableList<Task> expResult = FXCollections.observableArrayList();
        expResult.add(new Task("Buy round trip flight tickets to Lima", LocalDate.of(2021, Month.JULY, 3), "Completed"));
        expResult.add(new Task("Reserve hotel room", LocalDate.of(2021, Month.JULY, 4), "Incompleted"));

        ObservableList<Task> actualResult = controller.loadFile(new File("C:\\Users\\EDELITA\\Desktop\\OOPExercises\\assignment4part2\\inputFiles\\test.txt"));

        assertEquals(expResult, actualResult);
    }

    @Test
    void saveFile() {
    }

    @Test
    void clearObservableList() {
    }

    @Test
    void addButtonClicked() {
    }

    @Test
    void removeButtonClicked() {
    }

    @Test
    void updateTaskButtonClicked() {
    }
}