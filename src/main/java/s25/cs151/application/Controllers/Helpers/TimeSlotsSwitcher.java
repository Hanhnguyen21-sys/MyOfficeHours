package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class TimeSlotsSwitcher implements SceneSwitcher {
    private final Stage stage;

    public TimeSlotsSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/TimeSlots/TimeSlots.fxml", "Time Slots");
        switchScene.switchScene();
    }
}
