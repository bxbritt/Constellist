// Java
package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import application.TaskApp;

import java.util.ArrayList;

public class ConstellationGallery extends Application {
    private int selectedIndex = 0; // track which card is centered
    private ArrayList<VBox> galleryCards = new ArrayList<>(); // store all cards
    private HBox constellationRow = new HBox(40); //container for visible cards

    @Override
    public void start(Stage stage) {
        double cardWidth = 300;
        double cardHeight = 220;
        double windowWidth = 1000;
        double windowHeight = 600;

        // create all cards and add to galleryCards list
        for (int i = 1; i <= 10; i++) {
            VBox galleryCard = new VBox(8);
            galleryCard.setAlignment(Pos.CENTER);
            galleryCard.setPrefSize(cardWidth, cardHeight);
            galleryCard.getStyleClass().add("galleryCard");
            Text name = new Text("Constellation " + i);
            galleryCard.getChildren().add(name);
            galleryCards.add(galleryCard);
        }

        constellationRow.getStyleClass().add("constellation-row");

        // scrollPane for horizontal scrolling
        // Remove all image-related code and use only the carousel row
        ScrollPane scrollPane = new ScrollPane(constellationRow);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane"); // use css for background
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(windowWidth, windowHeight);

        // navigation buttons
        Button leftButton = new Button("<");
        Button rightButton = new Button(">");
        leftButton.getStyleClass().add("bubble-button");
        rightButton.getStyleClass().add("bubble-button");
        leftButton.setOnAction(e -> {
            if (selectedIndex > 0) {
                selectedIndex--;
                updateCarousel();
            }
        });
        rightButton.setOnAction(e -> {
            if (selectedIndex < galleryCards.size() - 1) {
                selectedIndex++;
                updateCarousel();
            }
        });

        updateCarousel();

        Button taskButton = new Button("Back to Tasks");
        taskButton.getStyleClass().add("bubble-button");
        taskButton.setOnAction(e -> {
            TaskApp taskApp = new TaskApp();
            try {
                taskApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(20, taskButton, leftButton, scrollPane, rightButton);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene gallery = new Scene(layout, windowWidth, windowHeight);
        gallery.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(gallery);
        stage.setTitle("Constellation Gallery");
        stage.show();
    }

    // show previous, current, and next card in the row
    private void updateCarousel() {
        constellationRow.getChildren().clear();
        if (selectedIndex > 0) {
            constellationRow.getChildren().add(galleryCards.get(selectedIndex - 1));
        }
        VBox centerCard = galleryCards.get(selectedIndex);
        centerCard.getStyleClass().add("center-card"); // add emphasis class
        constellationRow.getChildren().add(centerCard);
        if (selectedIndex < galleryCards.size() - 1) {
            constellationRow.getChildren().add(galleryCards.get(selectedIndex + 1));
        }
        // remove highlight from other cards
        for (int i = 0; i < galleryCards.size(); i++) {
            if (i != selectedIndex) {
                galleryCards.get(i).getStyleClass().remove("center-card");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}