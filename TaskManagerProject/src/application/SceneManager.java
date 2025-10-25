package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchToLogin() {
        Scene loginScene = Main.getLogin(primaryStage);
        primaryStage.setScene(loginScene);
    }

    public static void switchToSignUp() {
        Scene signUpScene = SignUp.getSignUp(primaryStage);
        primaryStage.setScene(signUpScene);
    }

}
    // Add more transitions as your app grows
