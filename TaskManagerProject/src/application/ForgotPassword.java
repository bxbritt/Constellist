package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ForgotPassword extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Forgot Password");

        // Title label
        Label title = new Label("Please enter your email");
        title.setFont(Font.font("Verdana", 16));
        title.setTextFill(Color.DARKTURQUOISE);

        // Email field
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        // Send button
        Button sendButton = new Button("Send Reset Link");
        sendButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter your email.");
                return;
            }

            String token = TokenUtils.generateResetToken();
            Database.saveResetToken(email, token);
            EmailUtils.sendResetEmail(email, token);

            showAlert(Alert.AlertType.INFORMATION, "Reset link sent to your email.");
        });
        
        Button backButton = new Button("Back to Main");
        backButton.setOnAction(e -> {
            try {
                // Re‑launch your main screen
                new Main().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout with title included
        VBox layout = new VBox(15, title, emailField, sendButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(300, 200);

        // Background color (you can swap for an image if you want)
        layout.setBackground(new Background(
            new BackgroundFill(Color.DARKSLATEBLUE, new CornerRadii(10), Insets.EMPTY)
        ));
        
        Image bgImage = new Image(ForgotPassword.class.getResource("starsbackground.jpg").toExternalForm());

        BackgroundImage backgroundImage = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,   // repeat horizontally
            BackgroundRepeat.NO_REPEAT,   // repeat vertically
            BackgroundPosition.CENTER,    // position
            new BackgroundSize(100, 100, true, true, false, true) // scale
        );

        layout.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}