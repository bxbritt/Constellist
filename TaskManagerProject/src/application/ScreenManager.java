package application;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ScreenManager {

    // the app's main window, so popups/dialogs can attach to it
    private static Stage mainStage;

    public static Stage getStage() {
        return mainStage;
    }

    // The whole app shares one Scene; switching pages only swaps its root.
    // Replacing the Scene (and resizing the stage) on every page change is what
    // knocked the window out of fullscreen/maximized and made it jump around.
    public static void show(Stage stage, Parent root) {
        mainStage = stage;
        Scene scene = stage.getScene();

        if (scene == null) {
            scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(ScreenManager.class.getResource("style.css").toExternalForm());

            // F11 toggles fullscreen (Esc also exits it)
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });

            stage.setMinWidth(800);
            stage.setMinHeight(550);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }

        // fade the new page in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        stage.show();
    }
}
