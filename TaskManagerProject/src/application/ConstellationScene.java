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
    public static void show(Stage stage) {
        // Build stars
        Star star1 = new Star(300, 300, 6); star1.setComplete(true);
        Star star2 = new Star(400, 200, 6); star2.setComplete(true);
        Star star3 = new Star(500, 300, 6); star3.setComplete(true);
        Star star4 = new Star(400, 500, 6); star4.setComplete(true);
        Star star5 = new Star(400, 300, 6); star5.setComplete(true);

        // Build links
        StarLink[] links = {
            new StarLink(star1, star2), new StarLink(star2, star3),
            new StarLink(star3, star4), new StarLink(star4, star1),
            new StarLink(star5, star1), new StarLink(star5, star2),
            new StarLink(star5, star3), new StarLink(star5, star4)
        };

        // Draw links
        for (StarLink link : links) link.draw();

        // Build scene
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        for (StarLink link : links) {
            root.getChildren().addAll(link.getGlowLine(), link.getCoreLine());
        }
        root.getChildren().addAll(star1, star2, star3, star4, star5);

        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.setTitle("Constellation Demo");
        
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        
        delay.setOnFinished(event -> {
            VBox messageBox = new VBox(10);
           
            messageBox.setLayoutX(500);
            messageBox.setLayoutY(400);

            Label message = new Label("✨ Constellation Complete ✨");
            message.setStyle("-fx-text-fill: #DDA0DD; -fx-font-size: 24px; -fx-font-weight: bold;");
            Button returnButton = new Button("Return to Tasks");

            message.getStyleClass().add("constellation-label");
            returnButton.getStyleClass().add("bubble-button");

            returnButton.setOnAction(e -> {
                TaskApp taskApp = new TaskApp();
                try {
                    taskApp.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            Button galleryButton = new Button("Go to Gallery");
            galleryButton.getStyleClass().add("bubble-button");
            
            galleryButton.setOnAction(e -> {
            ConstellationGallery gallery = new ConstellationGallery();
                try {
                    gallery.start(stage); // switch stage to ConstellationGallery
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            //add buttons return and go to gallery
            messageBox.getChildren().addAll(message, returnButton, galleryButton);
            ((Pane) stage.getScene().getRoot()).getChildren().add(messageBox);
        });
        
        delay.play();
    }
}