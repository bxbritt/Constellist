package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ForgotPassword extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Forgot Password");

        // global ui
        String globalUI =
                "-fx-font-family: 'Century Gothic';" +
                "-fx-text-fill: white;";

        // header
        Label title = new Label("Reset Your Password");
        title.getStyleClass().add("glow-text");
        title.setStyle("-fx-font-size: 55px; -fx-font-weight: bold;");

        Label subtitle = new Label("Enter your email to receive a reset link");
        subtitle.getStyleClass().add("glow-text");
        subtitle.setStyle("-fx-font-size: 22px;");

        VBox header = new VBox(8, title, subtitle);
        header.setAlignment(Pos.CENTER);

        // input field
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("custom-textfield");

        // buttons
        Button send = new Button("Send Reset Link");
        send.getStyleClass().add("bubble-button");

        Button back = new Button("Back to Login");
        back.getStyleClass().add("bubble-button");

        VBox buttonBox = new VBox(15, send, back);
        buttonBox.setAlignment(Pos.CENTER);

        // main column
        VBox layout = new VBox(30, header, emailField, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(globalUI);

        // background
        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
        );

        StarOverlay stars = new StarOverlay(180);

        root.getChildren().addAll(stars, layout);

        ScreenManager.show(primaryStage, root);

        // button handlers

        send.setOnAction(e -> {
            String email = emailField.getText().trim();

            if (email.isEmpty()) {
                show("Please enter your email.", Alert.AlertType.WARNING);
                return;
            }

            String token = TokenUtils.generateResetToken();
            Database.saveResetToken(email, token);
            EmailUtils.sendResetEmail(email, token);
            show("A reset link has been sent to your email.", Alert.AlertType.INFORMATION);
        });

        back.setOnAction(e -> new Main().start(primaryStage));
    }

    private void show(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(ScreenManager.getStage());
        alert.setContentText(message);
        alert.show();
    }
}
