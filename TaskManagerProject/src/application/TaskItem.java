package application;

import javafx.animation.FadeTransition;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class TaskItem extends HBox {
    private CheckBox checkBox;
    private Text taskText;
    private Button deleteButton;
    private boolean completed = false;

    public TaskItem(String description, VBox parentList, TaskList parentContainer) {
        checkBox = new CheckBox();
        taskText = new Text(description);
        deleteButton = new Button("Delete");

        this.setSpacing(10);
        this.getStyleClass().add("task-box");
        taskText.getStyleClass().add("task-text");
        deleteButton.getStyleClass().add("bubble-button");

        this.getChildren().addAll(checkBox, taskText, deleteButton);

        checkBox.setOnAction(e -> {
            completed = checkBox.isSelected();
            if (completed) {
                taskText.getStyleClass().add("completed");

                // ⭐ Update the database so this task is marked completed
                Database.markTaskItemCompleted(parentContainer.getListId(), taskText.getText().trim());

                // Fade out and remove from the UI
                FadeTransition fade = new FadeTransition(Duration.millis(500), this);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(event -> parentList.getChildren().remove(this));
                fade.play();

                // Award a star when task is completed
                parentContainer.onTaskCompleted();
                parentContainer.checkCompletion();
            } else {
                taskText.getStyleClass().remove("completed");
            }
        });
    }

    public boolean isCompleted() {
        return completed;
    }
}