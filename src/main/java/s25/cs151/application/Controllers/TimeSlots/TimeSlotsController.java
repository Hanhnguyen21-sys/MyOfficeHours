package s25.cs151.application.Controllers.TimeSlots;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

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

    public void handleSaveButton(ActionEvent event) {
    }

    public void switchToDashboard(MouseEvent mouseEvent) {

    }
}
