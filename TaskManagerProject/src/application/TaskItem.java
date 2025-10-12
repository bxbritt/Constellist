package application;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.stage.Window;

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
                
                
                Window window = taskText.getScene().getWindow();
                if (window instanceof Stage) {
                TaskListScene.taskCompleted((Stage) window);
   
            } else {
                taskText.getStyleClass().remove("completed");
            }
            parentContainer.checkCompletion();
            }});

        deleteButton.setOnAction(e -> {
            parentList.getChildren().remove(this);
            parentContainer.checkCompletion();
        });
    }

    public boolean isCompleted() {
        return completed;
    }
}