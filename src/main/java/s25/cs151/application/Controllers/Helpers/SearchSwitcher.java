package s25.cs151.application.Controllers.Helpers;

import javafx.stage.Stage;
import java.io.IOException;

public class SearchSwitcher implements SceneSwitcher {
    private final Stage stage;

    public SearchSwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void switchScene() throws IOException {
        SwitchScene switchScene = new SwitchScene(stage, "/Fxml/Search/SearchSchedule.fxml", "Search Schedule");
        switchScene.switchScene();
    }
}
