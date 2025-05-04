package s25.cs151.application.Controllers.Search;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import s25.cs151.application.Controllers.Helpers.*;
import s25.cs151.application.Models.Schedule;
import s25.cs151.application.Models.ConnectDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import java.util.Optional;

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
    @FXML
    private Stage stage;

    private ObservableList<Schedule> searchObservableList = FXCollections.observableArrayList();
    private final String DB_URL = "jdbc:sqlite:src/main/resources/Database/schedule.db";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) root.getScene().getWindow();
        });

        setupTableColumns();
        setupNavigationHandlers();
        loadSearchedSchedules("");

        searchBtn.setOnAction(e -> performSearch());
        searchStudent.setOnAction(e -> performSearch());
        deleteBtn.setOnAction(e -> deleteSelected());
    }

    private void setupNavigationHandlers() {

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));

        dashboardItem.setOnAction(event -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(event -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesItem.setOnAction(event -> switchTo(new CoursesSwitcher(stage)));
        scheduleItem.setOnAction(event -> switchTo(new ScheduleSwitcher(stage)));
        searchItem.setOnAction(event -> switchTo(new SearchSwitcher(stage)));
    }

    private void switchTo(SceneSwitcher switcher) {
        try {
            switcher.switchScene();
        } catch (IOException e) {
            System.err.println("Error switching to view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelected() {
        Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            showInfoAlert("No Selection", "Please select a schedule entry to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Schedule Entry");
        alert.setContentText("Are you sure you want to delete the schedule for " +
                selectedSchedule.getStudentName() + " on " + selectedSchedule.getDate() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (deleteScheduleFromDB(selectedSchedule.getId())) {
                searchObservableList.remove(selectedSchedule);
                showInfoAlert("Success", "Schedule deleted successfully.");
            } else {
                showErrorAlert("Deletion Failed", "Could not delete the schedule from the database.");
            }
        }
    }

    private boolean deleteScheduleFromDB(int scheduleId) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        ConnectDB connectDB = new ConnectDB(DB_URL);
        Connection conn = connectDB.getConnection();

        if (conn == null) {
            showErrorAlert("Database Error", "Failed to connect to the database for deletion.");
            return false;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Error deleting schedule: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing connection: " + ex.getMessage());
            }
        }
    }

    private void setupTableColumns() {
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        reasonColumn.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }

    private void performSearch() {
        searchObservableList.clear();
        String searchText = searchStudent.getText().trim();
        loadSearchedSchedules(searchText);
    }

    private void loadSearchedSchedules(String searchText) {
        searchObservableList.clear();
        ConnectDB connectDB = new ConnectDB(DB_URL);
        Connection connection = connectDB.getConnection();

        if (connection != null) {
            try (Statement createstatement = connection.createStatement()) {
                String createTable = "CREATE TABLE IF NOT EXISTS schedule ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "studentName TEXT, "
                        + "date TEXT, "
                        + "time TEXT, "
                        + "course TEXT, "
                        + "reason TEXT, "
                        + "comment TEXT)";
                createstatement.executeUpdate(createTable);
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to ensure schedule table exists: " + e.getMessage());
                try {
                    connection.close();
                } catch (SQLException ex) {
                    /* ignore */ }
                return;
            }

            String selectQuery = "SELECT id, studentName, date, time, course, reason, comment " +
                    "FROM   schedule " +
                    "WHERE  studentName LIKE ? COLLATE NOCASE " +
                    "ORDER BY date DESC, time DESC";

            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                pstmt.setString(1, "%" + searchText + "%");
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String studentName = resultSet.getString("studentName");
                    String date = resultSet.getString("date");
                    String time = resultSet.getString("time");
                    String course = resultSet.getString("course");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");

                    Schedule schedule = new Schedule(id, studentName, date, time, course, reason, comment);
                    searchObservableList.add(schedule);
                }
                scheduleTable.setItems(searchObservableList);
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                scheduleTable.setItems(searchObservableList);

                // use hidden column for time sorting
                TableColumn<Schedule, Integer> hiddenFromHourColumn = new TableColumn<>("fromTimeSlots Hidden Order");
                hiddenFromHourColumn.setCellValueFactory(new PropertyValueFactory<>("fromTimeOrder"));

                TableColumn<Schedule, Integer> hiddenToHourColumn = new TableColumn<>("toTimeSlots Hidden Order");
                hiddenToHourColumn.setCellValueFactory(new PropertyValueFactory<>("toTimeOrder"));

                // hide from user
                hiddenFromHourColumn.setVisible(false);
                hiddenToHourColumn.setVisible(false);

                scheduleTable.getColumns().add(hiddenFromHourColumn);
                scheduleTable.getColumns().add(hiddenToHourColumn);

                // sort descending based on fromHour & toHour
                dateColumn.setSortType(TableColumn.SortType.DESCENDING);
                hiddenFromHourColumn.setSortType(TableColumn.SortType.DESCENDING);
                hiddenToHourColumn.setSortType(TableColumn.SortType.DESCENDING);

                // clear old sorting order & add the new one
                scheduleTable.getSortOrder().clear();
                scheduleTable.getSortOrder().addAll(dateColumn, hiddenFromHourColumn, hiddenToHourColumn);
                scheduleTable.sort();

            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to load schedules: " + e.getMessage());
            } finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException ex) {
                    System.err.println("Error closing connection: " + ex.getMessage());
                }
            }
        } else {
            showErrorAlert("Database Error", "Failed to connect to the database.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
