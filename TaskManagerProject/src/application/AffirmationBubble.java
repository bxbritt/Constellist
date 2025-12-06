package application;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.Random;

public class AffirmationBubble extends StackPane {

    // affirmation messages
    private static final String[] AFFIRMATIONS = {
        "you're doing great! :)",
        "keep up the amazing work! <3",
        "one step at a time!",
        "you've got this!",
        "progress, not perfection! :)",
        "believe in yourself! <3",
        "every task completed is a win!",
        "you're making it happen!",
        "stay focused and positive! :)",
        "small steps lead to big results!",
        "your effort matters! <3",
        "you're stronger than you think!",
        "keep pushing forward! :)",
        "today is your day!",
        "you're capable of amazing things! <3",
        "trust the process :)",
        "you're on the right path!",
        "consistency is key! <3",
        "be proud of your progress!",
        "you're doing better than you realize! :)",
        "keep going, you're awesome! <3",
        "your hard work is paying off!",
        "you can handle this! :)",
        "take it one task at a time!",
        "you're exactly where you need to be! <3"
    };

    private Label affirmationLabel;

    public AffirmationBubble() {

        // choose random affirmation
        Random random = new Random();
        String affirmation = AFFIRMATIONS[random.nextInt(AFFIRMATIONS.length)];

        // label styling to match global ui theme
        affirmationLabel = new Label(affirmation);
        affirmationLabel.getStyleClass().add("glow-text");
        affirmationLabel.setStyle(
                "-fx-background-color: rgba(255,255,255,0.10);" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 8 14;" +
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #ffe8a3;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.35), 9, 0.35, 0, 0);"
        );

        // bubble container style
        this.getChildren().add(affirmationLabel);
        this.setStyle("-fx-padding: 10;");
        this.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.layout.HBox.setHgrow(this, javafx.scene.layout.Priority.ALWAYS);
        this.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        // fade in animation for smooth appearance
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}
