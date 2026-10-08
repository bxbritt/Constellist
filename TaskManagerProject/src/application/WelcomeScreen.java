package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

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

        root.getChildren().addAll(stars, layout);

        // button action
        startButton.setOnAction(e -> {
            TaskApp app = new TaskApp();
            try {
                app.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ScreenManager.show(stage, root);
    }
}
