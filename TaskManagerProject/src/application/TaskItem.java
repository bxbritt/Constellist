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
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

//        checkBox.setOnAction(e -> {
//            completed = checkBox.isSelected();
//            if (completed) {
//                taskText.getStyleClass().add("completed");
//
//                // Update DB
//                Database.markTaskItemCompleted(parentContainer.getListId(), taskText.getText().trim());
//
//                // Increment constellation progress
//                ConstellationManager cm = ConstellationManager.getInstance();
//                cm.completeTask();
//
//                // Check completion
//                Constellation current = cm.getCurrentConstellation();
//                if (current.isComplete()) {
//                    Platform.runLater(() -> {
//                        Stage popup = new Stage();
//                        popup.initModality(Modality.APPLICATION_MODAL);
//                        popup.setTitle("Constellation Completed!");
//
//                        VBox root = new VBox(20);
//                        root.setAlignment(Pos.CENTER);
//                        root.setStyle("-fx-background-color: black;");
//
//                        Label msg = new Label("✨ " + current.getName() + " Completed! ✨");
//                        msg.setStyle("-fx-font-size: 28px; -fx-text-fill: gold;");
//
//                        Button back = new Button("Back to Tasks");
//                        back.setOnAction(ev -> popup.close());
//
//                        root.getChildren().addAll(msg, back);
//
//                        Scene scene = new Scene(root, 600, 400);
//                        popup.setScene(scene);
//                        popup.showAndWait();
//
//                        // Advance after popup closes
//                        cm.advanceToNextConstellation();
//                    });
//                }
//
//                // Fade out and remove
//                FadeTransition fade = new FadeTransition(Duration.millis(500), this);
//                fade.setFromValue(1);
//                fade.setToValue(0);
//                fade.setOnFinished(event -> parentList.getChildren().remove(this));
//                fade.play();
//
//                parentContainer.checkCompletion();
//
//            } else {
//                taskText.getStyleClass().remove("completed");
//            }
//        });
        checkBox.setOnAction(e -> {
            completed = checkBox.isSelected();
            if (completed) {
                taskText.getStyleClass().add("completed");

                // Update DB
                Database.markTaskItemCompleted(parentContainer.getListId(), taskText.getText().trim());

                // Increment constellation progress
                ConstellationManager cm = ConstellationManager.getInstance();
                cm.completeTask();

                // Check completion
                Constellation current = cm.getCurrentConstellation();
                if (current.isComplete()) {
                    Platform.runLater(() -> {
                        Stage popup = new Stage();
                        popup.initModality(Modality.APPLICATION_MODAL);
                        popup.setTitle("Constellation Completed!");

                        BorderPane root = new BorderPane();
                        root.setStyle("-fx-background-color: black;");

                        // Celebration message
                        Label msg = new Label("✨ " + current.getName() + " Completed! ✨");
                        msg.setStyle("-fx-font-size: 28px; -fx-text-fill: gold;");
                        BorderPane.setAlignment(msg, Pos.TOP_CENTER);
                        root.setTop(msg);
                        
                        // Full constellation show
                        Pane showPane = current.createShow(500, 300); // ✅ centered constellation
                        showPane.setPrefSize(500, 300);
                        root.setCenter(showPane);
                        // Back button
                        Button back = new Button("Back to Tasks");
                        back.setOnAction(ev -> popup.close());
                        BorderPane.setAlignment(back, Pos.BOTTOM_RIGHT);
                        root.setBottom(back);

                        Scene scene = new Scene(root, 600, 500);
                        popup.setScene(scene);
                        popup.showAndWait();

                        // Advance after popup closes
                        cm.advanceToNextConstellation();
                    });
                }
                // Fade out and remove
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
        
        deleteButton.setOnAction(e -> {
            //parentList.getChildren().remove(this);
            Database.deleteTaskItem(parentContainer.getListId(), taskText.getText().trim());
            parentList.getChildren().remove(this);
            parentContainer.checkCompletion();
        });
    }

    public boolean isCompleted() {
        return completed;
    }
    
    //helper for popup
    
    private void showCompletionPopup(String constellationName) {
        Stage popup = new Stage();
        popup.setTitle("Constellation Completed!");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        Label msg = new Label("✨ " + constellationName + " Completed! ✨");
        msg.setStyle("-fx-font-size: 28px; -fx-text-fill: gold;");

        Button back = new Button("Back to Tasks");
        back.setOnAction(e -> popup.close());

        root.getChildren().addAll(msg, back);

        Scene scene = new Scene(root, 600, 400);
        popup.setScene(scene);

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
    }
}