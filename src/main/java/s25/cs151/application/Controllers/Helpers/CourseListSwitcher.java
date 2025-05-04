package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class CourseListSwitcher implements SceneSwitcher {
    private final Stage stage;

    public CourseListSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Courses/CourseList.fxml", "Course List");
        switchScene.switchScene();
    }
}
