package s25.cs151.application.Controllers.Courses;

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
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours Courses Page
 * Handles user input for Course information
 */
public class CoursesController implements Initializable {

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
        setupNavigationHandlers();
    }

    private void setupNavigationHandlers() {
        officeHoursBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeSlotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        CoursesBtn.setOnAction(e -> resetForm());
        listAllBtn.setOnAction(e -> switchTo(new CourseListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        cancelBtn.setOnAction(e -> resetForm());
        saveBtn.setOnAction(e -> handleSaveButton());

        dashboardItem.setOnAction(e -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesItem.setOnAction(e -> resetForm());
        scheduleItem.setOnAction(e -> switchTo(new ScheduleSwitcher(stage)));
        searchItem.setOnAction(e -> switchTo(new SearchSwitcher(stage)));
    }

    private void switchTo(SceneSwitcher switcher) {
        try {
            switcher.switchScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}