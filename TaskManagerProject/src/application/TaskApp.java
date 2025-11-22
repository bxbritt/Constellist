package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class TaskApp extends Application {

    private BorderPane rootLayout = new BorderPane();

    // Store the different "pages"
    private ScrollPane mainContent;
    private VBox galleryContent;
    private VBox progressContent;
    private VBox loginContent;
    private Pane starPane;
    private StarManager starManager;

    @Override
    public void start(Stage stage) {

        System.out.println("Logged-in user ID: " + LoggedInUser.getId());

        // side bar navigation
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");

        Button mainButton = new Button("Tasks");
        Button galleryButton = new Button("Gallery");
        Button progressButton = new Button("Progress");
        Button logoutButton = new Button("Logout");

        mainButton.getStyleClass().add("sidebar-button");
        galleryButton.getStyleClass().add("sidebar-button");
        progressButton.getStyleClass().add("sidebar-button");
        logoutButton.getStyleClass().add("sidebar-button");

        sidebar.getChildren().addAll(mainButton, galleryButton, progressButton, logoutButton);
        rootLayout.setLeft(sidebar);

        // main page content
        Button createListButton = new Button("Create New List");
        createListButton.getStyleClass().add("bubble-button");

        HBox menuBar = new HBox(15);
        menuBar.getChildren().addAll(createListButton);
        menuBar.setStyle("-fx-alignment: center-left; -fx-padding: 10;");

        FlowPane listContainer = new FlowPane();
        listContainer.setHgap(20);
        listContainer.setVgap(20);
        listContainer.setPrefWrapLength(900);
        listContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listContainer.setMinHeight(Region.USE_PREF_SIZE);
        listContainer.setMaxHeight(Region.USE_COMPUTED_SIZE);
        listContainer.getStyleClass().add("list-container");

        // gallery page content
<<<<<<< HEAD
        galleryContent = new VBox(20);
        galleryContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
        Text galleryText = new Text("Constellation Gallery Placeholder");
        galleryText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        galleryContent.getChildren().add(galleryText);

        // progress page content
        progressContent = new VBox(20);
        progressContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
        Text progressText = new Text("Your constellation progress will appear here");
        progressText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        progressContent.getChildren().add(progressText);

        starManager = new StarManager(() -> {
            // Switch to the progress page and show a finished constellation
            setCenterContent(progressContent);

            // Replace placeholder text with your constellation
            progressContent.getChildren().clear();
            Text constellation = new Text("🌌 Orion Constellation Unlocked!");
            constellation.setStyle("-fx-fill: white; -fx-font-size: 24;");
            progressContent.getChildren().add(constellation);
        });
=======
//        galleryContent = new VBox(20);
//        galleryContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
//        Text galleryText = new Text("Constellation Gallery Placeholder");
//        galleryText.setStyle("-fx-fill: white; -fx-font-size: 20;");
//        galleryContent.getChildren().add(galleryText);

        // progress page content
//        progressContent = new VBox(20);
//        progressContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
//        Text progressText = new Text("Your constellation progress will appear here");
//        progressText.setStyle("-fx-fill: white; -fx-font-size: 20;");
//        progressContent.getChildren().add(progressText);
//
//        starManager = new StarManager(() -> {
//            // Switch to the progress page and show a finished constellation
//            setCenterContent(progressContent);
//
//            // Replace placeholder text with your constellation
//            progressContent.getChildren().clear();
//            Text constellation = new Text("🌌 Orion Constellation Unlocked!");
//            constellation.setStyle("-fx-fill: white; -fx-font-size: 24;");
//            progressContent.getChildren().add(constellation);
//        });
>>>>>>> GalleryandProg

        // login page content
        loginContent = new VBox(20);
        loginContent.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: #1A103F;");
        Text loginText = new Text("Login Screen Placeholder");
        loginText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        loginContent.getChildren().add(loginText);

        // Load saved lists for the logged-in user
        int userId = LoggedInUser.getId();
        List<SaveProgress> savedLists = Database.loadProgressForUser(userId);

        for (SaveProgress progress : savedLists) {
            TaskList list = new TaskList(progress.getDescription(), progress.getId(), starManager);

            // Only load tasks that are not completed
            List<String> items = Database.loadActiveTaskItemsForList(progress.getId());
            for (String item : items) {
                list.addItem(item);
            }

            listContainer.getChildren().add(list);
        }

        VBox contentLayout = new VBox(20, menuBar, listContainer);
        contentLayout.getStyleClass().add("root");

        mainContent = new ScrollPane(contentLayout);
        mainContent.setFitToWidth(true);
        mainContent.setFitToHeight(true);
        mainContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainContent.setStyle("-fx-background: transparent;");

        // button actions
        createListButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("New List");
            dialog.setHeaderText("Enter a name for your list:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String listName = result.get().trim();
                if (listName.isEmpty()) {
                    listName = "Untitled List"; // fallback if blank
                }

                SaveProgress progress = new SaveProgress(LoggedInUser.getId(), listName, false);
                Database.saveProgress(progress);

                TaskList newList = new TaskList(listName, progress.getId(), starManager);

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
            }
        });

<<<<<<< HEAD
        // Sidebar button actions and SOUND EFFECTS
        mainButton.setOnAction(e -> { 
        	    Sound_Effects.playPianoKey();
        		setCenterContent(mainContent);});
        galleryButton.setOnAction(e -> {
        	    Sound_Effects.playPianoKey();
        		setCenterContent(galleryContent);});
        progressButton.setOnAction(e -> {
        		Sound_Effects.playPianoKey();
        		setCenterContent(progressContent);});
=======
        // Sidebar button actions and SOUND EFFECTS 
        mainButton.setOnAction(e -> { 
        	    Sound_Effects.playPianoKey();});
        	   // TaskApp taskApp = new TaskApp();
        		//taskApp.start(stage);
        		//setCenterContent(mainContent);});
        galleryButton.setOnAction(e -> {
        	    Sound_Effects.playPianoKey();
        	    ConstellationGallery galleryScene = new ConstellationGallery();
        	    galleryScene.start(stage);});
        		//setCenterContent(galleryContent);});
        progressButton.setOnAction(e -> {
        		Sound_Effects.playPianoKey();
        		ProgressScene progressScene = new ProgressScene();
        		progressScene.start(stage);});
        		//setCenterContent(progressContent);});
>>>>>>> GalleryandProg
        logoutButton.setOnAction(e -> {
        		Sound_Effects.playPianoKey();
        		setCenterContent(loginContent);});

        
        // Set default content
        rootLayout.setCenter(mainContent);
        
        
        //plays the music
        music.play("/music/menu_music.mp3", 0.25);
        
        // scene setup
        Scene scene = new Scene(rootLayout, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Task Manager");
        stage.show();
    }

    // menu switch
    private void setCenterContent(javafx.scene.Node content) {
        if (rootLayout.getCenter() == content) return;

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), rootLayout.getCenter());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            rootLayout.setCenter(content);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), content);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }

    public static void main(String[] args) {
        launch();
    }
}