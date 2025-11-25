package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class TaskApp extends Application {

    private ScrollPane mainContent;
    private VBox galleryContent;
    private VBox progressContent;
    private VBox loginContent;

    private StarManager starManager;

    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) {

        rootLayout = new BorderPane();

        StackPane layeredRoot = new StackPane();
        layeredRoot.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
        );

        StarOverlay stars = new StarOverlay(180);
        stars.prefWidthProperty().bind(stage.widthProperty());
        stars.prefHeightProperty().bind(stage.heightProperty());

        layeredRoot.getChildren().addAll(stars, rootLayout);

        int userId = LoggedInUser.getId();
        boolean returningUser = !Database.loadProgressForUser(userId).isEmpty();

        if (!LoggedInUser.hasSeenWelcome) {
            LoggedInUser.hasSeenWelcome = true;

            WelcomeScreen ws = new WelcomeScreen();
            ws.show(stage, returningUser);
            return;
        }

        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.getStyleClass().add("sidebar-border");

        String welcomeText = returningUser
                ? "Welcome Back, " + LoggedInUser.getUsername()
                : "Welcome, " + LoggedInUser.getUsername();

        Label welcomeLabel = new Label(welcomeText);
        welcomeLabel.getStyleClass().add("welcome-label");
        welcomeLabel.getStyleClass().add("glow-text");

        sidebar.getChildren().add(0, welcomeLabel);

        Button mainButton = new Button("Tasks");
        Button galleryButton = new Button("Gallery");
        Button progressButton = new Button("Progress");
        Button logoutButton = new Button("Logout");

        mainButton.getStyleClass().add("sidebar-button");
        galleryButton.getStyleClass().add("sidebar-button");
        progressButton.getStyleClass().add("sidebar-button");
        logoutButton.getStyleClass().add("sidebar-button");

        sidebar.getChildren().addAll(mainButton, galleryButton, progressButton, logoutButton);
        sidebar.setStyle("-fx-background-color: transparent;");
        rootLayout.setLeft(sidebar);

        Button createListButton = new Button("Create New List");
        createListButton.getStyleClass().add("bubble-button");
        createListButton.getStyleClass().add("glow-text");

        FlowPane listContainer = new FlowPane();
        listContainer.setHgap(20);
        listContainer.setVgap(20);
        listContainer.setPrefWrapLength(900);
        listContainer.getStyleClass().add("list-container");

        VBox contentLayout = new VBox(20, createListButton, listContainer);
        contentLayout.getStyleClass().add("content-root");

        mainContent = new ScrollPane(contentLayout);
        mainContent.setFitToWidth(true);
        mainContent.setFitToHeight(true);
        mainContent.setStyle("-fx-background: transparent;");

        galleryContent = new VBox(20);
        galleryContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: transparent;");
        Text galleryText = new Text("Constellation Gallery Placeholder");
        galleryText.getStyleClass().add("glow-text");
        galleryContent.getChildren().add(galleryText);

        progressContent = new VBox(20);
        progressContent.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: transparent;");
        Text progressText = new Text("Constellation Progress Appears here");
        progressText.getStyleClass().add("glow-text");
        progressContent.getChildren().add(progressText);

        starManager = new StarManager(() -> {
            setCenterContent(progressContent);

            progressContent.getChildren().clear();
            Text constellation = new Text("Constellation Unlocked!");
            constellation.getStyleClass().add("glow-text");
            constellation.setStyle("-fx-font-size: 24;");
            progressContent.getChildren().add(constellation);
        });

        loginContent = new VBox(20);
        loginContent.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: transparent;");
        Text loginText = new Text("Login Screen Placeholder");
        loginText.getStyleClass().add("glow-text");
        loginContent.getChildren().add(loginText);

        userId = LoggedInUser.getId();
        List<SaveProgress> savedLists = Database.loadProgressForUser(userId);

        for (SaveProgress progress : savedLists) {
            TaskList list = new TaskList(progress.getDescription(), progress.getId(), starManager);

            // ⭐ No more getTitleField() — TaskList handles glowing titleLabel itself

            List<String> items = Database.loadActiveTaskItemsForList(progress.getId());
            for (String item : items) {
                list.addItem(item);
            }

            listContainer.getChildren().add(list);
        }

        createListButton.setOnAction(e -> {

            if (rootLayout.getCenter() != mainContent) {
                System.out.println("⚠ Cannot create lists outside the Tasks page.");
                return;
            }

            TextInputDialog dialog = new TextInputDialog("New List");
            dialog.setHeaderText("Enter a name for your list:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String listName = result.get().trim();
                if (listName.isEmpty()) listName = "Untitled List";

                SaveProgress progress = new SaveProgress(LoggedInUser.getId(), listName, false);
                Database.saveProgress(progress);

                TaskList newList = new TaskList(listName, progress.getId(), starManager);

                // ⭐ No more getTitleField() — TaskList applies glow automatically

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

        mainButton.setOnAction(e -> {
            Sound_Effects.playPianoKey();
            setCenterContent(mainContent);
        });

        galleryButton.setOnAction(e -> {
            Sound_Effects.playPianoKey();
            setCenterContent(galleryContent);
        });

        progressButton.setOnAction(e -> {
            Sound_Effects.playPianoKey();
            setCenterContent(progressContent);
        });

        logoutButton.setOnAction(e -> {
            Sound_Effects.playPianoKey();
            setCenterContent(loginContent);
        });

        rootLayout.setCenter(mainContent);

        music.play("/music/menu_music.mp3", 0.25);

        Scene scene = new Scene(layeredRoot, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Constellation Task Manager");
        stage.show();
    }

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
