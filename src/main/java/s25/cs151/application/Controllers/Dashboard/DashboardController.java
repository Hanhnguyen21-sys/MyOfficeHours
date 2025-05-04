package s25.cs151.application.Controllers.Dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Controllers.Helpers.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    public AnchorPane root;

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

        // Menu Items
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
            e.printStackTrace();
        }
    }
}


