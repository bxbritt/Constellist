package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

//this will display the current tasks with options to mark complete 

//TODO: Display a list of current tasks (ListView or VBox)
//TODO: Add checkboxes or buttons to mark tasks as complete
//TODO: Update task status and prepare for constellation visualization
//TODO: Add navigation to ConstellationScene when tasks are completed
//TODO: Style the scene with soft colors and clear layout


public class TaskListScene {
    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Task Entry Screen"));
        return new Scene(root, 600, 400);
    }
}