package application;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.Random;

public class AffirmationBubble extends StackPane {
    
    private static final String[] AFFIRMATIONS = {
        "You're doing great! :)",
        "Keep up the amazing work! <3",
        "One step at a time!",
        "You've got this!",
        "Progress, not perfection! :)",
        "Believe in yourself! <3",
        "Every task completed is a win!",
        "You're making it happen!",
        "Stay focused and positive! :)",
        "Small steps lead to big results!",
        "Your effort matters! <3",
        "You're stronger than you think!",
        "Keep pushing forward! :)",
        "Today is your day!",
        "You're capable of amazing things! <3",
        "Trust the process :)",
        "You're on the right path!",
        "Consistency is key! <3",
        "Be proud of your progress!",
        "You're doing better than you realize! :)",
        "Keep going, you're awesome! <3",
        "Your hard work is paying off!",
        "You can handle this! :)",
        "Take it one task at a time!",
        "You're exactly where you need to be! <3"
    };
    
    private Label affirmationLabel;
    
    public AffirmationBubble() {
        // get random affirmation
        Random random = new Random();
        String affirmation = AFFIRMATIONS[random.nextInt(AFFIRMATIONS.length)];
        
        
        affirmationLabel = new Label(affirmation);
        affirmationLabel.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #6a5acd, #483d8b);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.3), 5, 0.3, 0, 1);" +
                "-fx-font-size: 20px;" +
                "-fx-font-family: 'Consolas';"
            );
        
        this.getChildren().add(affirmationLabel);
        this.setStyle("-fx-padding: 10;");
        this.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.layout.HBox.setHgrow(this, javafx.scene.layout.Priority.ALWAYS);
        this.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}