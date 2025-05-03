package s25.cs151.application.Controllers.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button editScheduleBtn;
    @FXML
    private Button officeHoursBtn;
    @FXML
    private Button timeSlotsBtn;
    @FXML
    private Button coursesBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button reportBtn;
    @FXML
    private Button searchBtn;

    @FXML
    private RadioMenuItem dashboardItem;
    @FXML
    private RadioMenuItem officehoursItem;
    @FXML
    private RadioMenuItem timeslotsItem;
    @FXML
    private RadioMenuItem coursesItem;
    @FXML
    private RadioMenuItem scheduleItem;
    @FXML
    private RadioMenuItem searchItem;
    @FXML
    private RadioMenuItem reportItem;
    @FXML
    private RadioMenuItem editScheduleItem;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up navigation handlers
        setupNavigationHandlers();
    }

    private void setupNavigationHandlers(){
        // 3 BUTTONS + 2 BUTTONS TO BE IMPLEMENTED
        // "Office Hours" button is used to show up the office hour page
        officeHoursBtn.setOnMouseClicked(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        coursesBtn.setOnAction(event -> {
            try {
                switchToCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        scheduleBtn.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        searchBtn.setOnAction(event -> {
            try {
                switchToSearch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
        coursesItem.setOnAction(event -> {
            try {
                switchToCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        scheduleItem.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        searchItem.setOnAction(event -> {
            try {
                switchToSearch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        editScheduleBtn.setOnAction(event -> {
            try {
                switchToEditSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    // Method declarations for switching views
    /**
     * Switches to the dashboard page
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    /**
     * Switches to the Office Hours view
     */
    private void switchToOfficeHours() throws IOException {
        // Load the Office Hours FXML
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
     * Switches to the Courses view
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }
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
     * Switches to the Edit view
     */
    private void switchToEditSchedule() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Edit/EditSchedule.fxml", "Edit Schedule");
    }


}
