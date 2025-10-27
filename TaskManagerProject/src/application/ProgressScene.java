package application;

import java.lang.annotation.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;

import application.TaskListScene;
import application.ConstellationScene;
import application.TaskList;
import application.ConstellationGallery;


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
 * 	}
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
       
    
        //customizing and adding components to taskDetails VBox
        
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
       //fake list for now
       ObservableList<String> itemsRemain =FXCollections.observableArrayList (
           "Task1", "Task2");
       remainingItemsList.setItems(itemsRemain);
       
       
       tasksRemain.getChildren().addAll(remainText,remainingItemsList);
       
       
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
       
       ListView<String> completedItemsList = new ListView<String>();
       
     //  completedItemsList.setPrefSize(50, 50);
       //fake list for now
       ObservableList<String> itemsComplete = FXCollections.observableArrayList (
           "Task5", "Task6", "Task7");
       completedItemsList.setItems(itemsComplete);
 
       
       //Arrange components of Tasks completed box
       tasksComplete.getChildren().addAll(completeText,completedItemsList);
       
       
       //layout and style for task details
       taskDetails.getStyleClass().add("taskDetails");
       taskDetails.getChildren().addAll(tasksRemain, tasksComplete);
       
        
        //container that holds CURRENT constellation view
        Pane constView = new Pane();
        constView.setPadding(new Insets(10)); 
    
        //customizing and adding components to constView VBox
        
        constView.setPrefHeight(500);
        constView.setPrefWidth(425);
        constView.getStyleClass().add("constView");
        constView.relocate(625,150); //ranges from 0px - 675px for width
        
        Image constTest = new Image(getClass().getResourceAsStream("constellationPrototype.png"));
        ImageView constallationView = new ImageView(constTest);
       constallationView.fitWidthProperty().bind(constView.widthProperty());
       constallationView.fitHeightProperty().bind(constView.heightProperty());
      
        constView.getChildren().add(constallationView);
        
        
        
        Pane progressRoot = new Pane();
        progressRoot.getChildren().addAll(label,taskButton, galleryButton, taskDetails, constView); //add componetns to scene

    
        Scene scene = new Scene(progressRoot,1100,700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Progress");
        stage.show();
    }

    public static void main(String[] args) {
    	
        launch();
    }
}
