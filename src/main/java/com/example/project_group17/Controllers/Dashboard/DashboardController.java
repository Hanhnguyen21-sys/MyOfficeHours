package com.example.project_group17.Controllers.Dashboard;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public AnchorPane root1;
    public Button officeHoursBtn1;
    public Button scheduleBtn1;
    public Button reportBtn1;

    // Dashboard button and dropdown menu
    public MenuButton dashboardBtn1;
    public MenuItem officeHoursMenuItem;
    public MenuItem scheduleMenuItem;
    public MenuItem reportMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Handle the Office Hours button click
        officeHoursBtn1.setOnMouseClicked(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /*
         * // Handle the Schedule button click
         * scheduleBtn1.setOnMouseClicked(event -> {
         * // Implement functionality for schedule
         * });
         * // Handle the Report button click
         * reportBtn1.setOnMouseClicked(event -> {
         * // Implement functionality for report
         * });
         */

        // switch back to dashboard when click
        dashboardBtn1.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Handle the Office Hours menu item click
        officeHoursMenuItem.setOnAction(event -> {
            try {
                switchToOfficeHours();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /*
         * // Handle the Schedule menu item click
         * scheduleMenuItem.setOnAction(event -> {
         * // Implement functionality for schedule
         * });
         * 
         * // Handle the Report menu item click
         * reportMenuItem.setOnAction(event -> {
         * // Implement functionality for report
         * });
         */
    }

    // Method to switch to the Office Hours page
    private void switchToOfficeHours() throws IOException {
        // Load the Office Hours FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/OfficeHours/OfficeHours.fxml"));
        Parent officeHours = loader.load();

        // Get the current stage (window) and set the scene to the Office Hours page
        Scene officeHoursScene = new Scene(officeHours);
        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setTitle("Office Hours");
        stage.setScene(officeHoursScene);
        stage.show();
    }

    public void switchToDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard/Dashboard.fxml"));
        Parent dashboard = loader.load();
        Scene dashboardScene = new Scene(dashboard);
        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(dashboardScene);
        stage.show();
    }
}