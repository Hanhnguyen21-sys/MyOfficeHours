package s25.cs151.application.Controllers.TimeSlots;

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
import s25.cs151.application.Models.TimeSlots;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TimeSlotsListController implements Initializable {
    @FXML
    private Label dashboardLabel;
    @FXML
    private Label timeSlotsLabel;

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
    private TableView<TimeSlots> timeSlotsTable;
    @FXML
    private TableColumn<TimeSlots, String> fromHourColumn;
    @FXML
    private TableColumn<TimeSlots, String> toHourColumn;

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

    ObservableList<TimeSlots> timeSlotsObservableList = FXCollections.observableArrayList();

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
        fromHourColumn.setCellValueFactory(cellData -> cellData.getValue().fromHourProperty());
        toHourColumn.setCellValueFactory(cellData -> cellData.getValue().toHourProperty());
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
        scheduleItem.setOnAction(e -> {
            try {
                switchToSchedule();
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
        timeSlotsLabel.setOnMouseClicked(event -> {
            try {
                switchToTimeSlots();
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
    }

    /**
     * Loads courses data into the table
     */
    private void loadCourses() {

        try {

            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/timeSlots.db");
            Connection connection = connectDB.getConnection();

            Statement createstatement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS timeSlots ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "fromHour TEXT, "
                    + "toHour TEXT)";
            // create a table
            createstatement.executeUpdate(createTable);

            String selectQuery = "SELECT * FROM timeSlots";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String fromHour = resultSet.getString("fromHour");
                String toHour = resultSet.getString("toHour");
                timeSlotsObservableList.add(new TimeSlots(fromHour, toHour));
            }

            fromHourColumn.setCellValueFactory(new PropertyValueFactory<>("fromHour"));
            toHourColumn.setCellValueFactory(new PropertyValueFactory<>("toHour"));

            // set items to tableview
            timeSlotsTable.setItems(timeSlotsObservableList);

            // use hidden column to set order in descending prio: winter, fall, summer,
            // spring
            TableColumn<TimeSlots, Integer> hiddenFromHourColumn = new TableColumn<>("fromTimeSlots Hidden Order");
            hiddenFromHourColumn.setCellValueFactory(new PropertyValueFactory<>("fromTimeSlotOrder"));

            TableColumn<TimeSlots, Integer> hiddenToHourColumn = new TableColumn<>("toTimeSlots Hidden Order");
            hiddenToHourColumn.setCellValueFactory(new PropertyValueFactory<>("toTimeSlotOrder"));

            // hide from user
            hiddenFromHourColumn.setVisible(false);
            hiddenToHourColumn.setVisible(false);

            // add to table
            timeSlotsTable.getColumns().add(hiddenFromHourColumn);
            timeSlotsTable.getColumns().add(hiddenToHourColumn);

            // sort ascending based on fromHour & toHour
            hiddenFromHourColumn.setSortType(TableColumn.SortType.ASCENDING);
            hiddenToHourColumn.setSortType(TableColumn.SortType.ASCENDING);

            // clear old sorting order & add the new one
            timeSlotsTable.getSortOrder().clear();
            timeSlotsTable.getSortOrder().addAll(hiddenFromHourColumn, hiddenToHourColumn);
            timeSlotsTable.sort();

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
     *
     */
    @FXML
    private void switchToTimeSlotsList() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "Time Slots List");
    }

    /**
     * Switches to the Schedule view
     */
    private void switchToSchedule() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule");
    }
}
