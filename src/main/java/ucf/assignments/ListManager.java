package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class ListManager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("ToDoList.fxml"));

            Scene scene = new Scene(root);
//            scene.getStylesheets().add("lightBlue.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("TODO LIST APPLICATION");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }



}