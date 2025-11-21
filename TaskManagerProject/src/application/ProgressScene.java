package application;

import java.lang.annotation.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/* THIS IS THE PAGE 'view progress' (button) LEADS TO
 * 
 * 
 * the page will display a visual of their current list's and constellation
 * 
 * contains a container that holds this.constellationScene
 * and a container that holds this.taskList
 *  buttons to go back to home
 * 
 *
 *
 * 	
 */

public class ProgressScene extends Application {
	
	
    @Override
     public void start(Stage stage) {
    	
        // Navigation buttons
        Button taskButton = new Button("Back to Tasks");
        taskButton.getStyleClass().add("bubble-button");
        Button galleryButton = new Button("Go to Gallery");
        galleryButton.getStyleClass().add("bubble-button");
        
        //create events for buttons
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
        
        //set position for buttons in root pane
        galleryButton.relocate(925, 100);
        taskButton.relocate(925, 50);
        
        String listName = "THIS TASK LIST NAME";
        Label label = new Label(listName);
        
        label.setFont(new Font(24));
        label.relocate(75,50);
        label.setTextFill(Color.CORNFLOWERBLUE);
        
        //container that holds completes/remaining tasks
        Pane taskDetails = new Pane();
       
    
        //customizing and adding components to taskDetails
        
        taskDetails.setPrefHeight(500);
        taskDetails.setPrefWidth(425);
       
        taskDetails.relocate(50,140);
        
       VBox tasksRemain = new VBox();
       tasksRemain.setPadding(new Insets(10)); 
       tasksRemain.setAlignment(Pos.TOP_CENTER);
       tasksRemain.setPrefHeight(200);
       tasksRemain.setPrefWidth(400);
       tasksRemain.relocate(12, 30);
       tasksRemain.getStyleClass().add("tasksRemain");
       Text remainText = new Text("Tasks Remaining:");
       remainText.setFill(Color.ANTIQUEWHITE);
       
      
       ListView<String> remainingItemsList = new ListView<String>();
       
       
       //fake list for now, this is where the task list data will go
       ObservableList<String> itemsRemain =FXCollections.observableArrayList ( //node for displaying the list
           "Task1", "Task2");
       remainingItemsList.setItems(itemsRemain);
       
       
       tasksRemain.getChildren().addAll(remainText,remainingItemsList);
       tasksRemain.getStyleClass().add("progressBox");
       
       
       //completed tasks container
       
       VBox tasksComplete = new VBox();
       tasksComplete.setPadding(new Insets(10)); 
       tasksComplete.setAlignment(Pos.TOP_CENTER);
       tasksComplete.setPrefHeight(200);
       tasksComplete.setPrefWidth(400);
       tasksComplete.relocate(12, 290);
       tasksComplete.getStyleClass().add("tasksComplete");
       Text completeText = new Text("Tasks Completed:");
       completeText.setFill(Color.ANTIQUEWHITE);
       
       
       ListView<String> completedItemsList = new ListView<String>(); //node for displaying the list
       
       //fake list for now, this is where the task list data will go
       ObservableList<String> itemsComplete = FXCollections.observableArrayList (
           "Task5", "Task6", "Task7");
       completedItemsList.setItems(itemsComplete);
 
       
       //arrange components of tasks, completed box
       tasksComplete.getChildren().addAll(completeText,completedItemsList);
       tasksComplete.getStyleClass().add("progressBox");
       
       
       //layout and style for task details
       taskDetails.getStyleClass().add("taskDetails");
       taskDetails.getChildren().addAll(tasksRemain, tasksComplete);
       
        
       // Create the constellation with auto-scaling to fit
       C1_Heart capricorn = new C1_Heart();
       
       // constellation display area size
       double displayWidth = 400;
       double displayHeight = 400;
       
       // find the constellation bounds
       double constellationWidth = capricorn.getBoundsInLocal().getWidth();
       double constellationHeight = capricorn.getBoundsInLocal().getHeight();
       
       // calculate scale to fit
       double scaleX = displayWidth / constellationWidth;
       double scaleY = displayHeight / constellationHeight;
       double scale = Math.min(scaleX, scaleY) * 0.9; // 0.9 for padding
       
       // wrap constellation in a Group for scaling purposes
       Group scaledGroup = new Group(capricorn);
       scaledGroup.setScaleX(scale);
       scaledGroup.setScaleY(scale);
       
       // create a StackPane to center the constellation
       StackPane constView = new StackPane();
       constView.getChildren().add(scaledGroup);
       constView.setPrefSize(displayWidth, displayHeight);
       constView.setMaxSize(displayWidth, displayHeight);
       constView.setMinSize(displayWidth, displayHeight);
       
       // create a clipping rectangle to crop the view
       Rectangle clip = new Rectangle(displayWidth, displayHeight);
       constView.setClip(clip);
       
     
       constView.getStyleClass().add("progressBox");
       constView.relocate(650,200); 
        
        
       
        Pane progressRoot = new Pane();
        progressRoot.getChildren().addAll(label, taskButton, galleryButton, taskDetails, constView); 
        //add components to scene

    
        Scene scene = new Scene(progressRoot, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Progress");
        stage.show();
        
        
    }
    
    

    public static void main(String[] args) {
    	
        launch();
    }
}