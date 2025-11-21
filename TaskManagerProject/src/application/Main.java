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
     
        Database.createUsersTable();
        Database.createTasksTable();
        Database.createTaskItemsTable();
        Database.createConstellationProgressTable();
        
        
        Label title = new Label("Welcome");
        title.setFont(Font.font("Verdana", 28));
        title.setTextFill(Color.DARKTURQUOISE);

        Label tagline = new Label("Log in to begin");
        tagline.setFont(Font.font("Verdana", 14));
        tagline.setTextFill(Color.DARKTURQUOISE);

        VBox header = new VBox(5, title, tagline);
        header.setAlignment(Pos.CENTER);

        TextField emailField = new TextField();
        emailField.setPromptText("Email or Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        VBox inputBox = new VBox(10, emailField, passwordField);
        inputBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Log In");
        Button signupButton = new Button("Sign Up");

        loginButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;");
        signupButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;");

        HBox buttonBox = new HBox(20, loginButton, signupButton);
        buttonBox.setAlignment(Pos.CENTER);

        signupButton.setOnAction(e -> {
            SignUp signupScreen = new SignUp();
            try {
                signupScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loginButton.setOnAction(e -> {
            String input = emailField.getText();
            String password = passwordField.getText();

            if (Database.validateLogin(input, password)) {
                int userId = Database.getUserId(input);         //  Get the user's ID from the database
                LoggedInUser.setId(userId);                     //  Store it globally for later use

                
                TaskApp taskApp = new TaskApp();                // Launch TaskApp as before
                try {
                    taskApp.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
              
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid credentials. Please try again.");
            }
        });
        
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setTextFill(Color.DARKTURQUOISE);
        forgotPasswordLink.setFont(Font.font("Verdana", 12));

        forgotPasswordLink.setOnAction(e -> {
            ForgotPassword forgotScreen = new ForgotPassword();
            try {
                forgotScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
       
    
        
        VBox layout = new VBox(20, header, inputBox, buttonBox, forgotPasswordLink);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setPrefSize(400, 400);

        Image bgImage = new Image(Main.class.getResource("starsbackground.jpg").toExternalForm());
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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}