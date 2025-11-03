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
import java.util.List;

public class TaskApp extends Application {

    private BorderPane rootLayout = new BorderPane();

    // Store the different "pages"
    private ScrollPane mainContent;
    private VBox galleryContent;
    private VBox progressContent;
    private VBox loginContent;

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

        // Load saved lists for the logged-in user
        int userId = LoggedInUser.getId();
        List<SaveProgress> savedLists = Database.loadProgressForUser(userId);

        for (SaveProgress progress : savedLists) {
            TaskList list = new TaskList(progress.getDescription(), progress.getId());
            List<String> items = Database.loadTaskItemsForList(progress.getId());
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

        // gallery page content
        galleryContent = new VBox(20);
        galleryContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
        Text galleryText = new Text("Constellation Gallery Placeholder");
        galleryText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        galleryContent.getChildren().add(galleryText);

        // progress page place holder
        progressContent = new VBox(20);
        progressContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1B1640;");
        Text progressText = new Text("Your constellation progress will appear here");
        progressText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        progressContent.getChildren().add(progressText);

        // login place holder
        loginContent = new VBox(20);
        loginContent.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: #1A103F;");
        Text loginText = new Text("Login Screen Placeholder");
        loginText.setStyle("-fx-fill: white; -fx-font-size: 20;");
        loginContent.getChildren().add(loginText);

        // button actions

        // Prevent duplicate blank lists by checking existing titles
        createListButton.setOnAction(e -> {
            boolean duplicateExists = listContainer.getChildren().stream()
                .filter(node -> node instanceof TaskList)
                .map(node -> (TaskList) node)
                .anyMatch(list -> {
                    TextField titleField = (TextField) ((HBox) list.getChildren().get(0)).getChildren().get(0);
                    String title = titleField.getText().trim();
                    return title.isEmpty() || title.equals("Task List");
                });

            if (duplicateExists) {
                System.out.println("A blank or untitled list already exists. Please name it first.");
                return;
            }

            // Otherwise create a fresh list
            SaveProgress progress = new SaveProgress(LoggedInUser.getId(), "Task List", false);
            Database.saveProgress(progress);
            TaskList newList = new TaskList("Task List", progress.getId());

            // Animation
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

        // Sidebar button actions
        mainButton.setOnAction(e -> setCenterContent(mainContent));
        galleryButton.setOnAction(e -> setCenterContent(galleryContent));
        progressButton.setOnAction(e -> setCenterContent(progressContent));
        logoutButton.setOnAction(e -> setCenterContent(loginContent));

        // Set default content
        rootLayout.setCenter(mainContent);

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
