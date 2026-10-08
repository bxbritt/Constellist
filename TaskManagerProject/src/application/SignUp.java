package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class SignUp extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Sign Up");

        // header
        Label title = new Label("Create Your Account");
        title.getStyleClass().add("glow-text");
        title.setStyle("-fx-font-size: 55px; -fx-font-weight: bold;");

        Label tagline = new Label("Begin your constellation journey");
        tagline.getStyleClass().add("glow-text");
        tagline.setStyle("-fx-font-size: 22px;");

        VBox header = new VBox(8, title, tagline);
        header.setAlignment(Pos.CENTER);

        // input fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("custom-textfield");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("custom-textfield");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("custom-passwordfield");

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm Password");
        confirmField.getStyleClass().add("custom-passwordfield");

        VBox inputBox = new VBox(12, usernameField, emailField, passwordField, confirmField);
        inputBox.setAlignment(Pos.CENTER);

        // buttons
        Button createAccount = new Button("Create Account");
        createAccount.getStyleClass().add("bubble-button");

        Button backButton = new Button("Back to Login");
        backButton.getStyleClass().add("bubble-button");

        HBox buttonBox = new HBox(20, createAccount, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        // main layout
        VBox layout = new VBox(25, header, inputBox, buttonBox);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #071229, #0D234F, #280c4c);"
        );

        // animated star overlay
        StarOverlay stars = new StarOverlay(180);

        root.getChildren().addAll(stars, layout);

        ScreenManager.show(primaryStage, root);

        // button handlers

        backButton.setOnAction(e -> new Main().start(primaryStage));

        createAccount.setOnAction(e -> {

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirm = confirmField.getText();

            // Validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
                return;
            }

            if (!password.equals(confirm)) {
                showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
                return;
            }

            // login looks users up by username OR email, so both must be unique
            if (Database.getUserId(username) != -1 || Database.getUserId(email) != -1) {
                showAlert(Alert.AlertType.ERROR, "That username or email is already registered.");
                return;
            }

            try (Connection conn = Database.connect()) {

                String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
                var stmt = conn.prepareStatement(sql);

                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, PasswordUtils.hashPassword(password));

                stmt.executeUpdate();

                // store the new user ID
                LoggedInUser.setId(Database.getUserId(username));
                LoggedInUser.setUsername(username);

                // move to TaskApp
                new TaskApp().start(primaryStage);

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error: " + ex.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(ScreenManager.getStage());
        alert.setContentText(message);
        alert.showAndWait();
    }
}
