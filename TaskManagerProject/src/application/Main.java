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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Start Screen");

        // Title and tagline
        Label title = new Label("Welcome");
        title.setFont(Font.font("Verdana", 28));
        title.setTextFill(Color.DARKTURQUOISE);

        Label tagline = new Label("Log in to begin");
        tagline.setFont(Font.font("Verdana", 14));
        tagline.setTextFill(Color.DARKTURQUOISE);

        VBox header = new VBox(5, title, tagline);
        header.setAlignment(Pos.CENTER);

        // email and password fields
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        VBox inputBox = new VBox(10, emailField, passwordField);
        inputBox.setAlignment(Pos.CENTER);

        //Login/signup Buttons
        Button loginButton = new Button("Log In");
        Button signupButton = new Button("Sign Up");
        
        loginButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;");
        signupButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;");

        HBox buttonBox = new HBox(20, loginButton, signupButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        //sign up button takes you to signup.java 
        signupButton.setOnAction(e -> {
            SignUp signupScreen = new SignUp();
            try {
                signupScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        loginButton.setOnAction(e -> {
            TaskApp taskApp = new TaskApp();
            try {
                taskApp.start(primaryStage); // Reuse the same window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Forgot password (phase 3) 
        //Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        //forgotPassword.setTextFill(Color.DARKTURQUOISE);

        //  Main layout
        VBox layout = new VBox(20, header, inputBox, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setPrefSize(400, 400); // Ensure VBox fills the scene

        // Background image
        Image bgImage = new Image(Main.class.getResource("starsbackground.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(100, 100, true, true, false, true) // Stretch to fill VBox
            
        );
        layout.setBackground(new Background(backgroundImage));

        //Scene setup
        Scene scene = new Scene(layout, 400, 400);

        // Optional: bind VBox size to scene size for full coverage
        scene.widthProperty().addListener((obs, oldVal, newVal) -> layout.setPrefWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> layout.setPrefHeight(newVal.doubleValue()));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

	public static Scene getLogin(Stage primaryStage) {
		// TODO Auto-generated method stub
		return null;
	}
}
