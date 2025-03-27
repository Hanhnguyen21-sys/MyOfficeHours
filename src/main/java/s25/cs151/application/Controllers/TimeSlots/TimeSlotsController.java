package s25.cs151.application.Controllers.TimeSlots;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;
import s25.cs151.application.Models.ConnectDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * Controller for the Office Hours time slots page.
 * Handles user input for time slots
 */

public class TimeSlotsController implements Initializable {
    public AnchorPane root;
    public Label dashboardLabel;
    public Button officeHoursBtn;
    public Button timeSlotsBtn;
    public Button CoursesBtn;
    public Button listAllBtn;
    public MenuItem dashboardItem;
    public MenuItem officehoursItem;
    public MenuItem scheduleItem;
    public MenuItem reportItem;
    public ComboBox<String> startComboBox;
    public ComboBox<String> endComboBox;
    public Button cancelBtn;
    public Button saveBtn;
    /*
    public VBox timeGroup;
    public HBox startTimeGroup;
    public TextField startTimeField;
    public HBox endTimeGroup;
    public TextField endTimeField;
    public VBox titleSection;

    String startTime = startTimeField.getText();
    String endTime = endTimeField.getText();
    String course = courseCode.getText();

    /*
    startTime.isEmpty() || endTime.isEmpty() || course.isEmpty())
    showAlert("Time: " + startTime + " - " + endTime + "\n" +
                "Course: " + course + " - " + section + "\n" +
                "Course Name: " + name);
     // reset text-field
        startTimeField.setText("");
        endTimeField.setText("");
        courseSection.setText("");
        courseCode.setText("");
        courseName.setText("");
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigationHandlers();
        showTimeOptions();
    }

    /**
     * The method displays comboBoxes for users to define time slots
     */
    private void showTimeOptions() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        startComboBox.setEditable(true);
        endComboBox.setEditable(true);

        IntStream.range(0, 24).forEach(hour ->
                IntStream.range(0, 60).filter(min -> min % 15 == 0).forEach(min -> {
                    LocalTime time = LocalTime.of(hour, min);
                    startComboBox.getItems().add(time.format(dateTimeFormatter));
                    endComboBox.getItems().add(time.format(dateTimeFormatter));
                })
        );

        startComboBox.setValue("10:00 AM");
        endComboBox.setValue("11:30 AM");
    }

    // Sets up navigation between different views
    private void setupNavigationHandlers() {
        // Handle the Dashboard menu item click
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle the Office Hours menu item click
        officehoursItem.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // switch back to dashboard when click
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle the Office Hours button click
        officeHoursBtn.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // "Cancel" button is used to refresh the form
        cancelBtn.setOnAction(e -> resetForm());

        timeSlotsBtn.setOnAction(e -> resetForm());

        // "List All" button is used to show up table containing all office hours
        listAllBtn.setOnAction(e -> {
            try {
                switchToListAllView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void handleSaveButton(ActionEvent event) {
        //Connect to database
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/timeSlots.db");
        Connection connection = connectDB.getConnection();

        String fromHour= startComboBox.getValue();
        String toHour = endComboBox.getValue();
        if (connection!=null){
            try{
                Statement statement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS timeSlots ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "fromHour TEXT, "
                        + "toHour TEXT)";

                //create a table
                statement.executeUpdate(createTable);
                //insert input data to the table
                String insertQuery = "INSERT INTO timeSlots (fromHour, toHour) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1,fromHour);
                preparedStatement.setString(2,toHour);
                preparedStatement.executeUpdate();
                showAlert("Successfully Saved!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Switches to the new office hours form view
     */
    private void switchToOfficeHours() throws IOException {
        // Load the Office Hours FXML
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }

    // Method to switch to Dashboard page
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    /**
     * Switches to the list view showing all office hours entries
     */
    private void switchToListAllView() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "Time Slots List");
    }

    /**
     * Resets all form fields to their default values
     */
    private void resetForm() {
        // TODO: Reset time picker
    }
}
