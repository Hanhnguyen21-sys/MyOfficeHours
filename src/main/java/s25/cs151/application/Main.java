package s25.cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This Main Class is the JavaFX Office Hours Application entry point, which extends Application.
 */
public class Main extends Application {

    /**
     * JavaFX calls start automatically after launch is called, we override this method to
     * set up the application dashboard.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("/Fxml/Dashboard/Dashboard.fxml"));
        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * This is where the JavaFX application is launched from
     */
    public static void main(String[] args) {
        launch(args);
    }
}



