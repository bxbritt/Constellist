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

public class SignUp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

        // Title 
        Label title = new Label("Create Account");
        title.setFont(Font.font("Verdana", 28));
        title.setTextFill(Color.LIGHTSKYBLUE);

        Label tagline = new Label("Begin your constellation journey");
        tagline.setFont(Font.font("Verdana", 14));
        tagline.setTextFill(Color.LIGHTGRAY);

        VBox header = new VBox(5, title, tagline);
        header.setAlignment(Pos.CENTER);

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        VBox inputBox = new VBox(10, nameField, emailField, passwordField, confirmPasswordField);
        inputBox.setAlignment(Pos.CENTER);

        // Buttons
        Button createAccountButton = new Button("Create Account");
        Button backButton = new Button("Back to Login");

        HBox buttonBox = new HBox(20, createAccountButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, header, inputBox, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setPrefSize(400, 400);

        // Background image
        Image bgImage = new Image(SignUp.class.getResource("starsbackground.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(100, 100, true, true, false, true)
        );
        layout.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(layout, 400, 400);

        
        scene.widthProperty().addListener((obs, oldVal, newVal) -> layout.setPrefWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> layout.setPrefHeight(newVal.doubleValue()));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}