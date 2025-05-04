package s25.cs151.application.Controllers.OfficeHours;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import s25.cs151.application.Controllers.Helpers.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.SceneSwitcher;
import s25.cs151.application.Controllers.Helpers.SwitchScene;
import s25.cs151.application.Models.ConnectDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours form view.
 * Handles user input for creating and editing office hours entries.
 */
public class OfficeHourController implements Initializable {
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
    private ChoiceBox<String> semesterCombo;
    @FXML
    private TextField yearField;
    @FXML
    private CheckBox monCheck;
    @FXML
    private CheckBox tueCheck;
    @FXML
    private CheckBox wedCheck;
    @FXML
    private CheckBox friCheck;
    @FXML
    private CheckBox thurCheck;
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


    private String[] semester = { "Spring", "Summer", "Fall", "Winter" };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize semester choices and set default value
        semesterCombo.getItems().addAll(semester);
        semesterCombo.setValue("Spring");

        // Set up navigation handlers
        setupNavigationHandlers();

        // Set up form action buttons
        setupFormActions();
    }

    private void setupNavigationHandlers() {

        officeHoursBtn.setOnAction(e -> resetForm());
        timeSlotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        CoursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        listAllBtn.setOnAction(e -> switchTo(new OfficeHoursListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        cancelBtn.setOnAction(e -> resetForm());
        saveBtn.setOnAction(e -> handleSaveButton());

        dashboardItem.setOnAction(event -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> resetForm());
        timeslotsItem.setOnAction(event -> switchTo(new TimeSlotsSwitcher(stage)));
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


    // Sets up form action buttons (cancel, list all, new)
    private void setupFormActions() {
        // ... existing button setup code ...
    }

    /**
     * Handles saving of office hours entry.
     * Validates required fields and shows confirmation message.
     */
    @FXML
    private void handleSaveButton() {
        try {
            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/officehours.db");
            Connection connection = connectDB.getConnection();

            // Collect all form data
            String selectedSemester = semesterCombo.getValue();
            String selectedYear = yearField.getText();

            // Collect days
            StringBuilder days = new StringBuilder();
            if (monCheck.isSelected())
                days.append("Monday, ");
            if (tueCheck.isSelected())
                days.append("Tuesday, ");
            if (wedCheck.isSelected())
                days.append("Wednesday, ");
            if (thurCheck.isSelected())
                days.append("Thursday, ");
            if (friCheck.isSelected())
                days.append("Friday, ");

            // Remove trailing comma if exists
            if (days.length() > 0) {
                days.setLength(days.length() - 2); // Removes last ", "
            }
            String selectedDays = days.toString();

            // Validate required fields
            if (selectedSemester == null || selectedYear.isEmpty() || selectedDays.isEmpty()) {
                showAlert("Please fill in all required fields: Semester, Year, Days");
                return;
            }

            // Validate year format (must be exactly 4 digits)
            if (!selectedYear.matches("\\d{4}")) {
                showAlert("Year must be exactly 4 digits.");
                return;
            }

            if (connection != null) {
                try {
                    Statement statement = connection.createStatement();
                    String createTable = "CREATE TABLE IF NOT EXISTS officeHours ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "semester TEXT, "
                            + "year TEXT, "
                            + "days TEXT)";
                    // create a table
                    statement.executeUpdate(createTable);

                    // check for duplication
                    String dupCountQuery = "SELECT COUNT(*) AS count FROM officeHours WHERE semester = ? AND year = ? AND days = ?";
                    try(PreparedStatement preparedStatementDup = connection.prepareStatement(dupCountQuery)) {
                        preparedStatementDup.setString(1, selectedSemester);
                        preparedStatementDup.setString(2, selectedYear);
                        preparedStatementDup.setString(3, selectedDays);

                        try(ResultSet resultSetDup = preparedStatementDup.executeQuery()) {
                            resultSetDup.next();

                            if (resultSetDup.getInt("count") > 0) {
                                showAlert(
                                        "This semester, year, and days combination for office hours already exists. Please select another one.");
                                return;
                            }
                        }
                    }

                    // insert data if there is no duplication
                    String insertQuery = "INSERT INTO officeHours (semester, year, days) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, selectedSemester);
                    preparedStatement.setString(2, selectedYear);
                    preparedStatement.setString(3, selectedDays);
                    preparedStatement.executeUpdate();
                    // show successfully alert
                    showAlert("Successfully Saved!");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            connectDB.closeConnection();
            // Reset the form after saving
            resetForm();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resets all form fields to their default values
     */
    private void resetForm() {
        // reset the semester combo choice
        semesterCombo.setValue(semesterCombo.getItems().get(0));

        // reset text-field
        yearField.setText("");

        // clear checkboxes
        monCheck.setSelected(false);
        tueCheck.setSelected(false);
        wedCheck.setSelected(false);
        thurCheck.setSelected(false);
        friCheck.setSelected(false);
    }

    /**
     * Shows an alert dialog with the given message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
