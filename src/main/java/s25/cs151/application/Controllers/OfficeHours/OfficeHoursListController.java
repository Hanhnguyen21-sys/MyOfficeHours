package s25.cs151.application.Controllers.OfficeHours;

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
    @FXML
    private Label dashboardLabel;

    @FXML
    private Label officeHoursLabel;

    @FXML
    private Button officeHourBtn;
    @FXML
    private Button timeslotsBtn;
    @FXML
    private Button CoursesBtn;
    @FXML
    private Button listAllBtn;

    @FXML
    private Label officeHoursListLabel;
    @FXML
    private Label timeSlotsListLabel;
    @FXML
    private Label coursesListLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<OfficeHours> officeHoursTable;
    @FXML
    private TableColumn<OfficeHours, String> semesterColumn;
    @FXML
    private TableColumn<OfficeHours, String> yearColumn;
    @FXML
    private TableColumn<OfficeHours, String> daysColumn;
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

    ObservableList<OfficeHours> officeHoursObservableList = FXCollections.observableArrayList();
    @FXML
    private Stage stage;

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
    }

    /**
     * Sets up event handlers for all buttons and MenuItems
     */
    private void setupButtonActions() {

        officeHourBtn.setOnAction(e -> switchTo(new OfficeHoursSwitcher(stage)));
        timeslotsBtn.setOnAction(e -> switchTo(new TimeSlotsSwitcher(stage)));
        CoursesBtn.setOnAction(e -> switchTo(new CoursesSwitcher(stage)));
        listAllBtn.setOnAction(e -> switchTo(new OfficeHoursListSwitcher(stage)));

        dashboardLabel.setOnMouseClicked(event -> switchTo(new DashboardSwitcher(stage)));
        officeHoursLabel.setOnMouseClicked(event -> switchTo(new OfficeHoursSwitcher(stage)));

        officeHoursListLabel.setOnMouseClicked(event -> switchTo(new OfficeHoursListSwitcher(stage)));
        timeSlotsListLabel.setOnMouseClicked(event -> switchTo(new TimeSlotsListSwitcher(stage)));
        coursesListLabel.setOnMouseClicked(event -> switchTo(new CourseListSwitcher(stage)));

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
            System.err.println("Error switching view: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * Loads office hours data into the table
     */
    private void loadOfficeHours() {

        try {

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

            while (resultSet.next()) {
                String semester = resultSet.getString("semester");
                String year = resultSet.getString("year");
                String days = resultSet.getString("days");
                officeHoursObservableList.add(new OfficeHours(semester, year, days));
            }
            semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

            // set items to tableview
            officeHoursTable.setItems(officeHoursObservableList);

            // use hidden column to set order in descending prio: winter, fall, summer,
            // spring
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
     *
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
     * Switches to the edit office hours form view
     *
     * @param officeHours The office hours entry to edit

    private void switchToEditOfficeHoursView(OfficeHours officeHours) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        SwitchScene.switchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Edit Office Hours");
    }
    */

}
