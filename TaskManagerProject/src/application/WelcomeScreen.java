package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

    // persistent window size
    private static double savedWidth = 1000;
    private static double savedHeight = 600;

    // apply persistent size
    private void applyPersistentWindowSize(Stage stage) {
        stage.setWidth(savedWidth);
        stage.setHeight(savedHeight);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> savedWidth = newVal.doubleValue());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> savedHeight = newVal.doubleValue());
    }

    public void show(Stage stage, boolean returningUser) {

        // message logic
        String message = returningUser
                ? "Welcome Back, " + LoggedInUser.getUsername() + "!"
                : "Welcome to Constellist, " + LoggedInUser.getUsername() + "!";

        // title
        Label title = new Label(message);
        title.getStyleClass().add("glow-text");
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        // subtitle
        Label subtitle = new Label(returningUser
                ? "Jump back in?"
                : "Ready to manage your daily tasks?");
        subtitle.getStyleClass().add("glow-text");
        subtitle.setStyle("-fx-font-size: 22px;");

        // continue button
        Button startButton = new Button(returningUser ? "continue" : "get started");
        startButton.getStyleClass().add("bubble-button");
        startButton.setStyle("-fx-font-size: 18px;");

        // layout container
        VBox layout = new VBox(30, title, subtitle, startButton);
        layout.setAlignment(Pos.CENTER);

        // background gradient
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #071229, #0d234f, #280c4c);");

        // animated stars
        StarOverlay stars = new StarOverlay(150);
        stars.prefWidthProperty().bind(stage.widthProperty());
        stars.prefHeightProperty().bind(stage.heightProperty());

        root.getChildren().addAll(stars, layout);

        // scene setup
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        applyPersistentWindowSize(stage);

        // button action
        startButton.setOnAction(e -> {
            TaskApp app = new TaskApp();
            try {
                app.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
