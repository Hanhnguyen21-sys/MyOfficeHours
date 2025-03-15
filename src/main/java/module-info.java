module com.example.project_group17 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.project_group17 to javafx.fxml;
    opens com.example.project_group17.Controllers to javafx.fxml;
    opens com.example.project_group17.Controllers.OfficeHours to javafx.fxml;
    opens com.example.project_group17.Controllers.Dashboard to javafx.fxml;
    opens com.example.project_group17.Models to javafx.fxml;
    opens com.example.project_group17.Views to javafx.fxml;

    exports com.example.project_group17;
    exports com.example.project_group17.Controllers;
    exports com.example.project_group17.Controllers.OfficeHours;
    exports com.example.project_group17.Controllers.Dashboard;
    exports com.example.project_group17.Models;
    exports com.example.project_group17.Views;
}
