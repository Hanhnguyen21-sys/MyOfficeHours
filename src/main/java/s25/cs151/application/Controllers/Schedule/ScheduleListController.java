package s25.cs151.application.Controllers.Schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;
import s25.cs151.application.Models.ConnectDB;
import s25.cs151.application.Models.Schedule;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ScheduleListController implements Initializable {
    @FXML
    private Label dashboardLabel;

    @FXML
    private Button OfficeHourBtn;
    @FXML
    private Button TimeslotsBtn;
    @FXML
    private Button CoursesBtn;
    @FXML
    private Button ScheduleBtn;

    @FXML
    private AnchorPane root;

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

    ObservableList<Schedule> scheduleObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns and button actions
        setupTableColumns();
        setupButtonActions();
        loadSchedules();
    }

    /**
     * Configures the table columns to display schedule data
     */
    private void setupTableColumns() {
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        reasonColumn.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }

    /**
     * Sets up event handlers for all buttons and MenuItems
     */
    private void setupButtonActions() {
        // Menu items and main navigation remain unchanged
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
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

        scheduleItem.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        reportItem.setOnAction(event -> {
            try {
                switchToReport();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Main buttons navigate to main views
        OfficeHourBtn.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        TimeslotsBtn.setOnAction(event -> {
            try {
                switchToTimeSlots();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CoursesBtn.setOnAction(event -> {
            try {
                switchToCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ScheduleBtn.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Dashboard label click
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Loads schedule data from the database and populates the table
     */
    private void loadSchedules() {
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/schedule.db");
        Connection connection = connectDB.getConnection();

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String selectQuery = "SELECT * FROM schedule";
                ResultSet resultSet = statement.executeQuery(selectQuery);

                while (resultSet.next()) {
                    String studentName = resultSet.getString("studentName");
                    String date = resultSet.getString("date");
                    String time = resultSet.getString("time");
                    String course = resultSet.getString("course");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");

                    Schedule schedule = new Schedule(studentName, date, time, course, reason, comment);
                    scheduleObservableList.add(schedule);
                }

                scheduleTable.setItems(scheduleObservableList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Switches to the dashboard view
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
     * Switches to the time slots view
     */
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }

    /**
     * Switches to the courses view
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }

    /**
     * Switches to the schedule view
     */
    private void switchToSchedule() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule");
    }

    /**
     * Switches to the report view
     */
    private void switchToReport() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Report/Report.fxml", "Report");
    }

    /**
     * Switches to the office hours list view
     */
    public void switchToOfficeHoursList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHoursList.fxml", "Office Hours List");
    }

    /**
     * Switches to the time slots list view
     */
    public void switchToTimeSlotsList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "Time Slots List");
    }

    /**
     * Switches to the courses list view
     */
    public void switchToCoursesList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/CourseList.fxml", "Course List");
    }
}
