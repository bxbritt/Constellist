package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;

public class TaskList extends VBox {
    private VBox taskContainer = new VBox(10);
    private final int TASK_LIMIT = 10;

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
        
        // add task button
        Button addTaskButton = new Button("Add Task");
        addTaskButton.getStyleClass().add("bubble-button");  
        
        // progress button
        Button viewProgressButton = new Button("View Progress");
        viewProgressButton.getStyleClass().add("bubble-button");
        
        // hidden input field
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a task...");
        taskInputField.setVisible(false);	// hidden initially
        
        // shows input field when add task is clicked
        addTaskButton.setOnAction(e -> {
            if (taskContainer.getChildren().size() < TASK_LIMIT) {
            	addTaskButton.setVisible(false);
                taskInputField.setVisible(true);
                taskInputField.requestFocus();
            } else {
                Alert limitAlert = new Alert(Alert.AlertType.WARNING);
                limitAlert.setTitle("Task Limit Reached");
                limitAlert.setHeaderText(null);
                limitAlert.setContentText("This list can only hold 10 tasks.\nPlease create a new task list.");
                limitAlert.show();
            }
        });

        // press enter to confirm and add task to list
        taskInputField.setOnAction(e -> {
        	String text = taskInputField.getText().trim();
        	if (!text.isEmpty()) {
        		TaskItem task = new TaskItem(text, taskContainer, this);
        		taskContainer.getChildren().add(task);
        		taskInputField.clear();
        		taskInputField.setVisible(false);	// hides again
        		addTaskButton.setVisible(true);	// shows button again
        	}
        });
        
        viewProgressButton.setOnAction(e -> {
            Stage progressStage = new Stage();
            VBox layout = new VBox(20);
            layout.setStyle("-fx-padding: 20; -fx-background-color: #1B1640;");
            Text placeholder = new Text("Constellation progress will appear here.");
            placeholder.getStyleClass().add("constellation-label");
            layout.getChildren().add(placeholder);
            Scene scene = new Scene(layout, 400, 200);
            progressStage.setScene(scene);
            progressStage.setTitle("Constellation Progress");
            progressStage.show();
        });
        
        this.getChildren().addAll(titleBar, taskInputField, addTaskButton, viewProgressButton, taskContainer);
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
           // popup.setContentText("Constellation here");
           // popup.show();
        }
    }
}