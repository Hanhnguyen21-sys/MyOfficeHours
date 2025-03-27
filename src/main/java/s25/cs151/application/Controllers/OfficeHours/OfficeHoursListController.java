package s25.cs151.application.Controllers.OfficeHours;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import s25.cs151.application.Helper.SwitchScene;
import s25.cs151.application.Models.ConnectDB;
import s25.cs151.application.Models.OfficeHours;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller for the Office Hours List view.
 * Displays and manages the table of office hours entries.
 */
public class OfficeHoursListController implements Initializable {
    public Label dashboardLabel;
    public Button OfficeHourBtn;
    @FXML
    public AnchorPane root;
    @FXML
    public MenuButton menuBtn;
    @FXML
    public TableView<OfficeHours> officeHoursTable;
    @FXML
    public TableColumn<OfficeHours, String> semesterColumn;
    @FXML
    public TableColumn<OfficeHours, String> yearColumn;
    @FXML
    public TableColumn<OfficeHours, String> daysColumn;
    public MenuItem dashboardItem;
    public MenuItem officehoursItem;
    //    @FXML
//    private TableColumn<OfficeHours, String> timeColumn;
//    @FXML
//    private TableColumn<OfficeHours, String> courseColumn;
//    @FXML
//    private Button newBtn;
//    @FXML
//    private Button editBtn;
//    @FXML
//    private Button deleteBtn;
    ObservableList<OfficeHours> officeHoursObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns and button actions
        setupTableColumns();
        setupButtonActions();
        loadOfficeHours();
    }

    /**
     * Configures the table columns to display office hours data
     */
    private void setupTableColumns() {
        semesterColumn.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        daysColumn.setCellValueFactory(cellData -> cellData.getValue().daysProperty());
//        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
//        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
    }

    /**
     * Sets up event handlers for all buttons and MenuItems
     */
    private void setupButtonActions() {
        // Dashboard button
        dashboardLabel.setOnMouseClicked(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        dashboardItem.setOnAction(event -> {
            try {
                switchToDashboard();
            } catch (IOException e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        // switch to Office Hours form
        OfficeHourBtn.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                System.err.println("Error switching to new office hours view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        officehoursItem.setOnAction(e -> {
            try {
                switchToNewOfficeHoursView();
            } catch (IOException ex) {
                System.err.println("Error switching to new office hours view: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });


        // Edit button
//        editBtn.setOnAction(e -> {
//            OfficeHours selected = officeHoursTable.getSelectionModel().getSelectedItem();
//            if (selected != null) {
//                try {
//                    System.out.println("Edit button clicked");
//                    switchToEditOfficeHoursView(selected);
//                } catch (IOException ex) {
//                    System.err.println("Error switching to edit office hours view: " + ex.getMessage());
//                    ex.printStackTrace();
//                    throw new RuntimeException(ex);
//                }
//            } else {
//                showAlert("Please select an office hours entry to edit.");
//            }
//        });
//
//        // Delete button
//        deleteBtn.setOnAction(e -> {
//            OfficeHours selected = officeHoursTable.getSelectionModel().getSelectedItem();
//            if (selected != null) {
//                deleteOfficeHours(selected);
//            } else {
//                showAlert("Please select an office hours entry to delete.");
//            }
//        });
    }

    /**
     * Loads office hours data into the table
     */
    private void loadOfficeHours() {

        try{

            ConnectDB connectDB = new ConnectDB("jdbc:sqlite:src/main/resources/Database/officehours.db");
            Connection connection = connectDB.getConnection();

            Statement createstatement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS officeHours ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "semester TEXT, "
                    + "year TEXT, "
                    + "days TEXT)";
            // create a table
            createstatement.executeUpdate(createTable);

            String selectQuery = "SELECT * FROM officeHours";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()){
                String semester = resultSet.getString("semester");
                String year = resultSet.getString("year");
                String days  = resultSet.getString("days");
                officeHoursObservableList.add(new OfficeHours(semester,year,days));
            }
            semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

            //set items to tableview
            officeHoursTable.setItems(officeHoursObservableList);

            // use hidden column to set order in descending prio: winter, fall, summer, spring
            TableColumn<OfficeHours, Integer> hiddenSemColumn = new TableColumn<>("Semester Hidden Order");
            hiddenSemColumn.setCellValueFactory(new PropertyValueFactory<>("semesterOrder"));

            // hide from user
            hiddenSemColumn.setVisible(false);

            // add to table
            officeHoursTable.getColumns().add(hiddenSemColumn);

            // sort descending based on year and semester
            yearColumn.setSortType(TableColumn.SortType.DESCENDING);
            hiddenSemColumn.setSortType(TableColumn.SortType.DESCENDING);

            // clear old sorting order & add the new one
            officeHoursTable.getSortOrder().clear();
            officeHoursTable.getSortOrder().addAll(yearColumn, hiddenSemColumn);
            officeHoursTable.sort();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the selected office hours entry
     * TODO: Implement database integration
     */
    private void deleteOfficeHours(OfficeHours officeHours) {
        // TODO: Implement delete functionality
        // This will be implemented when we add database functionality
        showAlert("Delete functionality will be implemented with database integration.");
    }

    /**
     * Shows an alert dialog with the given message
     * @param message to be sent to user
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
    }

    /**
     * Switches to the new office hours form view
     */
    private void switchToNewOfficeHoursView() throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
    }

    /**
     * Switches to the edit office hours form view
     *
     * @param officeHours The office hours entry to edit
     */
    private void switchToEditOfficeHoursView(OfficeHours officeHours) throws IOException {
        Stage stage = (Stage)root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Edit Office Hours");
    }
}
