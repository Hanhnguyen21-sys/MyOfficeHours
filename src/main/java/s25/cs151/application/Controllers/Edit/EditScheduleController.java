package s25.cs151.application.Controllers.Edit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.*;
import s25.cs151.application.Models.ConnectDB;
import s25.cs151.application.Models.Schedule;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditScheduleController implements Initializable {

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
    private MenuItem editScheduleItem;
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
    ConnectDB connectDB = new ConnectDB(DB_URL);
    Connection connection = connectDB.getConnection();
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
        editBtn.setOnAction(e->{
            Schedule schedule = scheduleTable.getSelectionModel().getSelectedItem();
            try {
                editStudentSchedule(schedule);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setupNavigationHandlers() {

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));

        dashboardItem.setOnAction(e -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesItem.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        scheduleItem.setOnAction(e -> switchTo(new ScheduleSwitcher(stage)));
        searchItem.setOnAction(e -> switchTo(new SearchSwitcher(stage)));
        editScheduleItem.setOnAction(e -> switchTo(new EditScheduleSwitcher(stage)));

    }

    private void switchTo(SceneSwitcher switcher) {
        try {
            switcher.switchScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void editStudentSchedule(Schedule schedule) throws IOException {
        //load fxml of edit dialog
        // Load the FXML file

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Edit/PopupEdit.fxml"));
        DialogPane dialogPane = loader.load();

        if(schedule!=null){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Schedule");
            dialog.setHeaderText("Edit Schedule Information");
            dialog.setDialogPane(dialogPane);
            PopupEditController controller = loader.getController();
            controller.setSchedule(schedule);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Update the schedule object with values from the form
                controller.updateSchedule();
//
                // Save updated data to database
                updateToDatabase(schedule);
            }
        }
        else{
            showInfoAlert("No Selection", "Please select a schedule entry to delete.");
        }
    }
    private void updateToDatabase(Schedule schedule) {
        if(connection!=null){
            try{
                String updateQuery ="UPDATE schedule SET studentName = ?, date = ?, time = ?, course =?, reason =?, comment =? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1,schedule.getStudentName());
                preparedStatement.setString(2,schedule.getDate());
                preparedStatement.setString(3,schedule.getTime());
                preparedStatement.setString(4,schedule.getCourse());
                preparedStatement.setString(5,schedule.getReason());
                preparedStatement.setString(6,schedule.getComment());
                preparedStatement.setInt(7,schedule.getId());
                int updatedRow = preparedStatement.executeUpdate();
                if (updatedRow>0){
                    showInfoAlert("Success", "Update Successfully!");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
