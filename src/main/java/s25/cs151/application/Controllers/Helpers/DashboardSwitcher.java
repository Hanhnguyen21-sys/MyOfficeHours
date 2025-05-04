package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class DashboardSwitcher implements SceneSwitcher {
    private final Stage stage;

    public DashboardSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Dashboard/Dashboard.fxml", "Dashboard");
        switchScene.switchScene();
    }
}
