package s25.cs151.application.Controllers.Edit;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import s25.cs151.application.Models.ConnectDB;
import s25.cs151.application.Models.Schedule;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PopupEditController implements Initializable {
    @FXML
    private TextField studentName;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> timeSlotComboBox;
    @FXML
    private ComboBox<String> courseComboBox;
    @FXML
    private TextField reasonField;
    @FXML
    private TextField commentField;
    @FXML
    private AnchorPane root;

    private Schedule schedule;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            //load all options of timeSlots and Courses to Dialog
            List<String> timeSlots = loadTime();
            List<String> courses = loadCourses();
            timeSlotComboBox.getItems().addAll(timeSlots);
            courseComboBox.getItems().addAll(courses);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
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
    //set values with whatever users enter from the dialog
    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
        studentName.setText(schedule.getStudentName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(schedule.getDate(), formatter);
        dateField.setValue(localDate);
        timeSlotComboBox.setValue(schedule.getTime());
        courseComboBox.setValue(schedule.getCourse());
        commentField.setText(schedule.getComment());
        reasonField.setText(schedule.getReason());
    }
    //update the values that users enter to schedule object
    public boolean updateSchedule(){
        schedule.setName(studentName.getText());
        if(studentName.getText().isEmpty())
        {
            showAlert("Please fill in required field: Student Name");
            return false;
        }
        schedule.setTime(timeSlotComboBox.getValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        schedule.setDate(dateField.getValue().format(formatter));
        schedule.setCourse(courseComboBox.getValue());
        schedule.setComment(commentField.getText());
        schedule.setReason(reasonField.getText());
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
