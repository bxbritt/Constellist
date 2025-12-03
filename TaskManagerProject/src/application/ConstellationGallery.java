package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ConstellationGallery extends Application {
	
	private ConstellationManager constellationManager;
    private int selectedIndex = 0;
    private ArrayList<VBox> galleryCards = new ArrayList<>();
    private HBox constellationRow = new HBox(40);

    public void start(Stage stage) {
    	constellationManager = ConstellationManager.getInstance();
        double cardWidth = 280;
        double cardHeight = 350;
        double windowWidth = 1000;
        double windowHeight = 600;

        // Title
        Text title = new Text("Constellation Gallery");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 28));
        title.setFill(Color.web("#B388FF"));
        
     // card creation with constellation data
     
        	//create 10 empty cards
        for (int i = 0; i < 10; i++) {
            VBox galleryCard = new VBox(8);
            galleryCards.add(galleryCard);
        }

       // fill and style the cards
        for (int i = 0; i < galleryCards.size(); i++) {
            VBox galleryCard = galleryCards.get(i);
            galleryCard.setAlignment(Pos.CENTER);
            galleryCard.setPrefSize(cardWidth, cardHeight);
            galleryCard.setStyle(
                    "-fx-background-color: black;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-color: #B388FF;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-width: 3;" +
                    "-fx-effect: dropshadow(gaussian, rgba(179, 136, 255, 0.4), 10, 0.5, 0, 2);"
            );

            galleryCard.getChildren().clear();
            
            Constellation constellation = constellationManager.getConstellationByIndex(i);

            // Unlocked state
           
            if (constellation != null && constellation.isComplete()) {
               

                Pane preview = constellation.createPreview(200, 200);

                Text nameText = new Text(constellation.getName());
                nameText.setFont(Font.font("Consolas", 16));
                nameText.setFill(Color.web("#dcdcff"));

                Text progressText = new Text(constellation.getStarsLit() + "/10 Stars");
                progressText.setFont(Font.font("Consolas", 12));
                progressText.setFill(Color.GRAY);

                galleryCard.getChildren().addAll(preview, nameText, progressText);
            }
            else {
                // Locked card
                Text placeholder = new Text("Locked\nConstellation " + (i + 1));
                placeholder.setFont(Font.font("Consolas", 16));
                placeholder.setFill(Color.GRAY);
                galleryCard.getChildren().add(placeholder);
            }
        }
             
        

        constellationRow.setAlignment(Pos.CENTER);
        constellationRow.setPadding(new Insets(20));
        constellationRow.setStyle("-fx-background-color: linear-gradient(to right, #0D0B2D, #2A1E63, #6a5acd, #2A1E63, #0D0B2D);");

        // Add all cards to the row initially
        constellationRow.getChildren().addAll(galleryCards);

        // ScrollPane for horizontal scrolling
        ScrollPane scrollPane = new ScrollPane(constellationRow);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(windowWidth, 450);
        scrollPane.setMaxHeight(450);
        scrollPane.setHvalue(0);

        // Back button

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


        VBox layout = new VBox(25, title, taskButton, scrollPane);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30, 20, 20, 20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #0D0B2D, #1B1640, #2A1E63);");


        Scene gallery = new Scene(layout, windowWidth, windowHeight);
        gallery.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(gallery);
        stage.setTitle("Constellation Gallery");
        stage.show();
        stage.setFullScreen(true);


    }


    public static void main(String[] args) {
        launch();
    }
}

