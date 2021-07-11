package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Filter;

public class ToDoListController implements Initializable {

    @FXML private Button clearListButton;
    @FXML private Button updateTaskButton;
    @FXML private Button clearFieldsButton;
    @FXML private Button removeButton;

    @FXML private MenuBar menuBar;

    @FXML private Label errorLabel;

    @FXML private TableView<Task> tableView;
    @FXML private TableColumn<Task, String> taskDescriptionColumn;
    @FXML private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    @FXML private TextField descriptionTextField;
    @FXML private DatePicker dueDatePicker;

    @FXML private ComboBox<String> comboBox;

    FileChooser fileChooser = new FileChooser();
    ObservableList<Task> observableTaskList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateTaskButton.setDisable(true);
        clearFieldsButton.setDisable(true);
        removeButton.setDisable(true);
        // Add later remove item button

        // set up the columns
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // load dummy data for testing
        observableTaskList.addAll(getTasks());

        tableView.setItems(observableTaskList);
//        tableView.setPlaceholder(new Label("Your Table is Empty"));

        // Update table to allow Status Column to be editable.
        tableView.setEditable(true);
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Completed", "Incompleted"));


        // Disable remove button until a task is selected from the table
        removeButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        // Disable remove button when tableView is empty
        removeButton.disableProperty().bind(Bindings.size(tableView.getItems()).lessThan(1));
        // Disable Clear List button when tableView is empty
        clearListButton.disableProperty().bind(Bindings.size(tableView.getItems()).lessThan(1));


        // Allow multiple tasks selection at one
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        //----------------------------------------------------------------initialize view comboBox
        /*
        genderBoxData.add(new String("Male"));
        genderBoxData.add(new String("Female"));

        genderBox.setItems(genderBoxData);
         */


    }

    // Method to create a Task Object and add it to the table
    public void addButtonClicked() {
        errorLabel.setVisible(false);

        if (descriptionTextField.getText().isEmpty() || dueDatePicker.getValue() == null) {
            errorLabel.setVisible(true);
            errorLabel.setText("Task description and due date must be filled in!");
        } else {
            Task newTask = new Task(descriptionTextField.getText(), dueDatePicker.getValue(), "Incompleted");

            // Get all the items as a list, then add the new task to the list
            observableTaskList.add(newTask);
            clearFields();
        }
    }


    public void menuItemOpenListClicked() {
        fileChooser.setTitle("Open a ToDo List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null){
            loadFile(file);
        }
    }

    public void loadFile(File file){
        ObservableList<Task> items = FXCollections.observableArrayList();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] lines;
        LocalDate datetime = null;

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                lines = scan.nextLine().split(",");

                // convert String to LocalDate
                try {
                    datetime = LocalDate.parse(lines[1], pattern);
                } catch (DateTimeParseException e) {
                    System.out.println(lines[1] + " cannot be parse into yyyy-MM-dd");
                }

                items.add(new Task(lines[0], datetime, lines[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // remove previous Tasks
        clearObservableList(observableTaskList);

        // Add new Tasks from the file
        observableTaskList.addAll(items);

        // For Testing
        printObservableList(observableTaskList);
    }


    public void clearListButtonClicked() {
        clearObservableList(observableTaskList);

        // For testing
        printObservableList(observableTaskList);
    }


    public void menuItemSaveListClicked() {
        fileChooser.setTitle("Save ToDo List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File file = fileChooser.showSaveDialog(new Stage());
//        System.out.println(file);

        if(file != null){
            saveFile(observableTaskList, file);
        }
    }

    public void saveFile(ObservableList<Task> tasksList, File file) {

        try {
            // create a writer
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < tasksList.size(); i++) {
                outWriter.write(tasksList.get(i).toString());
                outWriter.newLine();
            }
            System.out.println(tasksList.toString());
            outWriter.close();

        } catch (IOException e) {
            Alert saveAlert = new Alert(Alert.AlertType.ERROR);
            saveAlert.setHeaderText("A writing error has occurred");
            saveAlert.showAndWait();
            e.printStackTrace();
        }
    }


    public void menuItemGetHelpClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Get Help");
        alert.setHeaderText("Refer to file README.md in the GitHub Repository for the project");
        alert.showAndWait();
    }



    // Method to clear data from Item Description and Date Picker
    public void clearFields() {
        descriptionTextField.clear();
        dueDatePicker.getEditor().clear();
        descriptionTextField.requestFocus();
    }

    // Method to double click on a cell and update item description
    public void updateButtonClicked() {

    }


    // Method to remove selected row(s) from the table
    public void removeButtonClicked() {
        errorLabel.setVisible(false);

        if(!observableTaskList.isEmpty()){
            observableTaskList.removeAll(tableView.getSelectionModel().getSelectedItem());
            tableView.getSelectionModel().clearSelection();
        }
    }


    // Method to return an ObservableList of Task Objects
    public ObservableList<Task> getTasks() {
        ObservableList<Task> task = FXCollections.observableArrayList();
        task.add(new Task("Buy round trip flight tickets to Lima", LocalDate.of(2021, Month.JULY, 3), "Completed"));
        task.add(new Task("Reserve hotel room", LocalDate.of(2021, Month.JULY, 4), "Completed"));
        task.add(new Task("Buy round trip flight tickets from Lima to Cuzco", LocalDate.of(2021, Month.JULY, 8), "Completed"));
        task.add(new Task("Flight to Lima", LocalDate.of(2021, Month.AUGUST, 13), "Incompleted"));
        task.add(new Task("Flight to Cuzco", LocalDate.of(2021, Month.AUGUST, 15), "Incompleted"));
        task.add(new Task("Train to Machu Picchu", LocalDate.of(2021, Month.AUGUST, 16), "Incompleted"));
        task.add(new Task("Tour to Rainbow Mountain", LocalDate.of(2021, Month.AUGUST, 18), "Incompleted"));

        return task;
    }

    public void onEditStatus(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        selectedTask.setStatus(taskStringCellEditEvent.getNewValue());
    }

    public void updateView(MouseEvent mouseEvent) {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        descriptionTextField.setText(selectedTask.getTaskDescription());
        dueDatePicker.setValue(selectedTask.getDueDate());

        clearFieldsButton.setDisable(false);
        updateTaskButton.setDisable(false);
        removeButton.setDisable(false);
    }

    public void updateTaskButtonClicked() {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        int rowIdx = tableView.getSelectionModel().getFocusedIndex();

        if(selectedTask != null){
            selectedTask.setTaskDescription(descriptionTextField.getText());
            selectedTask.setDueDate(dueDatePicker.getValue());
            tableView.getItems().set(rowIdx, selectedTask);
        }
    }

    public void clearFieldsButtonClicked(){
        clearFields();
    }

    public void closeApp(){
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) menuBar.getScene().getWindow();
        exitAlert.setTitle("Exit Application");
        exitAlert.setHeaderText("Are you sure you want to exit?");
        exitAlert.initModality(Modality.APPLICATION_MODAL);
        exitAlert.initOwner(stage);
        exitAlert.showAndWait();

        if(exitAlert.getResult() == ButtonType.OK) {
            Platform.exit();
        }
        else {
            exitAlert.close();
        }
    }

    public void printObservableList(ObservableList<Task> list){
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).getTaskDescription());
        }
    }

    public void clearObservableList(ObservableList<Task> list){
        list.clear();
    }

}

