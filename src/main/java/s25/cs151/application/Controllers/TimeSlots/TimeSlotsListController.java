package s25.cs151.application.Controllers.TimeSlots;

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
    private MenuItem searchItem;
    @FXML
    private MenuItem reportItem;
    @FXML
    private Stage stage;

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
        OfficeHourBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        TimeslotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        CoursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        listAllBtn.setOnAction(e -> switchTo(new TimeSlotsListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        timeSlotsLabel.setOnMouseClicked(event -> switchTo(new TimeSlotsSwitcher(stage)));

        officeHoursListLabel.setOnMouseClicked(event -> switchTo(new OfficeHoursListSwitcher(stage)));
        timeSlotsListLabel.setOnMouseClicked(event -> switchTo(new TimeSlotsListSwitcher(stage)));
        coursesListLabel.setOnMouseClicked(event -> switchTo(new CourseListSwitcher(stage)));

        dashboardItem.setOnAction(e -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesItem.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        scheduleItem.setOnAction(e -> switchTo(new ScheduleSwitcher(stage)));
        searchItem.setOnAction(e -> switchTo(new SearchSwitcher(stage)));
    }

    private void switchTo(SceneSwitcher switcher) {
        try {
            switcher.switchScene();
        } catch (IOException e) {
            System.err.println("Error switching to view: " + e.getMessage());
            e.printStackTrace();
        }
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
}
