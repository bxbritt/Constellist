package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class TaskList extends VBox {

    private VBox taskContainer = new VBox(10);
    private final int TASK_LIMIT = 10;
    private int listId;

    private StarManager starManager;

    // title components
    private TextField titleField;
    private Label titleLabel; 

    public TaskList(String initialTitle, int listId, StarManager starManager) {
        this.listId = listId;
        this.starManager = starManager;
        this.setSpacing(10);
        this.getStyleClass().add("task-box");

        // title field
        titleField = new TextField(initialTitle.equals("Task List") ? "" : initialTitle);
        titleField.getStyleClass().add("task-title-field");
        titleField.setPromptText("Enter a title...");

        // title label and glow
        titleLabel = new Label(initialTitle);
        titleLabel.getStyleClass().add("glow-text");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titleLabel.setVisible(false); // hidden until title is confirmed
        
        // force label not to shrink
        titleLabel.setMinWidth(Region.USE_PREF_SIZE);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // buttons
        Button addTaskButton = new Button("Add Task");
        addTaskButton.getStyleClass().add("bubble-button");
        addTaskButton.setVisible(false);

        Button viewProgressButton = new Button("View Progress");
        viewProgressButton.getStyleClass().add("bubble-button");
        viewProgressButton.setVisible(false);

        // title edit and label
        titleField.setOnAction(e -> saveTitle(addTaskButton, viewProgressButton));
        titleField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                titleField.setEditable(true);
                titleField.setDisable(false);
                titleField.requestFocus();
            }
        });

        // label and edit
        titleLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                titleField.setVisible(true);
                titleLabel.setVisible(false);
                titleField.setEditable(true);
                titleField.setDisable(false);
                titleField.requestFocus();
            }
        });

        // close button
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> {
            if (this.getParent() instanceof FlowPane container) {
                container.getChildren().remove(this);
                Database.deleteTaskList(listId);
            }
        });

        // title bar
        HBox titleBar = new HBox(10);
        titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBar.getChildren().addAll(titleField, titleLabel, spacer, closeButton);

        // task input
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a task...");
        taskInputField.setVisible(false);

        // add task button logic
        addTaskButton.setOnAction(e -> {
            if (taskContainer.getChildren().size() < TASK_LIMIT) {
                addTaskButton.setVisible(false);
                taskInputField.setVisible(true);
                taskInputField.requestFocus();
            } else {
                Alert limitAlert = new Alert(Alert.AlertType.WARNING);
                limitAlert.setTitle("Task Limit Reached");
                limitAlert.setHeaderText(null);
                limitAlert.setContentText("This list can only hold 10 tasks.\nPlease create a new list.");
                limitAlert.show();
            }
        });

        // add task input logic
        taskInputField.setOnAction(e -> {
            String text = taskInputField.getText().trim();
            if (!text.isEmpty()) {
                TaskItem task = new TaskItem(text, taskContainer, this);
                taskContainer.getChildren().add(task);

                Database.saveTaskItem(listId, text);
                startCompletionWatcher();

                taskInputField.clear();
                taskInputField.setVisible(false);
                addTaskButton.setVisible(true);
            }
        });

        // view progress button
        viewProgressButton.setOnAction(e -> {
            ProgressScene progressScene = new ProgressScene();
            try {
                progressScene.start((Stage) this.getScene().getWindow());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.getChildren().addAll(titleBar, taskInputField, addTaskButton, viewProgressButton, taskContainer);

        // if list already has a title → show label, hide field
        if (!initialTitle.trim().isEmpty()) {
            activateLabelMode(initialTitle, addTaskButton, viewProgressButton);
        }
    }

    // save title and switch label
    private void saveTitle(Button addTask, Button viewProgress) {
        String title = titleField.getText().trim();

        if (!title.isEmpty()) {
            activateLabelMode(title, addTask, viewProgress);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Title");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a title before adding tasks.");
            alert.show();
        }
    }

    private void activateLabelMode(String title, Button addTask, Button viewProgress) {
        titleField.setEditable(false);
        titleField.setDisable(true);
        titleField.setVisible(false);
        titleField.setManaged(false);

        titleLabel.setText(title);
        titleLabel.setVisible(true);
        titleLabel.setManaged(true);

        addTask.setVisible(true);
        viewProgress.setVisible(true);
    }

    // completion tracking
    public void checkCompletion() {
        boolean allDone = taskContainer.getChildren().stream()
                .filter(node -> node instanceof TaskItem)
                .map(node -> (TaskItem) node)
                .allMatch(TaskItem::isCompleted);

        if (allDone && taskContainer.getChildren().size() == 10) {
            javafx.application.Platform.runLater(() -> {
                Stage stage = (Stage) this.getScene().getWindow();
                C1_Heart.show(stage);
            });
        }
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

    public void onTaskCompleted() {
        if (starManager != null) {
            starManager.earnStar();
        }
    }
}
