package s25.cs151.application.Controllers.Courses;

import javafx.fxml.FXML;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours Courses Page
 * Handles user input for Course information
 */

public class CoursesContoller implements Initializable{

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
    private TextField courseCode;
    @FXML
    private TextField courseName;
    @FXML
    private TextField sectionNumber;
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
    private MenuItem reportItem;

    /*
    String section = courseSection.getText();
    String name = courseName.getText();
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigationHandlers();
    }

    // Sets up navigation between different views
    private void setupNavigationHandlers() {
        // 4 BUTTONS + 1 DASHBOARD LABEL + CANCEL BUTTON + SAVE BUTTON
        // "Office Hours" button is used to show up the form
        officeHoursBtn.setOnAction(e -> {
            try {
                switchToOfficeHours();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // "Time Slots" button is used to show up the time slots page
        timeSlotsBtn.setOnAction(e -> {
            try {
                switchToTimeSlots();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // "Courses" button is used to reset the courses page
        CoursesBtn.setOnAction(e -> resetForm());

        // "List All" button is used to show up table containing all office hours
        listAllBtn.setOnAction(e -> {
            try {
                switchToListAllView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // switch back to dashboard when Dashboard label is clicked
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // "Cancel" button is used to refresh the form
        cancelBtn.setOnAction(e -> resetForm());

        // "Save" button is used to save input data to database
        saveBtn.setOnAction(e -> handleSaveButton());

        // 4 MENU ITEMS + 2 MENU ITEMS TO BE IMPLEMENTED
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

        // Handle the Time Slots menu item click
        timeslotsItem.setOnAction(event -> {
            try {
                switchToTimeSlots();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle the Courses menu item click
        coursesItem.setOnAction(e -> resetForm());
    }

    public void handleSaveButton() {
        try {
            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/courses.db");
            Connection connection = connectDB.getConnection();

            String selectedCourseCode = courseCode.getText();
            String selectedCourseName = courseName.getText();
            String selectedSectionNumber = sectionNumber.getText();

            if (selectedCourseCode.isEmpty() || selectedCourseName.isEmpty() || selectedSectionNumber.isEmpty())
            {
                showAlert("Please fill in all required fields: Course Code, Course Name, and Section Number");
                return;
            }

            if (connection != null) {
                try {
                    Statement statement = connection.createStatement();
                    String createTable = "CREATE TABLE IF NOT EXISTS courses ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "courseCode TEXT, "
                            + "courseName TEXT, "
                            + "sectionNumber TEXT)";
                    //create the table
                    statement.executeUpdate(createTable);

                    // duplicate checking
                    String dupCountQuery = "SELECT COUNT(*) AS count FROM courses WHERE courseCode = ? AND courseName = ? AND sectionNumber = ?";
                    try(PreparedStatement preparedStatementDup = connection.prepareStatement(dupCountQuery)) {
                        preparedStatementDup.setString(1, selectedCourseCode);
                        preparedStatementDup.setString(2, selectedCourseName);
                        preparedStatementDup.setString(3, selectedSectionNumber);

                        try(ResultSet resultSetDup = preparedStatementDup.executeQuery()) {
                            resultSetDup.next();
                            if (resultSetDup.getInt("count") > 0) {
                                showAlert("This course code, course name, and section number combination already exists. Please select another one.");
                                return;
                            }
                        }
                    }

                    String insertQuery = "INSERT INTO courses (courseCode, courseName, sectionNumber) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, selectedCourseCode);
                    preparedStatement.setString(2, selectedCourseName);
                    preparedStatement.setString(3, selectedSectionNumber);
                    preparedStatement.executeUpdate();

                    showAlert("Successfully Saved!");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            connectDB.closeConnection();
            resetForm();

        } catch (Exception e) {
            throw new RuntimeException(e);
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
        courseCode.setText("");
        courseName.setText("");
        sectionNumber.setText("");
    }
    /**
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }
    /**
     * Switches to the office hours view
     */
    private void switchToOfficeHours() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }
    /**
     * Switches to the Time Slots view
     */
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }
    /**
     * Switches to the list view showing all courses entries
     */
    private void switchToListAllView() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/CourseList.fxml", "Courses List");
    }
}
