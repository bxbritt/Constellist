package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class ConstellationGallery {

    private ConstellationManager constellationManager;
    private ArrayList<VBox> galleryCards = new ArrayList<>();
    private HBox constellationRow = new HBox(40);

    public ConstellationGallery() {
        constellationManager = ConstellationManager.getInstance();
    }

    public Node getView() {

        // gallery title
        Text title = new Text("Constellation Gallery");
        title.getStyleClass().add("glow-text");
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        // shared task card style
        String taskCardStyle =
                "-fx-background-color: rgba(150, 80, 200, 0.06);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: rgba(210, 140, 255, 0.4);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 2.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(210, 140, 255, 0.35), 16, 0.35, 0, 0);";

        // prepare 10 cards
        for (int i = 0; i < 10; i++) {
            VBox card = new VBox(10);
            galleryCards.add(card);
        }

        // fill cards
        for (int i = 0; i < galleryCards.size(); i++) {

            VBox card = galleryCards.get(i);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(260, 330);
            card.setStyle(taskCardStyle);

            Constellation constellation = constellationManager.getConstellationByIndex(i);

            card.getChildren().clear();

            if (constellation != null && constellation.isComplete()) {

                Pane preview = constellation.createPreview(300, 300);
                preview.setStyle("-fx-background-color: transparent;");
                preview.setScaleX(1.5);
                preview.setScaleY(1.5);

                Text name = new Text(constellation.getName());
                name.getStyleClass().add("glow-text");
                name.setStyle("-fx-font-size: 18px;");

                Text progress = new Text(constellation.getStarsLit() + "/10 stars");
                progress.setStyle("-fx-font-size: 14px; -fx-fill: #dcdcff;");

                card.getChildren().addAll(preview, name, progress);

            } else {

                Text locked = new Text("Locked\nConstellation " + (i + 1));
                locked.getStyleClass().add("glow-text");
                locked.setStyle("-fx-font-size: 16px; -fx-opacity: 0.7;");

                card.getChildren().add(locked);
            }
        }

        // row of cards
        constellationRow.setAlignment(Pos.CENTER);
        constellationRow.setPadding(new Insets(20));
        constellationRow.getChildren().addAll(galleryCards);

        ScrollPane scrollPane = new ScrollPane(constellationRow);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // layout
        VBox layout = new VBox(30, title, scrollPane);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }
}
