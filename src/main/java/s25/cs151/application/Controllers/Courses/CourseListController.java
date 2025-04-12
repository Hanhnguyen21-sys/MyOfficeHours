package s25.cs151.application.Controllers.Courses;

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
import s25.cs151.application.Models.Courses;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CourseListController implements Initializable {
    @FXML
    private Label dashboardLabel;
    @FXML
    private Label coursesLabel;

    @FXML
    private Button OfficeHourBtn;
    @FXML
    private Button TimeslotsBtn;
    @FXML
    private Button CoursesBtn;
    @FXML
    private Button listAllBtn;

    @FXML
    private Label officeHoursListLabel;
    @FXML
    private Label timeSlotsListLabel;
    @FXML
    private Label coursesListLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Courses> coursesTable;
    @FXML
    private TableColumn<Courses, String> courseCodeColumn;
    @FXML
    private TableColumn<Courses, String> courseNameColumn;
    @FXML
    private TableColumn<Courses, String> sectionNumberColumn;

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

    ObservableList<Courses> coursesObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns and button actions
        setupTableColumns();
        setupButtonActions();
        loadCourses();
    }

    /**
     * Configures the table columns to display courses data
     */
    private void setupTableColumns() {
        courseCodeColumn.setCellValueFactory(cellData -> cellData.getValue().courseCodeProperty());
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
        sectionNumberColumn.setCellValueFactory(cellData -> cellData.getValue().sectionNumberProperty());
    }

    /**
     * Sets up event handlers for all buttons and MenuItems
     */
    private void setupButtonActions() {
        // Dashboard button
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        // switch to Office Hours form
        officeHoursListLabel.setOnMouseClicked(e -> {
            try {
                switchToOfficeHoursList();
            } catch (IOException ex) {
                System.err.println("Error switching to new office hours view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        OfficeHourBtn.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                System.err.println("Error switching to new office hours view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        officehoursItem.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                System.err.println("Error switching to new office hours view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        // switch to TimeSlots
        timeSlotsListLabel.setOnMouseClicked(e -> {
            try {
                switchToTimeSlotsList();
            } catch (IOException ex) {
                System.err.println("Error switching to time slot view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        TimeslotsBtn.setOnAction(event -> {
            try {
                switchToTimeSlots();
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

        // switch to courses views
        coursesListLabel.setOnMouseClicked(e -> {
            try {
                switchToCoursesList();
            } catch (IOException ex) {
                System.err.println("Error switching to courses view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        coursesLabel.setOnMouseClicked(event -> {
            try {
                switchToCourses();
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
    }

    /**
     * Loads courses data into the table
     */
    private void loadCourses() {

        try {

            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/courses.db");
            Connection connection = connectDB.getConnection();

            Statement createstatement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS courses ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "courseCode TEXT, "
                    + "courseName TEXT, "
                    + "sectionNumber TEXT)";
            // create a table
            createstatement.executeUpdate(createTable);

            String selectQuery = "SELECT * FROM courses";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String courseCode = resultSet.getString("courseCode");
                String courseName = resultSet.getString("courseName");
                String sectionNumber = resultSet.getString("sectionNumber");
                coursesObservableList.add(new Courses(courseCode, courseName, sectionNumber));
            }
            courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
            sectionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));

            // set items to tableview
            coursesTable.setItems(coursesObservableList);

            // sort descending based on code, name & section
            courseCodeColumn.setSortType(TableColumn.SortType.DESCENDING);
            courseNameColumn.setSortType(TableColumn.SortType.DESCENDING);
            sectionNumberColumn.setSortType(TableColumn.SortType.DESCENDING);

            // clear old sorting order & add the new one
            coursesTable.getSortOrder().clear();
            coursesTable.getSortOrder().addAll(courseCodeColumn, courseNameColumn, sectionNumberColumn);
            coursesTable.sort();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    /**
     * Switches to the new office hours form view
     */
    @FXML
    private void switchToNewOfficeHoursView() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }

    /**
     * Switches to the Time Slots view
     */
    @FXML
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }

    /**
     * Switches to the Courses View
     */
    @FXML
    private void switchToCourses() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }

    /**
     * Switches to course list view
     *
     */
    @FXML
    private void switchToCoursesList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/CourseList.fxml", "Courses List");
    }

    /**
     * Switches to office hours list view
     *
     */
    @FXML
    private void switchToOfficeHoursList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHoursList.fxml", "Office Hours List");
    }

    /**
     * Switches to Time Slots List View
     */
    @FXML
    private void switchToTimeSlotsList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "Time Slots List");
    }

    /**
     * Switches to Schedule List View
     */
    @FXML
    private void switchToScheduleList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/ScheduleList.fxml", "Schedule List");
    }

    /**
     * Switches to Time Slots List View
     */
    @FXML
    private void switchToSchedule() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule View");
    }

}
