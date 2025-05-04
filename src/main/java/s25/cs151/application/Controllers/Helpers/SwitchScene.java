package s25.cs151.application.Controllers.Helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScene implements SceneSwitcher {
    private final Stage stage;
    private final String fxmlPath;
    private final String title;

    // Constructor accepts Stage, FXML path, and title
    public SwitchScene(Stage stage, String fxmlPath, String title) {
        this.stage = stage;
        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    // Implement the switchScene() method from SceneSwitcher interface
    @Override
    public void switchScene() throws IOException {
        // Load the FXML and switch the scene
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
