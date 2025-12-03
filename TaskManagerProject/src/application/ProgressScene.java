package application;

import java.lang.annotation.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
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
<<<<<<< HEAD
 *
=======
>>>>>>> GalleryandProg
 *
 *
 * 	
 */

public class ProgressScene extends Application {
	//initialize list Id to read seperate lists
	private static int listId;
	
	public ProgressScene() {
		this.listId = -1;
	}
	
	//constructor for listid
	public ProgressScene (int listId) {
		this.listId = listId;
	}

    @Override
    public void start(Stage stage) {
    	
    	//null check for view progress
    	
    	if(listId == -1) {
    		System.out.println("No List on file");
    	
    	} else {
    		System.out.println("Progress Scene started: "+ listId);
    	}
    	
        // Navigation buttons
        Button taskButton = new Button("Back to Tasks");
        taskButton.getStyleClass().add("bubble-button");
        Button galleryButton = new Button("Go to Gallery");
        galleryButton.getStyleClass().add("bubble-button");

        // create events for buttons
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

        // set position for buttons
        galleryButton.relocate(925, 100);
        taskButton.relocate(925, 50);

        String listName = "THIS TASK LIST NAME";
        Label label = new Label(listName);
        label.setFont(new Font(24));
        label.relocate(75, 50);
        label.setTextFill(Color.CORNFLOWERBLUE);

        // containers for tasks
        Pane taskDetails = new Pane();
        taskDetails.setPrefHeight(500);
        taskDetails.setPrefWidth(425);
        taskDetails.relocate(50, 140);

        VBox tasksRemain = new VBox();
        tasksRemain.setPadding(new Insets(10));
        tasksRemain.setAlignment(Pos.TOP_CENTER);
        tasksRemain.setPrefHeight(200);
        tasksRemain.setPrefWidth(400);
        tasksRemain.relocate(12, 30);
        tasksRemain.getStyleClass().add("tasksRemain");
        Text remainText = new Text("Tasks Remaining:");
        remainText.setFill(Color.ANTIQUEWHITE);

      

        // --- Remaining Tasks Box ---
       
        

     
     ListView<String> remainingItemsList = new ListView<>();
     ObservableList<String> itemsRemain = FXCollections.observableArrayList();
     remainingItemsList.getStyleClass().add("progress-list");


     for (String item: Database.loadActiveTaskItemsForList(this.listId)) {
    	 itemsRemain.add("* " + item);
     }
     remainingItemsList.setItems(itemsRemain);

     tasksRemain.getChildren().addAll(remainText, remainingItemsList);
     tasksRemain.getStyleClass().add("progressBox");

     // --- Completed Tasks Box ---
     VBox tasksComplete = new VBox();
     tasksComplete.setPadding(new Insets(10));
     tasksComplete.setAlignment(Pos.TOP_CENTER);
     tasksComplete.setPrefHeight(200);
     tasksComplete.setPrefWidth(400);
     tasksComplete.relocate(12, 290);
     tasksComplete.getStyleClass().add("tasksComplete");

     Text completeText = new Text("Tasks Completed:");
     completeText.setFill(Color.ANTIQUEWHITE);

     ListView<String> completedItemsList = new ListView<>();
     completedItemsList.getStyleClass().add("progress-list");


     ObservableList<String> itemsComplete = FXCollections.observableArrayList();
     for (String item : Database.loadCompletedTaskItemsForList(this.listId)) {
         itemsComplete.add("• " + item); // adds bullet
     }
     completedItemsList.setItems(itemsComplete);



     tasksComplete.getChildren().addAll(completeText, completedItemsList);
     tasksComplete.getStyleClass().add("progressBox");

     // --- Add both boxes to the parent container ---
     taskDetails.getStyleClass().add("taskDetails");
     taskDetails.getChildren().addAll(tasksRemain, tasksComplete);

  // --- After loading completed tasks ---
     ConstellationManager cm = ConstellationManager.getInstance();
     cm.loadAllConstellationsFromDatabase();
     Constellation current = cm.getCurrentConstellation();



     // Desired box size
     double boxW = 400;
     double boxH = 400;

     //createProgressView to fit progessbox
     Pane constView = current.createProgressView(boxW, boxH);
     constView.getStyleClass().add("progressBox");
     
     constView.layoutXProperty().bind(
    		 taskDetails.layoutXProperty()
    		 .add(taskDetails.prefWidthProperty())
    		 .add(50)
    		 );
     constView.setLayoutY(200);
     constView.setStyle("-fx-background-color: transparent;");
     constView.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
     
  // --- Root container ---
     Pane progressRoot = new Pane();
     progressRoot.getChildren().addAll(label, taskButton, galleryButton, taskDetails, constView);

     // --- Layered background like TaskApp ---
     StackPane layeredRoot = new StackPane();
     layeredRoot.setStyle(
         "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
     );

     // Add animated stars
     StarOverlay stars = new StarOverlay(180);
     stars.prefWidthProperty().bind(stage.widthProperty());
     stars.prefHeightProperty().bind(stage.heightProperty());

     // Put stars + your progressRoot together
     layeredRoot.getChildren().addAll(stars, progressRoot);

     // --- Scene ---
     Scene scene = new Scene(layeredRoot, 1200, 700);
     scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
     stage.setScene(scene);
     stage.setTitle("Progress");
     stage.setMaximized(true);
     stage.show();
     stage.setFullScreen(true);


    }
   

    
   
    
    public static void main(String[] args) {
        launch();
    }
}

