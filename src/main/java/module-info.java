module com.example.project_group17 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project_group17 to javafx.fxml;
    exports com.example.project_group17.Controllers;
    exports com.example.project_group17.Controllers.OfficeHours;
    exports com.example.project_group17.Models;
    exports com.example.project_group17.Views;
    exports com.example.project_group17;
}
