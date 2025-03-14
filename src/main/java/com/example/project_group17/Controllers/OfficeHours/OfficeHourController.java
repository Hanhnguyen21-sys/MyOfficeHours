package com.example.project_group17.Controllers.OfficeHours;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OfficeHourController implements Initializable {
    public AnchorPane root;
    public MenuButton dashboardBtn;
    public VBox mainContainer;
    public VBox topForm;
    public VBox semesterGroup;
    public Label semesterLabel;
    public ChoiceBox<String> semesterCombo;
    public VBox yearGroup;
    public Label yearLabel;
    public StackPane yearFieldContainer;
    public TextField yearField;
    public VBox dayGroup;
    public HBox dayCheckboxes;
    public CheckBox monCheck;
    public CheckBox tueCheck;
    public CheckBox wedCheck;
    public CheckBox friCheck;
    public CheckBox thurCheck;
    public VBox timeGroup;
    public HBox startTimeGroup;
    public TextField startTimeField;
    public HBox endTimeGroup;
    public TextField endTimeField;
    public VBox titleSection;
    public HBox actionBtn;
    public Button listAllBtn;
    public Button newBtn;
    public TextField courseSection;
    public TextField courseCode;
    public TextField courseName;
    public Button cancelBtn;
    public Button saveBtn;

    private String[] semester = {"Spring","Summer","Fall","Winter"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // semester choices
        semesterCombo.getItems().addAll(semester);
        semesterCombo.setValue("Spring");
        //switch back to dashboard when click
        dashboardBtn.setOnMouseClicked(event -> {
            try {
                switchToDashboard(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // "Cancel" button is used to refresh the form
        cancelBtn.setOnAction(e-> resetForm());

        // "Save" button is used to show successfully saved message and save the information
        // "New Office Hours" button is used to show up the form
        // "List All" button is used to show up table containing all office hours of users

    }

    private void resetForm() {
        // reset the semester combo choice
        semesterCombo.setValue(semesterCombo.getItems().get(0));

        // reset text-field

        startTimeField.setText("");
        endTimeField.setText("");
        yearField.setText("");
        courseSection.setText("");
        courseCode.setText("");
        courseName.setText("");

        //clear checkboxes
        monCheck.setSelected(false);
        tueCheck.setSelected(false);
        wedCheck.setSelected(false);
        thurCheck.setSelected(false);
        friCheck.setSelected(false);
    }

    public void switchToDashboard(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml"));
        Parent dashboard = loader.load();
        Scene scene = new Scene(dashboard);
        Stage stage = (Stage)root.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}

