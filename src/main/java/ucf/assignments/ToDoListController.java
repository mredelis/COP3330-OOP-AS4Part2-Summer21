package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ToDoListController implements Initializable {

    @FXML private MenuItem menuItemOpenList;
    @FXML private MenuItem menuItemSaveList;
    @FXML private MenuItem menuItemGetHelp;
    @FXML private MenuItem viewAllTasksMenuItem;
    @FXML private MenuItem viewCompletedTasksMenuItem;
    @FXML private MenuItem viewIncompletedTasksMenuItem;

    @FXML private Button addButton;
    @FXML private Button clearListButton;
    @FXML private Button updateTaskButton;
    @FXML private Button clearFieldsButton;
    @FXML private Button removeButton;
    @FXML private Button submitButton;

    @FXML private Label errorLabel;

    @FXML private TableView<Task> tableView;
    @FXML private TableColumn<Task, String> taskDescriptionColumn;
    @FXML private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    @FXML private TextField descriptionTextField;
    @FXML private DatePicker dueDatePicker;

    FileChooser fileChooser = new FileChooser();

    // Initialize controller class
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateTaskButton.setDisable(true);
        clearFieldsButton.setDisable(true);
        // Add later remove item button

        // set up the columns
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // load dummy tasks for testing
        tableView.setItems(getTasks());

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
    }


    // DONE------------------------------------------------------------------------------------------------------
    @FXML
    void menuItemOpenListClicked(ActionEvent event) {
        fileChooser.setTitle("Open a ToDo List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File file = fileChooser.showOpenDialog(new Stage());
//        System.out.println(file);

        if(file != null){
            loadFile(file);
        }
    }

    // DONE------------------------------------------------------------------------------------------------------
    public void loadFile(File file){
        ObservableList<Task> item = FXCollections.observableArrayList();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] lines = null;
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

                item.add(new Task(lines[0], datetime, lines[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tableView.setItems(item);
    }

    // DONE------------------------------------------------------------------------------------------------------
    @FXML
    void clearButtonClicked(ActionEvent event) {
        tableView.getItems().clear();
    }

    // DONE------------------------------------------------------------------------------------------------------
    @FXML
    void menuItemSaveListClicked(ActionEvent event) {
        fileChooser.setTitle("Save ToDo List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));

        File file = fileChooser.showSaveDialog(new Stage());
//        System.out.println(file);

        if(file != null){
            saveFile(file);
        }
    }

    // DONE------------------------------------------------------------------------------------------------------
    public void saveFile(File file) {
        ObservableList<Task> tasks = tableView.getItems();

        try {
            // create a writer
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < tasks.size(); i++) {
                outWriter.write(tasks.get(i).toString());
                outWriter.newLine();
            }
            System.out.println(tasks.toString());
            outWriter.close();

        } catch (IOException e) {
            Alert saveAlert = new Alert(Alert.AlertType.ERROR);
            saveAlert.setHeaderText("A writing error has occurred");
            saveAlert.showAndWait();
            e.printStackTrace();
        }
    }


    @FXML
    void menuItemGetHelpClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Get Help");
        alert.setHeaderText("Refer to file README.md in the GitHub Repository for the project");
        alert.showAndWait();
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
            tableView.getItems().add(newTask);
            clearFields();
        }
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

        if(tableView.getSelectionModel().isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText("Select task to remove!");
        }
        else {
            tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
            tableView.getSelectionModel().clearSelection();
        }
    }


//    // Method to go back to the ListManager Scene
//    public void submitButtonClicked() {
//
//    }


//    public ObservableList<Task> getAllTableViewItems(){
//        return tableView.getItems();
//    }
//
//    public void viewAllTasksMenuItemClicked() {
//        filter("", getAllTableViewItems());
//
//
//    }
//
//    public void viewCompletedTasksMenuItemClicked() {
//        filter("Completed", getAllTableViewItems());
//
//    }
//
//    public void viewIncompletedTasksMenuItemClicked() {
//        filter("Incompleted", getAllTableViewItems());
//
//    }


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

//    public void filter(String searchValue, ObservableList<Task> dataList) {
//
//        FilteredList<Task> filteredData = new FilteredList<>(dataList, b -> true);
//
//        filteredData.setPredicate(task -> {
//            // If filter text is empty, display all persons.
//            if (searchValue == null || searchValue.isEmpty()) {
//                return true;
//            }
//
//            if (task.getStatus().indexOf(searchValue) != -1)
//                return true; // Filter matches first name.
//            else
//                return false; // Does not match.
//        });
//
//        // 3. Wrap the FilteredList in a SortedList.
//        SortedList<Task> sortedData = new SortedList<>(filteredData);
//
//        // 4. Bind the SortedList comparator to the TableView comparator.
//        // 	  Otherwise, sorting the TableView would have no effect.
//        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
//
//        // 5. Add sorted (and filtered) data to the table.
//        tableView.setItems(sortedData);
//    }


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
}
