package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class TimeSlotsListSwitcher implements SceneSwitcher {
    private final Stage stage;

    public TimeSlotsListSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/TimeSlots/TimeSlotsList.fxml", "Time Slots List");
        switchScene.switchScene();
    }
}
