package s25.cs151.application.Controllers.TimeSlots;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.*;
import s25.cs151.application.Models.ConnectDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * Controller for the Office Hours time slots page.
 * Handles user input for time slots
 */

public class TimeSlotsController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Button officeHoursBtn;
    @FXML
    private Button timeSlotsBtn;
    @FXML
    private Button CoursesBtn;
    @FXML
    private Button listAllBtn;
    @FXML
    private ComboBox<String> startComboBox;
    @FXML
    private ComboBox<String> endComboBox;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @FXML
    private Label dashboardLabel;
    @FXML
    private MenuItem dashboardItem;
    @FXML
    private MenuItem officehoursItem;
    @FXML
    private MenuItem timeslotsItem;
    @FXML
    private MenuItem coursesItem;
    @FXML
    private MenuItem scheduleItem;
    @FXML
    private MenuItem searchItem;
    @FXML
    private MenuItem reportItem;
    @FXML
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) root.getScene().getWindow();
        });

        //show example for users to know the format of time slot
        startComboBox.setValue("10:00 AM");
        endComboBox.setValue("11:30 AM");
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
    }

    // Sets up navigation between different views
    private void setupNavigationHandlers() {

        officeHoursBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeSlotsBtn.setOnAction(e -> resetForm());
        CoursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        listAllBtn.setOnAction(e -> switchTo(new TimeSlotsListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        cancelBtn.setOnAction(e -> resetForm());
        saveBtn.setOnAction(e -> {
            try {
                handleSaveButton();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        dashboardItem.setOnAction(event -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(event -> resetForm());
        coursesItem.setOnAction(event -> switchTo(new CoursesSwitcher(stage)));
        scheduleItem.setOnAction(event -> switchTo(new ScheduleSwitcher(stage)));
        searchItem.setOnAction(event -> switchTo(new SearchSwitcher(stage)));
    }

    private void switchTo(SceneSwitcher switcher) {
        try {
            switcher.switchScene();
        } catch (IOException e) {
            System.err.println("Error switching to view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleSaveButton() throws ParseException {
        //Connect to database
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/timeSlots.db");
        Connection connection = connectDB.getConnection();

        String fromHour= startComboBox.getValue().toUpperCase();
        String toHour = endComboBox.getValue().toUpperCase();

        // check if the field is empty or not
        if(fromHour.isEmpty() || toHour.isEmpty()){
            showAlert("Please fill in all required fields: From Hour and To Hour");
        }

        //validate fromHour < toHour
        // compare 2 strings. str1: 10:30 AM and 12:30 PM
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setLenient(false);
        try{
            Date fromDate = dateFormat.parse(fromHour);
            Date toDate = dateFormat.parse(toHour);
            if (fromDate.compareTo(toDate)>0)
            {
                showAlert("From Hour should be before To Hour");
                return;
            }
        }catch (ParseException e) {
            // validate input format, the format should be hh:mm AM/PM
            showAlert("Time Slots should be formatted: hh:mm AM/PM");
            return;
        }

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
     * Resets all form fields to their default values
     */
    private void resetForm() {
        //reset ComboChoices
        startComboBox.setValue("");
        endComboBox.setValue("");
    }
}
