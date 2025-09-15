package application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

//this will function so that the user types in tasks and adds them to the list 

//TODO: Create a layout with a TextField for task input
//TODO: Add a Button to submit the task
//TODO: Store tasks in a shared data structure (optional)
//TODO: Add navigation to TaskListScene after adding a task
//TODO: Style the scene with CSS for a cozy entry vibe

public class TaskEntryScene {
    public static Scene createScene() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Task Entry Screen"));
        return new Scene(root, 600, 400);
    }
}