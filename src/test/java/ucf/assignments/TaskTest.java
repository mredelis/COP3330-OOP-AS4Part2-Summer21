package ucf.assignments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Trekking the Inca Trail", LocalDate.of(2021, Month.AUGUST, 20), "Incompleted");
    }

    @Test
    void convertToString() {
        String expResult = "Trekking the Inca Trail,2021-08-20,Incompleted";
        assertEquals(expResult, task.convertToString());
    }
}