package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConstellationTest extends Application {

    @Override
    public void start(Stage stage) {
        Constellation constellation = new C2_Capricorn();

        // use createShow instead of createPreview
        StackPane root = new StackPane(
            constellation.createShow(600, 600) // fills the window nicely
        );
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Constellation Show");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}