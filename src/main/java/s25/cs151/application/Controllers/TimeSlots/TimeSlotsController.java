package s25.cs151.application.Controllers.TimeSlots;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import s25.cs151.application.Models.ConnectDB;

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

    private void setupNavigationHandlers() {

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

    public void switchToDashboard(MouseEvent mouseEvent) {

    }
}
