package s25.cs151.application.Controllers.OfficeHours;

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
import s25.cs151.application.Models.OfficeHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours List view.
 * Displays and manages the table of office hours entries.
 */
public class OfficeHoursListController implements Initializable {
    @FXML
    private Label dashboardLabel;

    @FXML
    private Label officeHoursLabel;

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
    private TableView<OfficeHours> officeHoursTable;
    @FXML
    private TableColumn<OfficeHours, String> semesterColumn;
    @FXML
    private TableColumn<OfficeHours, String> yearColumn;
    @FXML
    private TableColumn<OfficeHours, String> daysColumn;
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

    ObservableList<OfficeHours> officeHoursObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns and button actions
        setupTableColumns();
        setupButtonActions();
        loadOfficeHours();
    }

    /**
     * Configures the table columns to display office hours data
     */
    private void setupTableColumns() {
        semesterColumn.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        daysColumn.setCellValueFactory(cellData -> cellData.getValue().daysProperty());
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
        officeHoursLabel.setOnMouseClicked(event -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException e) {
                System.err.println("Error switching to new office hours view:" + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        // switch to Office Hours form
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

        // switch to time slots view
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

        // switch to courses label
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
        // Handle the Courses menu item click
        scheduleItem.setOnAction(event -> {
            try {
                switchToSchedule();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Loads office hours data into the table
     */
    private void loadOfficeHours() {

        try {

            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/officehours.db");
            Connection connection = connectDB.getConnection();

            Statement createstatement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS officeHours ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "semester TEXT, "
                    + "year TEXT, "
                    + "days TEXT)";
            // create a table
            createstatement.executeUpdate(createTable);

            String selectQuery = "SELECT * FROM officeHours";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String semester = resultSet.getString("semester");
                String year = resultSet.getString("year");
                String days = resultSet.getString("days");
                officeHoursObservableList.add(new OfficeHours(semester, year, days));
            }
            semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

            // set items to tableview
            officeHoursTable.setItems(officeHoursObservableList);

            // use hidden column to set order in descending prio: winter, fall, summer,
            // spring
            TableColumn<OfficeHours, Integer> hiddenSemColumn = new TableColumn<>("Semester Hidden Order");
            hiddenSemColumn.setCellValueFactory(new PropertyValueFactory<>("semesterOrder"));

            // hide from user
            hiddenSemColumn.setVisible(false);

            // add to table
            officeHoursTable.getColumns().add(hiddenSemColumn);

            // sort descending based on year and semester
            yearColumn.setSortType(TableColumn.SortType.DESCENDING);
            hiddenSemColumn.setSortType(TableColumn.SortType.DESCENDING);

            // clear old sorting order & add the new one
            officeHoursTable.getSortOrder().clear();
            officeHoursTable.getSortOrder().addAll(yearColumn, hiddenSemColumn);
            officeHoursTable.sort();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the selected office hours entry
     * TODO: Implement database integration
     */
    private void deleteOfficeHours(OfficeHours officeHours) {
        // TODO: Implement delete functionality
        // This will be implemented when we add database functionality
        showAlert("Delete functionality will be implemented with database integration.");
    }

    /**
     * Shows an alert dialog with the given message
     * 
     * @param message to be sent to user
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    private void switchToTimeSlots() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
    }

    /**
     * Switches to the Courses View
     */
    private void switchToCourses() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
    }

    /**
     * Switches to the edit office hours form view
     *
     * @param officeHours The office hours entry to edit
     */
    private void switchToEditOfficeHoursView(OfficeHours officeHours) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Edit Office Hours");
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
