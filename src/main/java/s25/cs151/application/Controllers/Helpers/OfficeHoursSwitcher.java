package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class OfficeHoursSwitcher implements SceneSwitcher {
    private final Stage stage;

    public OfficeHoursSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/OfficeHours/OfficeHours.fxml", "Office Hours");
        switchScene.switchScene();
    }
}
