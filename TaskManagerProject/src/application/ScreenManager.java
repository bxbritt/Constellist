
package application;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ScreenManager {

    private static Stage currentStage;

    // Save the current stage so other screens can access it
    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    // Fade Transition Between Screens
    public static void switchScreen(Stage stage, Scene newScene) {
        setCurrentStage(stage);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250));
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            stage.setScene(newScene);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newScene.getRoot());
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        if (stage.getScene() != null)
            fadeOut.setNode(stage.getScene().getRoot());

        fadeOut.play();
    }
}
