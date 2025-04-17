package s25.cs151.application.Controllers.OfficeHours;

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

    // Sets up navigation between different views
    private void setupNavigationHandlers() {
        // 4 BUTTONS + 1 DASHBOARD LABEL + CANCEL BUTTON + SAVE BUTTON
        // "Office Hours" button is used to show up the form
        officeHoursBtn.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
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

        // "Courses" button is used to show up the courses page
        CoursesBtn.setOnAction(e -> {
            try {
                switchToCourses();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

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
        officehoursItem.setOnAction(e -> resetForm());

        // Handle the Time Slots menu item click
        timeslotsItem.setOnAction(event -> {
            try {
                switchToTimeSlots();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle the Courses menu item click
        coursesItem.setOnAction(event -> {
            try {
                switchToCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Handle the Courses menu item click
        scheduleItem.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Handle the Search menu item click
        searchItem.setOnAction(event -> {
            try {
                switchToSearch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }
    /**
     * Switches to the office hours view
     */
    private void switchToNewOfficeHoursView() throws IOException {
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
     * Switches to the Courses View
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }
    /**
     * Switches to the list all view showing all office hours entries
     */
    private void switchToListAllView() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHoursList.fxml", "Office Hours List");
    }
    /**
     * Switches to the Schedule view
     */
    private void switchToSchedule() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule");
    }
    /**
     * Switches to the Search view
     */
    private void switchToSearch() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Search/SearchSchedule.fxml", "Search Schedule");
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
