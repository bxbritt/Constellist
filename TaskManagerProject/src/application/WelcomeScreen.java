package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

    public void show(Stage stage, boolean returningUser) {

        // message logic
        String message = returningUser
                ? "Welcome back, " + LoggedInUser.getUsername() + "!"
                : "Welcome, " + LoggedInUser.getUsername() + "!";

        // title
        Label title = new Label(message);
        title.getStyleClass().add("glow-text");
        title.setStyle(
                "-fx-font-size: 42px;" +
                "-fx-font-weight: bold;"
        );

        // subtitle
        Label subtitle = new Label(
                returningUser ? "Jump back in?" : "Ready to manage your daily tasks?"
        );
        subtitle.getStyleClass().add("glow-text");
        subtitle.setStyle("-fx-font-size: 20px;");

        // button
        Button startButton = new Button(returningUser ? "Continue" : "Let's Get Started");
        startButton.getStyleClass().add("bubble-button");
        startButton.setStyle("-fx-font-size: 18px;"); // font only — style.css controls everything else

        // main content layout
        VBox layout = new VBox(30, title, subtitle, startButton);
        layout.setAlignment(Pos.CENTER);

        // gradient background matches app
        StackPane root = new StackPane();
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
        );

        // star overlay
        StarOverlay stars = new StarOverlay(150);
        stars.prefWidthProperty().bind(stage.widthProperty());
        stars.prefHeightProperty().bind(stage.heightProperty());

        // layered stars behind content
        root.getChildren().addAll(stars, layout);

        Scene scene = new Scene(root, 1000, 600);
        
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // action
        startButton.setOnAction(e -> {
            TaskApp app = new TaskApp();
            try { app.start(stage); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
