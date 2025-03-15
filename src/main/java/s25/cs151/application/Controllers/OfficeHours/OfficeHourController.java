package s25.cs151.application.Controllers.OfficeHours;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours form view.
 * Handles user input for creating and editing office hours entries.
 */
public class OfficeHourController implements Initializable {
    public AnchorPane root;
    public MenuButton dashboardBtn;
    public VBox mainContainer;
    public VBox topForm;
    public VBox semesterGroup;
    public Label semesterLabel;
    public ChoiceBox<String> semesterCombo;
    public VBox yearGroup;
    public Label yearLabel;
    public StackPane yearFieldContainer;
    public TextField yearField;
    public VBox dayGroup;
    public HBox dayCheckboxes;
    public CheckBox monCheck;
    public CheckBox tueCheck;
    public CheckBox wedCheck;
    public CheckBox friCheck;
    public CheckBox thurCheck;
    public VBox timeGroup;
    public HBox startTimeGroup;
    public TextField startTimeField;
    public HBox endTimeGroup;
    public TextField endTimeField;
    public VBox titleSection;
    public HBox actionBtn;
    public Button listAllBtn;
    public Button newBtn;
    public TextField courseSection;
    public TextField courseCode;
    public TextField courseName;
    public Button cancelBtn;
    public Button saveBtn;

    public Label dashboardLabel;
    public MenuItem dashboardItem;
    public MenuItem officehoursItem;
    public MenuItem scheduleItem;
    public MenuItem reportItem;

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
        // handle MenuItems when chosen
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        officehoursItem.setOnAction(e -> resetForm());

        // switch back to dashboard when click
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // "Cancel" button is used to refresh the form
        cancelBtn.setOnAction(e -> resetForm());

        // "List All" button is used to show up table containing all office hours
        listAllBtn.setOnAction(e -> {
            try {
                switchToListAllView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // "New" button is used to show up the form
        newBtn.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
        // Collect all form data
        String selectedSemester = semesterCombo.getValue();
        String year = yearField.getText();

        // Collect days
        StringBuilder days = new StringBuilder();
        if (monCheck.isSelected())
            days.append("Monday,");
        if (tueCheck.isSelected())
            days.append("Tuesday,");
        if (wedCheck.isSelected())
            days.append("Wednesday,");
        if (thurCheck.isSelected())
            days.append("Thursday,");
        if (friCheck.isSelected())
            days.append("Friday,");

        // Remove trailing comma if exists
        String selectedDays = days.toString();
        if (selectedDays.endsWith(",")) {
            selectedDays = selectedDays.substring(0, selectedDays.length() - 1);
        }

        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String course = courseCode.getText();
        String section = courseSection.getText();
        String name = courseName.getText();

        // Validate required fields
        if (selectedSemester == null || year.isEmpty() || selectedDays.isEmpty() ||
                startTime.isEmpty() || endTime.isEmpty() || course.isEmpty()) {
            showAlert(
                    "Please fill in all required fields: Semester, Year, Days, Start Time, End Time, and Course Code.");
            return;
        }

        // For now, just show a confirmation message
        // In a future version, this would save to a database or file
        showAlert("Office Hours saved successfully!\n\n" +
                "Semester: " + selectedSemester + " " + year + "\n" +
                "Days: " + selectedDays + "\n" +
                "Time: " + startTime + " - " + endTime + "\n" +
                "Course: " + course + " - " + section + "\n" +
                "Course Name: " + name);

        // Reset the form after saving
        resetForm();
    }

    /**
     * Resets all form fields to their default values
     */
    private void resetForm() {
        // reset the semester combo choice
        semesterCombo.setValue(semesterCombo.getItems().get(0));

        // reset text-field
        startTimeField.setText("");
        endTimeField.setText("");
        yearField.setText("");
        courseSection.setText("");
        courseCode.setText("");
        courseName.setText("");

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard/Dashboard.fxml"));
        Parent dashboard = loader.load();
        Scene dashboardScene = new Scene(dashboard);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(dashboardScene);
        stage.show();
    }

    /**
     * Switches to the list view showing all office hours entries
     */
    private void switchToListAllView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/OfficeHours/OfficeHoursList.fxml"));
        Parent listView = loader.load();
        Scene scene = new Scene(listView);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Office Hours List");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches to the new office hours form view
     */
    private void switchToNewOfficeHoursView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/OfficeHours/OfficeHours.fxml"));
        Parent newOfficeHoursView = loader.load();
        Scene scene = new Scene(newOfficeHoursView);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("New Office Hours");
        stage.setScene(scene);
        stage.show();
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
