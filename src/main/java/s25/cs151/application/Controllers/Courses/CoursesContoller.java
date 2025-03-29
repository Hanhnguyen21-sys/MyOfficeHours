package s25.cs151.application.Controllers.Courses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;

import java.io.IOException;
import java.net.URL;
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
        //saveBtn.setOnAction(e -> handleSaveButton());

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

    public void handleSaveButton(ActionEvent event) {
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
        SwitchScene.switchScene(stage, "/Fxml/Courses/CourseList.fxml", "Time Slots List");
    }
}
