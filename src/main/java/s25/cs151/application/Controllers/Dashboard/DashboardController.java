package s25.cs151.application.Controllers.Dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button editScheduleBtn;
    @FXML
    private Button officeHoursBtn;
    @FXML
    private Button timeSlotsBtn;
    @FXML
    private Button coursesBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button reportBtn;
    @FXML
    private Button searchBtn;

    @FXML
    private RadioMenuItem dashboardItem;
    @FXML
    private RadioMenuItem officehoursItem;
    @FXML
    private RadioMenuItem timeslotsItem;
    @FXML
    private RadioMenuItem coursesItem;
    @FXML
    private RadioMenuItem scheduleItem;
    @FXML
    private RadioMenuItem searchItem;
    @FXML
    private Stage stage;
    @FXML
    private RadioMenuItem reportItem;
    @FXML
    private RadioMenuItem editScheduleItem;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) root.getScene().getWindow();
        });

        setupNavigationHandlers();
    }

    private void setupNavigationHandlers() {
        // Buttons
        officeHoursBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeSlotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        coursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        scheduleBtn.setOnAction(e -> switchTo(new ScheduleSwitcher(stage)));
        searchBtn.setOnAction(e -> switchTo(new SearchSwitcher(stage)));
        editScheduleBtn.setOnAction(e -> switchTo(new EditScheduleSwitcher(stage)));

        // Menu Items
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
}


