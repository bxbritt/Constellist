package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class TaskList extends VBox {

    // task container
    private VBox taskContainer = new VBox(10);

    // task limit
    private final int TASK_LIMIT = 10;

    // list id
    private int listId;

    // title components
    private TextField titleField;
    private Label titleLabel;

    public TaskList(String initialTitle, int listId) {
        this.listId = listId;

        // spacing and card style
        this.setSpacing(12);
        this.getStyleClass().add("task-box");

        // title field
        titleField = new TextField(initialTitle.equals("Task List") ? "" : initialTitle);
        titleField.getStyleClass().add("task-title-field"); // transparent title field
        titleField.setPromptText("Enter a title...");

        // title label
        titleLabel = new Label(initialTitle);
        titleLabel.getStyleClass().add("glow-text");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titleLabel.setVisible(false);

        // prevent label from shrinking
        titleLabel.setMinWidth(Region.USE_PREF_SIZE);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // add task button
        Button addTaskButton = new Button("add task");
        addTaskButton.getStyleClass().add("bubble-button");
        addTaskButton.setVisible(false);

        // view progress button
        Button viewProgressButton = new Button("view progress");
        viewProgressButton.getStyleClass().add("bubble-button");
        viewProgressButton.setVisible(false);

        // save title on enter
        titleField.setOnAction(e -> saveTitle(addTaskButton, viewProgressButton));

        // enable title editing on double click
        titleField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                titleField.setEditable(true);
                titleField.setDisable(false);
                titleField.requestFocus();
            }
        });

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
        Button closeButton = new Button("x");
        closeButton.getStyleClass().add("close-button");

        closeButton.setOnAction(e -> {
            if (this.getParent() instanceof FlowPane container) {
                container.getChildren().remove(this);
                Database.deleteTaskList(listId);
            }
        });

        // title bar layout
        HBox titleBar = new HBox(10);
        titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBar.getChildren().addAll(titleField, titleLabel, spacer, closeButton);

        // task input field
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a task...");
        taskInputField.setVisible(false);
        taskInputField.getStyleClass().add("task-input"); // transparent task field

        // add task logic
        addTaskButton.setOnAction(e -> {
            if (taskContainer.getChildren().size() < TASK_LIMIT) {
                addTaskButton.setVisible(false);
                taskInputField.setVisible(true);
                taskInputField.requestFocus();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Task Limit Reached!");
                a.setHeaderText(null);
                a.setContentText("This list can only hold 10 tasks.\nplease create a new list.");
                a.show();
            }
        });

        // add task on enter
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

        // progress button logic
        viewProgressButton.setOnAction(e -> {
            Sound_Effects.playPianoKey();
            LoggedInUser.setLastViewedListId(listId);
            ProgressScene progressScene = new ProgressScene(listId);
            TaskApp.setCenterContentStatic(progressScene.getView());
        });

        // add nodes to card
        this.getChildren().addAll(titleBar, taskInputField, addTaskButton, viewProgressButton, taskContainer);

        // switch to label mode for existing lists
        if (!initialTitle.trim().isEmpty()) {
            activateLabelMode(initialTitle, addTaskButton, viewProgressButton);
        }
    }

    private void saveTitle(Button addTask, Button viewProgress) {
        String title = titleField.getText().trim();

        if (!title.isEmpty()) {
            activateLabelMode(title, addTask, viewProgress);
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Missing Title");
            a.setHeaderText(null);
            a.setContentText("please enter a title before adding tasks.");
            a.show();
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

    public void checkCompletion() {
        boolean allDone = taskContainer.getChildren().stream()
                .filter(node -> node instanceof TaskItem)
                .map(node -> (TaskItem) node)
                .allMatch(TaskItem::isCompleted);
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
}
