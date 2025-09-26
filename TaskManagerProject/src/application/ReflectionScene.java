package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
//this will show the completed tasks with added motivational quotes 
//TODO: Display completed tasks in a gentle summary
//TODO: Add motivational quotes or poetic reflections
//TODO: Optional show progress stats or time spent?
//TODO: Add navigation back to TaskEntryScene or SettingsScene

public class ReflectionScene {
    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Task Entry Screen"));
        return new Scene(root, 600, 400);
    }
}