package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class TaskApp extends Application {
    @Override
    public void start(Stage stage) {
    	// Button to create new task list cards
        Button createListButton = new Button("Create New List");
        createListButton.getStyleClass().add("bubble-button");
        
        Button galleryButton = new Button("Go to Gallery");
        galleryButton.getStyleClass().add("bubble-button");
    	
    	// Horizontal menu bar
    	HBox menuBar = new HBox(15);	// spacing between buttons
    	menuBar.getChildren().addAll(createListButton, galleryButton);
    	menuBar.setStyle("-fx-alignment: center-left; -fx-padding: 10;");

        // FlowPane allows task lists to wrap side-by-side
        FlowPane listContainer = new FlowPane();
        listContainer.setHgap(20);
        listContainer.setVgap(20);
        listContainer.setPrefWrapLength(900); // triggers wrapping
        listContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listContainer.setMinHeight(Region.USE_PREF_SIZE);
        listContainer.setMaxHeight(Region.USE_COMPUTED_SIZE);
        listContainer.getStyleClass().add("list-container");
    	
        // vertical layout of everything
        VBox contentLayout = new VBox(20, menuBar, listContainer);
        contentLayout.getStyleClass().add("root");
        
        // ScrollPane wraps the FlowPane to enable scrolling
        ScrollPane scrollPane = new ScrollPane(contentLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent;");

        // add new task list on button click
        createListButton.setOnAction(e -> {
            TaskList newList = new TaskList("Task List");
            
            // animation of card
            FadeTransition fade = new FadeTransition(Duration.millis(500), newList);
            fade.setFromValue(0);
            fade.setToValue(1);
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(500), newList);
            scale.setFromX(0.8);
            scale.setFromY(0.8);
            scale.setToX(1);
            scale.setToY(1);
            
            fade.play();
            scale.play();
            
            listContainer.getChildren().add(newList);
            
           
        });
            
            galleryButton.setOnAction(e -> {
            ConstellationGallery gallery = new ConstellationGallery();
                try {
                    gallery.start(stage); // switch stage to ConstellationGallery
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        // Scene setup
        Scene scene = new Scene(scrollPane, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Responsive wrap length
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            listContainer.setPrefWrapLength(newVal.doubleValue() - 40);
        });

        stage.setScene(scene);
        stage.setTitle("Task Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}