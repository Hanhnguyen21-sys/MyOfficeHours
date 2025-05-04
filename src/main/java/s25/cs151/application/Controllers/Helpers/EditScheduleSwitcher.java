package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;

import java.io.IOException;

public class EditScheduleSwitcher implements SceneSwitcher {
    private final Stage stage;

    public EditScheduleSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Edit/EditSchedule.fxml", "Edit Schedule");
        switchScene.switchScene();
    }
}
