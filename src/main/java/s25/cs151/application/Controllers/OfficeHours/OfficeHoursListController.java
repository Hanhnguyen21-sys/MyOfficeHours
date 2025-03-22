package s25.cs151.application.Controllers.OfficeHours;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import s25.cs151.application.Models.ConnectDB;
import s25.cs151.application.Models.OfficeHours;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
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
    @FXML
    public TableColumn<OfficeHours, Void> actionColumn;
    public MenuItem dashboardItem;
    public MenuItem officehoursItem;

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

        // Set up the action column with delete button
        setupActionColumn();
    }

    /**
     * Sets up the action column with a delete button
     */
    private void setupActionColumn() {
        Callback<TableColumn<OfficeHours, Void>, TableCell<OfficeHours, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<OfficeHours, Void> call(final TableColumn<OfficeHours, Void> param) {
                return new TableCell<>() {
                    private final Button deleteBtn = new Button("Delete");
                    {
                        deleteBtn.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white;");
                        deleteBtn.setOnAction(event -> {
                            OfficeHours officeHours = getTableView().getItems().get(getIndex());
                            deleteOfficeHours(officeHours);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteBtn);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
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
    }

    /**
     * Loads office hours data into the table
     */
    private void loadOfficeHours() {
        // Clear the existing list to avoid duplicates when reloading
        officeHoursObservableList.clear();

        try {
            ConnectDB connectDB = new ConnectDB();
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

                // Create OfficeHours object with ID for deletion
                OfficeHours officeHour = new OfficeHours(semester, year, days);
                officeHour.setId(resultSet.getInt("id"));
                officeHoursObservableList.add(officeHour);
            }

            semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

            // set items to tableview
            officeHoursTable.setItems(officeHoursObservableList);

            // sort descending based on year & semester
            yearColumn.setSortType(TableColumn.SortType.DESCENDING);
            semesterColumn.setSortType(TableColumn.SortType.DESCENDING);

            // clear old sorting order & add the new one
            officeHoursTable.getSortOrder().clear();
            officeHoursTable.getSortOrder().addAll(yearColumn, semesterColumn);
            officeHoursTable.sort();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load office hours data.");
        }
    }

    /**
     * Deletes the selected office hours entry
     */
    private void deleteOfficeHours(OfficeHours officeHours) {
        // Confirm deletion with dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this office hours entry?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ConnectDB connectDB = new ConnectDB();
                Connection connection = connectDB.getConnection();

                String deleteQuery = "DELETE FROM officeHours WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, officeHours.getId());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Remove from the table
                    officeHoursObservableList.remove(officeHours);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Office hours entry deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete office hours entry.");
                }

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not delete office hours entry.");
            }
        }
    }

    /**
     * Shows an alert dialog with the given message
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an alert dialog with the given message
     */
    private void showAlert(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Information", message);
    }

    /**
     * Switches the view to the dashboard
     */
    public void switchToDashboard() throws IOException {
        System.out.println("Loading dashboard FXML from: /Fxml/Dashboard/Dashboard.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard/Dashboard.fxml"));
        Parent dashboard = loader.load();
        Scene scene = new Scene(dashboard, 660, 510);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches to the new office hours form view
     */
    private void switchToNewOfficeHoursView() throws IOException {
        System.out.println("Loading new office hours FXML from: /Fxml/OfficeHours/OfficeHours.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/OfficeHours/OfficeHours.fxml"));
        Parent newOfficeHoursView = loader.load();
        Scene scene = new Scene(newOfficeHoursView, 660, 510);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("New Office Hours");
        stage.setScene(scene);
        stage.show();
    }
}
