package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;

public class TaskList extends VBox {
    private VBox taskContainer = new VBox(5);
    private final int TASK_LIMIT = 5;

    public TaskList(String initialTitle) {
        this.setSpacing(10);
        this.getStyleClass().add("task-box");
        
        // title bar with close button
        TextField titleField = new TextField(initialTitle);
        titleField.getStyleClass().add("task-title-field");
        titleField.setEditable(false);
        
        // double click to edit
        titleField.setOnMouseClicked(e -> {
        	if (e.getClickCount() == 2) {
        		titleField.setEditable(true);
        		titleField.requestFocus();
        	}
        });
        
        // press enter to confirm edit
        titleField.setOnAction(e -> {
        	titleField.setEditable(false);
        });
        
        // close button to remove the entire list
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> {
        	if (this.getParent() instanceof FlowPane container) {
        		container.getChildren().remove(this);
        	}
        });
        
        // layout of title bar
        HBox titleBar = new HBox(10, titleField, closeButton);
        titleBar.setStyle("-fx-alignment: center-right;");
        
        // input field and button
        TextField inputField = new TextField();
        inputField.setPromptText("Enter a task...");
        Button addButton = new Button("Add Task");
        addButton.getStyleClass().add("bubble-button");

        addButton.setOnAction(e -> {
            if (taskContainer.getChildren().size() < TASK_LIMIT) {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    TaskItem task = new TaskItem(text, taskContainer, this);
                    taskContainer.getChildren().add(task);
                    inputField.clear();
                }
            } else {
                Alert limitAlert = new Alert(Alert.AlertType.WARNING);
                limitAlert.setTitle("Task Limit Reached");
                limitAlert.setHeaderText(null);
                limitAlert.setContentText("This list can only hold 5 tasks.\nPlease create a new task list.");
                limitAlert.show();
            }
        });

        this.getChildren().addAll(titleBar, inputField, addButton, taskContainer);
    }

    public void checkCompletion() {
        boolean allDone = taskContainer.getChildren().stream()
            .filter(node -> node instanceof TaskItem)
            .map(node -> (TaskItem) node)
            .allMatch(TaskItem::isCompleted);

        if (allDone && taskContainer.getChildren().size() > 0) {
            Alert popup = new Alert(Alert.AlertType.INFORMATION);
            popup.setTitle("Constellation Complete");
            popup.setHeaderText(null);
            popup.setContentText("Constellation here");
            popup.show();
        }
    }
}