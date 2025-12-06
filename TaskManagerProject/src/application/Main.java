package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    // persistent window size
    private static double savedWidth = 1000;
    private static double savedHeight = 600;

    private void applyPersistentWindowSize(Stage stage) {
        stage.setWidth(savedWidth);
        stage.setHeight(savedHeight);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> savedWidth = newVal.doubleValue());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> savedHeight = newVal.doubleValue());
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Constellist");

        Database.createUsersTable();
        Database.createTasksTable();
        Database.createTaskItemsTable();
        Database.createConstellationProgressTable();

        music.play("/music/menu_music.mp3", 0.25);

        // global ui
        String globalUI =
                "-fx-font-family: 'Century Gothic';" +
                "-fx-text-fill: white;";

        // title and subtitle message with login tagline
        Label title = new Label("Welcome to Constellist");
        title.getStyleClass().add("glow-text");
        title.setStyle(
                "-fx-font-size: 60px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("Complete tasks. Connect stars. Build your universe.");
        subtitle.getStyleClass().add("glow-text");
        subtitle.setStyle("-fx-font-size: 22px;");

        Label tagline = new Label("Log In to begin");
        tagline.getStyleClass().add("glow-text");
        tagline.setStyle("-fx-font-size: 20px;");

        VBox header = new VBox(10, title, subtitle, tagline);
        header.setAlignment(Pos.CENTER);

        // input fields
        TextField emailField = new TextField();
        emailField.setPromptText("Email or Username");
        emailField.getStyleClass().add("custom-textfield");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("custom-passwordfield");

        VBox inputBox = new VBox(15, emailField, passwordField);
        inputBox.setAlignment(Pos.CENTER);

        // buttons
        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("bubble-button");

        Button signupButton = new Button("Sign Up");
        signupButton.getStyleClass().add("bubble-button");

        HBox buttonBox = new HBox(30, loginButton, signupButton);
        buttonBox.setAlignment(Pos.CENTER);

        // forgot password
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setStyle("-fx-text-fill: #ffe8a3; -fx-font-size: 16;");

        // main layout
        VBox layout = new VBox(35, header, inputBox, buttonBox, forgotPasswordLink);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(globalUI);

        // background
        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
        );

        // animated star overlay
        StarOverlay stars = new StarOverlay(180);
        stars.prefWidthProperty().bind(primaryStage.widthProperty());
        stars.prefHeightProperty().bind(primaryStage.heightProperty());

        root.getChildren().addAll(stars, layout);

        // scene and window size logic
        Scene scene = new Scene(root);
        
        try {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (Exception ignored) {}

        applyPersistentWindowSize(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();

        // button handlers
        signupButton.setOnAction(e -> {
            try {
                new SignUp().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loginButton.setOnAction(e -> {
            String input = emailField.getText();
            String password = passwordField.getText();

            if (Database.validateLogin(input, password)) {
                int userId = Database.getUserId(input);
                LoggedInUser.setId(userId);

                String username = Database.getUsernameById(userId);
                LoggedInUser.setUsername(username);

                try {
                    new TaskApp().start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        forgotPasswordLink.setOnAction(e -> {
            try {
                new ForgotPassword().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
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
