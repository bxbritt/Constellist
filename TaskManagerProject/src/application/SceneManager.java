package application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;




public class SceneManager {
    private static Stage stage;
    private static Map<String, Scene> scenes = new HashMap<>();

    // Set the main stage (called from Main.java)
    public static void setStage(Stage s) {
        stage = s;
    }

    // Register a scene with a name
    public static void register(String name, Scene scene) {
        scenes.put(name, scene);
    }

    // Switch to a registered scene by name
    public static void switchTo(String name) {
        stage.setScene(scenes.get(name));
    }
}