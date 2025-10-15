package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import application.TaskApp;
import application.ConstellationScene;
import application.TaskList;
import application.ConstellationGallery;


/* THIS IS THE PAGE 'view progress' LEADS THE USER TO
 * 
 * the page will display a visual of their current list's constellation

 * contains a container that holds this.constellationScene
 * and a container that holds this.taskList
 * 
 * 
 *
 * 	}
 */

public class ProgressScene extends Application {

    @Override
    public void start(Stage stage) {
        // Navigation buttons
        Button taskButton = new Button("Back to Tasks");
        taskButton.getStyleClass().add("bubble-button");
        Button galleryButton = new Button("Gallery");
        galleryButton.getStyleClass().add("bubble-button");
        taskButton.setOnAction(e -> {
            TaskApp taskApp = new TaskApp();
            try {
                taskApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        galleryButton.setOnAction(e -> {
            ConstellationGallery galleryScene = new ConstellationGallery();
            try {
                galleryScene.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // left side: task list view
        VBox taskListBox = new VBox(15);
        taskListBox.setPadding(new Insets(0)); 
        taskListBox.setAlignment(Pos.TOP_LEFT);
        taskListBox.getStyleClass().add("progress-task-box");
        taskListBox.getChildren().add(new Text("Task List"));
        for (int i = 1; i <= 5; i++) {
            taskListBox.getChildren().add(new Text("- Task " + i));
        }

        // rightside: constellation view 
        VBox constellationBox = new VBox(15);
        constellationBox.setPadding(new Insets(0)); 
        constellationBox.setAlignment(Pos.TOP_LEFT);
        constellationBox.getStyleClass().add("progress-constellation-box");
        constellationBox.getChildren().add(new Text("Constellation View"));
        constellationBox.getChildren().add(new Text("[INPUT CONSTELLATION HERE]"));

        // big box that includes both boxes in progressScene
        HBox mainContent = new HBox(60, taskListBox, constellationBox);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40, 0, 0, 0)); // space at the top

       
        VBox layout = new VBox(30, taskButton, galleryButton, mainContent);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Progress");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
