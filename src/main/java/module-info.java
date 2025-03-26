module com.example.project_group17 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens s25.cs151.application to javafx.fxml;
    opens s25.cs151.application.Controllers.OfficeHours to javafx.fxml;
    opens s25.cs151.application.Controllers.Dashboard to javafx.fxml;
    opens s25.cs151.application.Models to javafx.fxml;
    opens s25.cs151.application.Views to javafx.fxml;

    exports s25.cs151.application;
    exports s25.cs151.application.Controllers.OfficeHours;
    exports s25.cs151.application.Controllers.Dashboard;
    exports s25.cs151.application.Models;
    exports s25.cs151.application.Views;
    exports s25.cs151.application.Controllers.Courses;
    opens s25.cs151.application.Controllers.Courses to javafx.fxml;
    exports s25.cs151.application.Controllers.TimeSlots;
    opens s25.cs151.application.Controllers.TimeSlots to javafx.fxml;
}
