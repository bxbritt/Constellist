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

import java.util.List;

public class TaskApp extends Application {
    @Override
    public void start(Stage stage) {
    	
    	System.out.println("Logged-in user ID: " + LoggedInUser.getId());
        // Button to create new task list cards
        Button createListButton = new Button("Create New List");
        createListButton.getStyleClass().add("bubble-button");

        Button galleryButton = new Button("Go to Gallery");
        galleryButton.getStyleClass().add("bubble-button");
        
        Button viewProgressButton = new Button("View Progress");
        viewProgressButton.getStyleClass().add("bubble-button");
        
        viewProgressButton.setOnAction(e -> {
            ProgressScene progress = new ProgressScene();
            try {
         	   progress.start(stage);
            } catch (Exception ex) {
         	   ex.printStackTrace();
            }
         });
        

        // Horizontal menu bar
        HBox menuBar = new HBox(15);
        menuBar.getChildren().addAll(createListButton, galleryButton, viewProgressButton);
        menuBar.setStyle("-fx-alignment: center-left; -fx-padding: 10;");

        // FlowPane allows task lists to wrap side-by-side
        FlowPane listContainer = new FlowPane();
        listContainer.setHgap(20);
        listContainer.setVgap(20);
        listContainer.setPrefWrapLength(900);
        listContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listContainer.setMinHeight(Region.USE_PREF_SIZE);
        listContainer.setMaxHeight(Region.USE_COMPUTED_SIZE);
        listContainer.getStyleClass().add("list-container");

        // Load saved progress for the logged-in user
        int userId = LoggedInUser.getId();
        List<SaveProgress> savedLists = Database.loadProgressForUser(userId);
      
        System.out.println("Loaded " + savedLists.size() + " saved lists for user " + userId);
        for (SaveProgress progress : savedLists) {
            System.out.println("→ " + progress.getDescription());
        }
        
        for (SaveProgress progress : savedLists) {
            TaskList list = new TaskList(progress.getDescription(), progress.getId());

            // Load saved items for this list
            List<String> items = Database.loadTaskItemsForList(progress.getId());
            for (String item : items) {
                list.addItem(item); 
            }

            listContainer.getChildren().add(list);
        }

        // Vertical layout of everything
        VBox contentLayout = new VBox(20, menuBar, listContainer);
        contentLayout.getStyleClass().add("root");

        // ScrollPane wraps the FlowPane to enable scrolling
        ScrollPane scrollPane = new ScrollPane(contentLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent;");

        // Add new task list on button click
        createListButton.setOnAction(e -> {
            String defaultTitle = "Task List";

            SaveProgress progress = new SaveProgress(userId, defaultTitle, false);
            System.out.println("🆕 Creating new list...");
            Database.saveProgress(progress);
            System.out.println("✅ Saved progress with ID: " + progress.getId());

            TaskList newList = new TaskList(defaultTitle, progress.getId());

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
                gallery.start(stage);
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