package s25.cs151.application.Controllers.OfficeHours;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours time slots page.
 * Handles user input for time slots
 */

public class TimeSlotsController implements Initializable {
    /*
    public VBox timeGroup;
    public HBox startTimeGroup;
    public TextField startTimeField;
    public HBox endTimeGroup;
    public TextField endTimeField;
    public VBox titleSection;

    String startTime = startTimeField.getText();
    String endTime = endTimeField.getText();
    String course = courseCode.getText();

    /*
    startTime.isEmpty() || endTime.isEmpty() || course.isEmpty())
    showAlert("Time: " + startTime + " - " + endTime + "\n" +
                "Course: " + course + " - " + section + "\n" +
                "Course Name: " + name);
     // reset text-field
        startTimeField.setText("");
        endTimeField.setText("");
        courseSection.setText("");
        courseCode.setText("");
        courseName.setText("");
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
