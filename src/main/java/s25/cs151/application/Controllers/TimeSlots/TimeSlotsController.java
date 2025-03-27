package s25.cs151.application.Controllers.TimeSlots;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void setupNavigationHandlers() {
        dashboardLabel.setOnMouseClicked(e->{
            try{
                switchToDashboard();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        listAllBtn.setOnMouseClicked(e->{
            try{
                switchToAllList();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        officeHoursBtn.setOnAction(e->{
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        CoursesBtn.setOnAction(e->{
            try {
                switchCourses();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        saveBtn.setOnAction(e-> {
            try {
                handleSaveButton();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        cancelBtn.setOnAction(e->handleCancelButton());
    }

    private void handleCancelButton() {
        //reset ComboChoices
        startComboBox.setValue("");
        endComboBox.setValue("");
    }

    public void handleSaveButton() throws ParseException {
        //Connect to database
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/timeSlots.db");
        Connection connection = connectDB.getConnection();

        String fromHour= startComboBox.getValue();
        String toHour = endComboBox.getValue();

        // check if the field is empty or not
        if(fromHour.isEmpty() || toHour.isEmpty()){
            showAlert("Please fill in all required fields: From Hour and To Hour");
        }

        //validate fromHour < toHour
        // compare 2 strings. str1: 10:30 AM and 12:30 PM
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date fromDate = dateFormat.parse(fromHour);
        Date toDate = dateFormat.parse(toHour);
        if (fromDate.compareTo(toDate)>0)
        {
            showAlert("From Hour should be before To Hour");
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

    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard.fxml", "Dashboard");
    }
    public void switchToAllList() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "List of Time Slots");
    }
    public void switchToNewOfficeHoursView() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage,"/Fxml/OfficeHours/OfficeHours.fxml","New Office Hours");
    }
    public void switchCourses() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }
}
