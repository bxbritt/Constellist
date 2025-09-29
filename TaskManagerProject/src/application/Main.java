package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create an instance of TaskApp and call its start method
        TaskApp taskApp = new TaskApp();
        taskApp.start(primaryStage); // This is the key line
    }

    public static void main(String[] args) {
        launch(args);
    }
}