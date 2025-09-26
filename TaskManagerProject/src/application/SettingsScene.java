package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
//optional settings? 

//TODO: Add theme toggle (light/dark mode)
//– TODO: Add sound/music toggle (optional ambient audio)
//– TODO: Add reset button to clear all tasks and stars
//– TODO: Add navigation back to TaskEntryScene


public class SettingsScene {
    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Task Entry Screen"));
        return new Scene(root, 600, 400);
    }
}