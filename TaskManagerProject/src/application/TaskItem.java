package application;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TaskItem extends HBox {

    private CheckBox checkBox;
    private Label taskText;               // changed from Text to Label so css can style color
    private Button deleteButton;
    private boolean completed = false;

    public TaskItem(String description, VBox parentList, TaskList parentContainer) {

        // checkbox
        checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");

        // task text label
        taskText = new Label(description);
        taskText.getStyleClass().add("task-item-label");    // glowing white-gold text

        // delete button
        deleteButton = new Button("delete");
        deleteButton.getStyleClass().add("bubble-button");

        // layout settings
        this.setSpacing(12);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("task-item-row");

        this.getChildren().addAll(checkBox, taskText, deleteButton);

        // checkbox completion logic
        checkBox.setOnAction(e -> {
            completed = checkBox.isSelected();

            if (completed) {
                // play sound
                Sound_Effects.playChime();

                // mark completed visually
                taskText.getStyleClass().add("completed");

                // update db
                Database.markTaskItemCompleted(parentContainer.getListId(), taskText.getText().trim());

                // increment constellation progress
                ConstellationManager cm = ConstellationManager.getInstance();
                Constellation current = cm.getCurrentConstellation();

                if (cm.completeTask()) {
                    Platform.runLater(() -> showConstellationPopup(current, parentList, parentContainer));
                }

                // fade out and remove
                FadeTransition fade = new FadeTransition(Duration.millis(500), this);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(event -> parentList.getChildren().remove(this));
                fade.play();

                parentContainer.checkCompletion();

            } else {
                taskText.getStyleClass().remove("completed");
            }
        });

        // delete button logic
        deleteButton.setOnAction(e -> {
            Database.deleteTaskItem(parentContainer.getListId(), taskText.getText().trim());
            parentList.getChildren().remove(this);
            parentContainer.checkCompletion();
        });
    }

    // return completion state
    public boolean isCompleted() {
        return completed;
    }

    // popup when constellation is finished
    private void showConstellationPopup(Constellation constellation, VBox parentList, TaskList parentContainer) {

        Stage popup = new Stage();
        popup.initOwner(ScreenManager.getStage()); // keeps it in front when fullscreen
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Constellation Completed!");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #071229, #0d234f, #280c4c);");

        Label msg = new Label("✨ " + constellation.getName() + " Completed! ✨");
        msg.getStyleClass().add("glow-text");
        msg.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        BorderPane.setAlignment(msg, Pos.TOP_CENTER);
        root.setTop(msg);

        StackPane centerPane = new StackPane();
        StarOverlay stars = new StarOverlay(120);
        Pane showPane = constellation.createShow(450, 300);
        showPane.setStyle("-fx-background-color: transparent;");

        centerPane.getChildren().addAll(stars, showPane);
        root.setCenter(centerPane);

        Button close = new Button("back to tasks");
        close.getStyleClass().add("bubble-button");
        close.setOnAction(ev -> popup.close());
        BorderPane.setAlignment(close, Pos.BOTTOM_CENTER);
        root.setBottom(close);

        Scene scene = new Scene(root, 650, 520);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        popup.setScene(scene);
        popup.showAndWait();

        ConstellationManager.getInstance().advanceToNextConstellation();
    }
}
