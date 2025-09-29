package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import application.TaskApp;
import application.ConstellationScene;


/* THIS IS THE PAGE 'go to galley' LEADS THE USER TO
 * 
 * 
 * class GalleryPage extends Application
*    start(Stage stage):
*        create HBox to hold constellations
*        for each constellation:
*            create a card with constellation name + progress bar
*            add card to HBox
*
*        wrap HBox inside a ScrollPane
*        set ScrollPane to scroll -- horizontally
*
*        add a "Back to Tasks" button to return to TaskApp
*
*        create scene with scrollable constellation list
*        set stage scene to this new scene*/


public class ConstellationGallery extends Application {

    @Override
    public void start(Stage stage) {
        // container for constellation cards
        HBox constellationRow = new HBox(20);
        constellationRow.setPadding(new Insets(20));
        constellationRow.setAlignment(Pos.CENTER_LEFT);

        // placeholder cards
        for (int i = 1; i <= 5; i++) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(10));
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 10; -fx-padding: 15; -fx-background-radius: 10; -fx-background-color: #f9f9f9;");

            Text name = new Text("Constellation " + i); //later change to list name

            constellationRow.getChildren().add(card);
        }

        // ScrollPane for horizontal scrolling
        ScrollPane scrollPane = new ScrollPane(constellationRow);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);

        // back button to go back to TaskApp
        Button backButton = new Button("Back to Tasks");
        backButton.setOnAction(e -> {
            TaskApp taskApp = new TaskApp();
            try {
                taskApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // layout for the gallery
        VBox layout = new VBox(20, backButton, scrollPane);
        layout.setPadding(new Insets(20));

        // scene
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Constellation Gallery");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

