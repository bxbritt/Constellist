package application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class ConstellationScene {

    // shows constellation animation on stage
    public static void show(Stage stage) {

        // build stars
        Star star1 = new Star(300, 300, 6); star1.setComplete(true);
        Star star2 = new Star(400, 200, 6); star2.setComplete(true);
        Star star3 = new Star(500, 300, 6); star3.setComplete(true);
        Star star4 = new Star(400, 500, 6); star4.setComplete(true);
        Star star5 = new Star(400, 300, 6); star5.setComplete(true);

        // build links
        StarLink[] links = {
            new StarLink(star1, star2), new StarLink(star2, star3),
            new StarLink(star3, star4), new StarLink(star4, star1),
            new StarLink(star5, star1), new StarLink(star5, star2),
            new StarLink(star5, star3), new StarLink(star5, star4)
        };

        for (StarLink link : links) link.draw();

        // pane for constellation
        Pane root = new Pane();
        root.setStyle("-fx-background-color: transparent;"); // fix: remove black background

        for (StarLink link : links)
            root.getChildren().addAll(link.getGlowLine(), link.getCoreLine());

        root.getChildren().addAll(star1, star2, star3, star4, star5);

        Scene scene = new Scene(root, 1000, 600);
        scene.setFill(null); // fix: allow transparency behind animation
        stage.setScene(scene);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {

            VBox messageBox = new VBox(10);
            messageBox.setAlignment(Pos.CENTER);
            messageBox.setLayoutX(350);
            messageBox.setLayoutY(220);

            Label message = new Label("✨ Constellation Complete ✨");
            message.setStyle("-fx-text-fill: #ffe8a3; -fx-font-size: 28px; -fx-font-weight: bold;");

            Button returnButton = new Button("Return to Tasks");
            returnButton.getStyleClass().add("bubble-button");

            Button galleryButton = new Button("Go to Gallery");
            galleryButton.getStyleClass().add("bubble-button");

            // fix: use new navigation system
            returnButton.setOnAction(e -> {
                TaskApp taskApp = new TaskApp();
                try {
                    taskApp.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // fix: gallery loads inside taskapp layout, not a new stage
            galleryButton.setOnAction(e -> {
                ConstellationGallery gallery = new ConstellationGallery();
                TaskApp.setCenterContentStatic(gallery.getView());
            });

            messageBox.getChildren().addAll(message, returnButton, galleryButton);
            root.getChildren().add(messageBox);
        });

        delay.play();
    }
}
