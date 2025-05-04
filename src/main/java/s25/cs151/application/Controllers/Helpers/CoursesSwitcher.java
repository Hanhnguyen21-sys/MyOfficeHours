package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class CoursesSwitcher implements SceneSwitcher {
    private final Stage stage;

    public CoursesSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Courses/Course.fxml", "Courses");
        switchScene.switchScene();
    }
}
