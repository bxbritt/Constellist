package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

//this will show the completed tasks as glowing stars 

//TODO: Create a visual layout that represents stars/tasks
//TODO: Light up stars when tasks are marked complete
//TODO: Animate or glow completed stars
//TODO: Add poetic feedback or messages for each star
//TODO: Add navigation to ReflectionScene

public class ConstellationScene {
    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Task Entry Screen"));
        return new Scene(root, 600, 400);
    }
}