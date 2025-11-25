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

import java.sql.Connection;

import application.Main;

public class SignUp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");
        
        Database.createUsersTable(); // Ensures the table exists before inserting

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
        
        //button to go back to main 
        backButton.setOnAction(e -> {
            Main mainScreen = new Main();
            try {
                mainScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        //button that will take user to TaskApp after signing up 
        
        createAccountButton.setOnAction(e -> {
            TaskApp taskApp = new TaskApp();
            try {
                taskApp.start(primaryStage); // Reuse the same window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        createAccountButton.setOnAction(e -> {
            String username = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
                return;
            }

            String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";

            try (Connection conn = Database.connect();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, email);
                String hashedPassword = PasswordUtils.hashPassword(password);
                pstmt.setString(3, hashedPassword);
                pstmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Account created successfully!");

                //  Get the new user's ID and set it in LoggedInUser
                int newUserId = Database.getUserId(username);
                LoggedInUser.setId(newUserId);
                
                // code from caitlyn
                // this will store username for new users
                LoggedInUser.setUsername(username);
                // end code from caitlyn

                // Transition to TaskApp with the new user
                TaskApp taskApp = new TaskApp();
                taskApp.start(primaryStage);

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
            }
        });

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

	public static Scene getSignUp(Stage primaryStage) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//used to make alerts if user got wrong password or other messages 
	private void showAlert(Alert.AlertType type, String message) {
	    Alert alert = new Alert(type);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
}