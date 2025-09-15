package application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

// this will handle the switching between the scenes 

//TODO: Create a static method to set the main Stage
//TODO: Create a method to register scenes by name
//TODO: Create a method to switch scenes by name
//TODO: Store scenes in a HashMap for easy access


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