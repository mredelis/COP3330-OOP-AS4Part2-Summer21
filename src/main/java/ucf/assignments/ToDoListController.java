package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Edelis Molina
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ToDoListController implements Initializable {



    @FXML private MenuItem menuItemOpenList;
    @FXML private MenuItem menuItemSaveList;

    @FXML private MenuItem menuItemGetHelp;


    @FXML private TextField ListNameTextFiled;
    @FXML private Label errorLabel;

    @FXML private TableView<Task> tableView;
    @FXML private TableColumn<Task, String> taskDescriptionColumn;
    @FXML private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;


    @FXML private TextField descriptionTextField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> statusComboBox;

    @FXML private Button addButton;
    @FXML private Button clearButton;
    @FXML private Button updateButton;
    @FXML private Button removeButton;
    @FXML private Button submitButton;

    @FXML private MenuItem viewAllTasksMenuItem;
    @FXML private MenuItem viewCompletedTasksMenuItem;
    @FXML private MenuItem viewUncompletedTasksMenuItem;

    FileChooser fileChooser = new FileChooser();

    // Initialize controller class
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ComboBox Items
        statusComboBox.getItems().removeAll(statusComboBox.getItems());
        statusComboBox.getItems().addAll("Completed", "Uncompleted");

        // set up the columns
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // load dummy tasks for testing
        tableView.setItems(getTasks());

        // Update table to allow item description and due date columns to be editable.
        // The status (complete or incomplete) will be done via pushedButton
        tableView.setEditable(true);

        // Allow multiple tasks selection at one
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);




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

    public void loadFile(File file){
        ObservableList<Task> item = FXCollections.observableArrayList();
        /*
                ObservableList<Task> task = FXCollections.observableArrayList();
        task.add(new Task("Buy round trip flight tickets to Lima", LocalDate.of(2021, Month.JULY, 3), "Completed"));
         */

        String[] lines = null;
        LocalDate datetime = null;

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                lines = scan.nextLine().split(",");

                // convert String to LocalDate
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    datetime = LocalDate.parse(lines[1], pattern);
                    System.out.println(datetime);
                } catch (DateTimeParseException e) {
                    System.out.println(lines[1] + " cannot be parse into yyyy-mm-dd");
                }

                item.add(new Task(lines[0], datetime, lines[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < item.size(); i++){
            System.out.println(item.get(i));
        }

        tableView.setItems(item);




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
        errorLabel.setText("");

        if (descriptionTextField.getText().isEmpty()) {
            errorLabel.setText("Enter item description!");
        } else {
            Task newTask = new Task(descriptionTextField.getText(), dueDatePicker.getValue(), statusComboBox.getValue());

            // Get all the items as a list, then add the new task to the list
            tableView.getItems().add(newTask);
        }
    }

    // Method to clear data from Item Description, Date Picker and Combo Box
    public void clearButtonClicked() {
        descriptionTextField.clear();
        dueDatePicker.getEditor().clear();
        statusComboBox.valueProperty().set(null);
    }

    // Method to double click on a cell and update item description
    public void updateButtonClicked() {

    }


    // Method to remove selected row(s) from the table
    public void removeButtonClicked() {

        errorLabel.setText("");

        ObservableList<Task> allTasks = null, selectedRows;

        try {
            allTasks = tableView.getItems();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        selectedRows = tableView.getSelectionModel().getSelectedItems();

        for (Task task : selectedRows) {
            allTasks.remove(task);
        }
    }

    // Method to go back to the ListManager Scene
    public void submitButtonClicked() {

    }


    public ObservableList<Task> getAllTableViewItems(){
        return tableView.getItems();
    }

    public void viewAllTasksMenuItemClicked() {
        filter("", getAllTableViewItems());


    }

    public void viewCompletedTasksMenuItemClicked() {
        filter("Completed", getAllTableViewItems());

    }

    public void viewUncompletedTasksMenuItemClicked() {
        filter("Uncompleted", getAllTableViewItems());

    }


    // Method to return an ObservableList of Task Objects
    public ObservableList<Task> getTasks() {
        ObservableList<Task> task = FXCollections.observableArrayList();
        task.add(new Task("Buy round trip flight tickets to Lima", LocalDate.of(2021, Month.JULY, 3), "Completed"));
        task.add(new Task("Reserve hotel room", LocalDate.of(2021, Month.JULY, 4), "Completed"));
        task.add(new Task("Buy round trip flight tickets from Lima to Cuzco", LocalDate.of(2021, Month.JULY, 8), "Completed"));
        task.add(new Task("Flight to Lima", LocalDate.of(2021, Month.AUGUST, 13), "Uncompleted"));
        task.add(new Task("Flight to Cuzco", LocalDate.of(2021, Month.AUGUST, 15), "Uncompleted"));
        task.add(new Task("Train to Machu Picchu", LocalDate.of(2021, Month.AUGUST, 16), "Uncompleted"));
        task.add(new Task("Tour to Rainbow Mountain", LocalDate.of(2021, Month.AUGUST, 18), "Uncompleted"));

        return task;
    }

    public void filter(String searchValue, ObservableList<Task> dataList) {

        FilteredList<Task> filteredData = new FilteredList<>(dataList, b -> true);

        filteredData.setPredicate(task -> {
            // If filter text is empty, display all persons.
            if (searchValue == null || searchValue.isEmpty()) {
                return true;
            }

            if (task.getStatus().indexOf(searchValue) != -1)
                return true; // Filter matches first name.
            else
                return false; // Does not match.
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Task> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);
    }
}
