package application;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;

public class TaskListScene {
    private static int completedTaskCount = 0;
    private static List<Star> progressStars = new ArrayList<>();
    private static HBox starBar = new HBox(10);

    public static HBox getStarBar() {
        starBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Star star = new Star(0, 0, 10);
            progressStars.add(star);
            starBar.getChildren().add(star);
        }
        return starBar;
    }

    public static void taskCompleted(Stage stage) {
        if (completedTaskCount < 5) {
          //  progressStars.get(completedTaskCount).setComplete(true);
            completedTaskCount++;

            if (completedTaskCount == 5) {
                transitionToConstellationScene(stage);
            }
        }
    }

    private static void transitionToConstellationScene(Stage stage) {
    	
    	ConstellationScene.show(stage);
       // VBox constellationLayout = new VBox(20);
       // constellationLayout.setAlignment(Pos.CENTER);
       //.getChildren().add(new Label("✨ Constellation Complete! ✨"));

       // Scene constellationScene = new Scene(constellationLayout, 1000, 600);
       // stage.setScene(constellationScene);
    }
}