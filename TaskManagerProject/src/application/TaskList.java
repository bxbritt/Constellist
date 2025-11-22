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
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class TaskList extends VBox {
    private VBox taskContainer = new VBox(10);
    private final int TASK_LIMIT = 10;
    private int listId;
    
    private StarManager starManager;

    // constructor that accepts StarManager
    public TaskList(String initialTitle, int listId, StarManager starManager) {
        this.listId = listId;
        this.starManager = starManager;
        this.setSpacing(10);
        this.getStyleClass().add("task-box");
    
        // Title field
        TextField titleField = new TextField(initialTitle.equals("Task List") ? "" : initialTitle);
        titleField.getStyleClass().add("task-title-field");
        titleField.setEditable(true);
        if (titleField.getText().isEmpty()) {
            titleField.setPromptText("Enter a title...");
        }

        // Buttons
        Button addTaskButton = new Button("Add Task");
        addTaskButton.getStyleClass().add("bubble-button");
        addTaskButton.setVisible(false);

        Button viewProgressButton = new Button("View Progress");
        viewProgressButton.getStyleClass().add("bubble-button");
        viewProgressButton.setVisible(false);

        // Double-click to edit
        titleField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                titleField.setEditable(true);
                titleField.requestFocus();
                addTaskButton.setVisible(false);
                viewProgressButton.setVisible(false);
            }
        });
        
      

        // Press Enter to confirm edit
        titleField.setOnAction(e -> {
            String title = titleField.getText().trim();
            if (!title.isEmpty()) {
                titleField.setEditable(false);
                addTaskButton.setVisible(true);
                viewProgressButton.setVisible(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Title");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a title before adding tasks.");
                alert.show();
            }
        });

        // Close button
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> {
            if (this.getParent() instanceof FlowPane container) {
                container.getChildren().remove(this);
                System.out.println("🗑 Deleting list with ID: " + listId);
                Database.deleteTaskList(listId);
            }
        });

        HBox titleBar = new HBox(10, titleField, closeButton);
        titleBar.setStyle("-fx-alignment: center-right;");

        // Task input
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a task...");
        taskInputField.setVisible(false);

        // Add task button logic
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

        // Task input logic
        taskInputField.setOnAction(e -> {
            String text = taskInputField.getText().trim();
            if (!text.isEmpty()) {
                TaskItem task = new TaskItem(text, taskContainer, this);
                taskContainer.getChildren().add(task);

                System.out.println("📝 Saving task '" + text + "' to list ID: " + listId);
                Database.saveTaskItem(listId, text);
                startCompletionWatcher();

                taskInputField.clear();
                taskInputField.setVisible(false);
                addTaskButton.setVisible(true);
            }
        });

        // View progress button
        viewProgressButton.setOnAction(e -> {
            ProgressScene progressScene = new ProgressScene();
            try {
                // Reuse the same stage so navigation feels seamless
                progressScene.start((Stage) this.getScene().getWindow());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.getChildren().addAll(titleBar, taskInputField, addTaskButton, viewProgressButton, taskContainer);

        if (!titleField.getText().trim().isEmpty()) {
            titleField.setEditable(false);
            addTaskButton.setVisible(true);
            viewProgressButton.setVisible(true);
        }
    }

    public void checkCompletion() {
        boolean allDone = taskContainer.getChildren().stream()
            .filter(node -> node instanceof TaskItem)
            .map(node -> (TaskItem) node)
            .allMatch(TaskItem::isCompleted);

        System.out.println("🔍 Completion check: " + taskContainer.getChildren().size() + " tasks, allDone=" + allDone);


//        if (allDone && taskContainer.getChildren().size() == 10) {
//            javafx.application.Platform.runLater(() -> {
//                Stage stage = (Stage) this.getScene().getWindow();
//                C1_Heart.show(stage);
//            });
//        }
    }

    private void startCompletionWatcher() {
        javafx.animation.Timeline watcher = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> checkCompletion())
        );
        watcher.setCycleCount(10);
        watcher.play();
    }

    public void addItem(String content) {
        TaskItem task = new TaskItem(content, taskContainer, this);
        taskContainer.getChildren().add(task);
    }

    public int getListId() {
        return listId;
    }
    
 // 
    public void onTaskCompleted() {
        if (starManager != null) {
            starManager.earnStar();
        }
    }


    
    
}
