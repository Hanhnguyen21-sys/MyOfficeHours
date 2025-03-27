package s25.cs151.application.Controllers.Dashboard;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public AnchorPane root1;
    public Button officeHoursBtn1;
    public Button scheduleBtn1;
    public Button reportBtn1;
    public Button timeSlotsBtn;
    public Button coursesBtn;

    // Dashboard button and dropdown menu
    public MenuButton menuBtn;
    public MenuItem officeHoursMenuItem;
    public MenuItem dashBoardMenuItem;
    public MenuItem scheduleMenuItem;
    public MenuItem reportMenuItem;
    public MenuItem dashboardItem1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Handle the Office Hours button click
        officeHoursBtn1.setOnMouseClicked(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /*
         * // Handle the Schedule button click
         * scheduleBtn1.setOnMouseClicked(event -> {
         * // Implement functionality for schedule
         * });
         * // Handle the Report button click
         * reportBtn1.setOnMouseClicked(event -> {
         * // Implement functionality for report
         * });
         */

        // switch back to dashboard when click

        // Handle the Dashboard menu item click
        dashBoardMenuItem.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Handle the Office Hours menu item click
        officeHoursMenuItem.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /*
         * // Handle the Schedule menu item click
         * scheduleMenuItem.setOnAction(event -> {
         * // Implement functionality for schedule
         * });
         *
         * // Handle the Report menu item click
         * reportMenuItem.setOnAction(event -> {
         * // Implement functionality for report
         * });
         */

        // "Time Slots" button is used to show up the time slots page
        timeSlotsBtn.setOnAction(e -> {
            try {
                switchToTimeSlots();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Handle the Office Hours button click
        coursesBtn.setOnAction(event -> {
            try {
                switchToCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Method to switch to Dashboard page
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root1.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    // Method to switch to the Office Hours page
    private void switchToOfficeHours() throws IOException {
        // Load the Office Hours FXML
        Stage stage = (Stage)root1.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }

    /**
     * Switches to the Time Slots view
     */
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage)root1.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }

    /**
     * Switches to the courses view
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage)root1.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }
}
