package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class OfficeHoursListSwitcher implements SceneSwitcher {
    private final Stage stage;

    public OfficeHoursListSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/OfficeHours/OfficeHoursList.fxml", "Office Hours List");
        switchScene.switchScene();
    }
}
