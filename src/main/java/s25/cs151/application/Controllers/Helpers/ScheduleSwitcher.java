package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class ScheduleSwitcher implements SceneSwitcher {
    private final Stage stage;

    public ScheduleSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Schedule/Schedule.fxml", "Schedule");
        switchScene.switchScene();
    }
}
