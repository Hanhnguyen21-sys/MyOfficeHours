package s25.cs151.application.Controllers.Schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ScheduleListController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Label dashboardLabel;
    @FXML
    private Label scheduleLabel;

    @FXML
    private Button ScheduleBtn;

    @FXML
    private Button listAllBtn;

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
    private MenuItem searchItem;
    @FXML
    private MenuItem reportItem;
    @FXML
    private Stage stage;

    private ObservableList<Schedule> scheduleObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupNavigationHandlers();
        loadSchedules();
    }

    private void setupTableColumns() {
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        reasonColumn.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }

    private void setupNavigationHandlers() {
        ScheduleBtn.setOnAction(e -> switchTo(new ScheduleSwitcher(stage)));
        listAllBtn.setOnAction(e -> switchTo(new OfficeHoursListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        scheduleLabel.setOnMouseClicked(event -> switchTo(new ScheduleSwitcher(stage)));

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

    private void loadSchedules() {
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/schedule.db");
        Connection connection = connectDB.getConnection();

        if (connection != null) {
            try {

                Statement createstatement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS schedule ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "studentName TEXT, "
                        + "date TEXT, "
                        + "time TEXT, "
                        + "course TEXT, "
                        + "reason TEXT, "
                        + "comment TEXT)";
                // create a table
                createstatement.executeUpdate(createTable);

                Statement statement = connection.createStatement();
                String selectQuery = "SELECT * FROM schedule";
                ResultSet resultSet = statement.executeQuery(selectQuery);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String studentName = resultSet.getString("studentName");
                    String date = resultSet.getString("date");
                    String time = resultSet.getString("time");
                    String course = resultSet.getString("course");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");

                    Schedule schedule = new Schedule(id, studentName, date, time, course, reason, comment);
                    scheduleObservableList.add(schedule);
                }

                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                scheduleTable.setItems(scheduleObservableList);

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

                // sort ascending based on fromHour & toHour
                dateColumn.setSortType(TableColumn.SortType.ASCENDING);
                hiddenFromHourColumn.setSortType(TableColumn.SortType.ASCENDING);
                hiddenToHourColumn.setSortType(TableColumn.SortType.ASCENDING);

                // clear old sorting order & add the new one
                scheduleTable.getSortOrder().clear();
                scheduleTable.getSortOrder().addAll(dateColumn, hiddenFromHourColumn, hiddenToHourColumn);
                scheduleTable.sort();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
