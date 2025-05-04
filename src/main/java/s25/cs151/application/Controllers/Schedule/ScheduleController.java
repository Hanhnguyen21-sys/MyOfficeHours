package s25.cs151.application.Controllers.Schedule;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.*;
import s25.cs151.application.Models.ConnectDB;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField studentNameField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> timeField;
    @FXML
    private ComboBox<String> courseField;
    @FXML
    private TextArea reasonField;
    @FXML
    private TextArea commentField;
    //button
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button listAllBtn;
    @FXML
    private Button ScheduleBtn;
    //label
    @FXML
    private Label dashboardLabel;
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


    public void initialize(URL location, ResourceBundle resources) {
        //set up default values for ComboBox and DatePicker

        try {
            List<String> courses = loadCourses();
            List<String> timeSlots = loadTime();
            timeField.getItems().addAll(timeSlots);
            timeField.getSelectionModel().selectFirst();
            courseField.getItems().addAll(courses);
            courseField.getSelectionModel().selectFirst();
            dateField.setValue(LocalDate.now());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setupNavigationHandlers();
    }

    /**
     * The function loads time slots from database to comboBox for users to pick
     * @return - list of time slots
     */
    private List<String> loadTime() throws SQLException {
        List<String> timeSlots = new ArrayList<>();
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/timeSlots.db");
        Connection connection = connectDB.getConnection();
        String selectQuery = "SELECT * FROM timeSlots";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        while ((resultSet.next())){
            String fromHour = resultSet.getString("fromHour");
            String toHour = resultSet.getString("toHour");
            timeSlots.add(fromHour + " - " +toHour);
        }
        return timeSlots;
    }
    /**
     * The function loads time courses from database to comboBox for users to pick
     * only need to display course's code and course's section in the ComboBox
     * @return - list of courses
     */
    private List<String> loadCourses() throws SQLException {
        List<String> courses = new ArrayList<>();
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/courses.db");
        Connection connection = connectDB.getConnection();

        String selectQuery = "SELECT courseCode, sectionNumber FROM courses";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        while(resultSet.next()){
            String code = resultSet.getString("courseCode");
            String section = resultSet.getString("sectionNumber");
            courses.add(code + " - " + section);
        }
        return courses;
    }

    private void setupNavigationHandlers(){

        ScheduleBtn.setOnAction(e -> resetForm());
        listAllBtn.setOnAction(e->switchTo(new ScheduleListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        cancelBtn.setOnAction(e -> resetForm());
        saveBtn.setOnAction(e -> handleSaveButton());

        dashboardItem.setOnAction(event -> switchTo(new DashboardSwitcher(stage)));
        officehoursItem.setOnAction(event -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsItem.setOnAction(event -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesItem.setOnAction(event -> switchTo(new CoursesSwitcher(stage)));
        scheduleItem.setOnAction(event -> resetForm());
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleSaveButton() {
        ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/schedule.db");
        Connection connection = connectDB.getConnection();
        String studentName = studentNameField.getText();
        String date = String.valueOf(dateField.getValue());
        String time = timeField.getValue();
        String course = courseField.getValue();
        String reason = reasonField.getText();
        String comment= commentField.getText();

        if(studentName.isEmpty())
        {
            showAlert("Please fill in required field: Student Name");
            return;
        }

        if(connection!=null){
            try{
                Statement statement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS schedule ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "studentName TEXT, "
                        + "date TEXT, "
                        + "time TEXT, "
                        + "course TEXT, "
                        + "reason TEXT, "
                        + "comment TEXT)";
                //create a table
                statement.executeUpdate(createTable);
                //inserting into the table
                String insertQuery = "INSERT INTO schedule (studentName, date, time, course, reason, comment) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1,studentName);
                preparedStatement.setString(2,date);
                preparedStatement.setString(3,time);
                preparedStatement.setString(4,course);
                preparedStatement.setString(5,reason);
                preparedStatement.setString(6,comment);
                preparedStatement.executeUpdate();
                showAlert("Successfully Saved!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Resets all form fields to their default values
     */
    private void resetForm() {
        studentNameField.setText("");
        dateField.setValue(LocalDate.now());
        timeField.getSelectionModel().selectFirst();
        courseField.getSelectionModel().selectFirst();
        reasonField.setText("");
        commentField.setText("");
    }

}
