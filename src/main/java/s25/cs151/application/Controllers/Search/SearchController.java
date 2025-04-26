package s25.cs151.application.Controllers.Search;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;
import s25.cs151.application.Models.Schedule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private AnchorPane root;
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
    private TableView<Schedule> scheduleTable;
    @FXML
    private TableColumn<Schedule, String> studentNameColumn;
    @FXML
    private TableColumn<Schedule, String> dateColumn;
    @FXML
    private TableColumn<Schedule, String> timeColumn;
    @FXML
    private TableColumn<Schedule, String> courseColumn;
    @FXML
    private TableColumn<Schedule, String> reasonColumn;
    @FXML
    private TableColumn<Schedule, String> commentColumn;
    @FXML
    private Label dashboardLabel;
    @FXML
    private TextField searchStudent;
    @FXML
    private Button searchBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button editBtn;

    public void initialize(URL location, ResourceBundle resources) {
        setupNavigationHandlers();
    }



    private void setupNavigationHandlers() {


        // switch back to dashboard when Dashboard label is clicked
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle all menu items click
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
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

        officehoursItem.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        timeslotsItem.setOnAction(event -> {
            try {
                switchToTimeSlots();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        coursesItem.setOnAction(event -> {
            try {
                switchToCourses();
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
    }


    /**
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    /**
     * Switches to the office hours view
     */
    private void switchToOfficeHours() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }

    /**
     * Switches to the Time Slots view
     */
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }

    /**
     * Switches to the Courses view
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }

    /**
     * Switches to the Schedule view
     */
    private void switchToSchedule() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule");
    }
    /**
     * Switches to the Search view
     */
    private void switchToSearch() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Search/SearchSchedule.fxml", "Search Schedule");
    }

}
