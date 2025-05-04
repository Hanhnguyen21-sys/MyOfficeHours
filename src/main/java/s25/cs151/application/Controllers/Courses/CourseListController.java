package s25.cs151.application.Controllers.Courses;

import javafx.application.Platform;
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
    private Button ListAllBtn;

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
    @FXML
    private MenuItem searchItem;
    @FXML
    private Stage stage;
    @FXML
    private MenuItem editScheduleItem;

    ObservableList<Courses> coursesObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) root.getScene().getWindow();
        });

        // Set up table columns and button actions
        setupTableColumns();
        setupNavigationHandlers();
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
    private void setupNavigationHandlers() {
        OfficeHourBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        TimeslotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        CoursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        ListAllBtn.setOnAction(e -> switchTo(new CourseListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        coursesLabel.setOnMouseClicked(event -> switchTo(new CoursesSwitcher(stage)));
        officeHoursListLabel.setOnMouseClicked(event -> switchTo(new OfficeHoursListSwitcher(stage)));
        timeSlotsListLabel.setOnMouseClicked(event -> switchTo(new TimeSlotsListSwitcher(stage)));
        coursesListLabel.setOnMouseClicked(event -> switchTo(new CourseListSwitcher(stage)));

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
            System.err.println("Error switching to view: " + e.getMessage());
            e.printStackTrace();
        }
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
}
