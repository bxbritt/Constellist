package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ProgressScene {

    private int listId;

    public ProgressScene(int listId) {
        this.listId = listId;
    }

    public Node getView() {

        // page title
        Label title = new Label("Progress Overview");
        title.getStyleClass().add("glow-text");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        VBox header = new VBox(title);
        header.setAlignment(Pos.CENTER);

        String taskCardStyle =
                "-fx-background-color: rgba(150, 80, 200, 0.06);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: rgba(210, 140, 255, 0.4);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 2.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(210, 140, 255, 0.35), 16, 0.35, 0, 0);";

        // tasks remaining box
        VBox remainingBox = new VBox(10);
        remainingBox.setPadding(new Insets(15));
        remainingBox.setAlignment(Pos.TOP_CENTER);
        remainingBox.setStyle(taskCardStyle);
        remainingBox.setPrefWidth(350);

        Text remainingTitle = new Text("Tasks Remaining:");
        remainingTitle.getStyleClass().add("glow-text");
        remainingTitle.setStyle("-fx-font-size: 19px;");

        ListView<String> remainingList = new ListView<>();

        remainingList.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-control-inner-background: transparent;" +
                "-fx-padding: 4;"
        );

        remainingList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }

                setStyle(
                        "-fx-text-fill: #ffeecf;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: transparent;"
                );
            }
        });

        ObservableList<String> remainItems =
                FXCollections.observableArrayList(Database.loadActiveTaskItemsForList(listId));
        remainingList.setPrefHeight(240);
        remainingList.setPrefHeight(240);
        remainingList.setItems(remainItems);

        remainingBox.getChildren().addAll(remainingTitle, remainingList);

        // tasks completed
        VBox completedBox = new VBox(10);
        completedBox.setPadding(new Insets(15));
        completedBox.setAlignment(Pos.TOP_CENTER);
        completedBox.setStyle(taskCardStyle);
        completedBox.setPrefWidth(350);

        Text completedTitle = new Text("Tasks Completed:");
        completedTitle.getStyleClass().add("glow-text");
        completedTitle.setStyle("-fx-font-size: 19px;");

        ListView<String> completedList = new ListView<>();

        completedList.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-control-inner-background: transparent;" +
                "-fx-padding: 4;"
        );

        completedList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
                setStyle(
                        "-fx-text-fill: #ffeecf;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: transparent;"
                );
            }
        });

        ObservableList<String> compItems =
                FXCollections.observableArrayList(Database.loadCompletedTaskItemsForList(listId));
        completedList.setPrefHeight(240);
        completedList.setPrefHeight(240);
        completedList.setItems(compItems);

        completedBox.getChildren().addAll(completedTitle, completedList);

        // left column
        VBox leftColumn = new VBox(30, remainingBox, completedBox);
        leftColumn.setAlignment(Pos.TOP_LEFT);
        leftColumn.setPadding(new Insets(20));
        leftColumn.setPrefWidth(370);

        // constellation preview
        ConstellationManager cm = ConstellationManager.getInstance();
        cm.loadAllConstellationsFromDatabase();

        Pane constView = cm.getCurrentConstellation().createProgressView(450, 450);
        constView.setStyle("-fx-background-color: transparent;");

        StackPane constWrapper = new StackPane(constView);
        constWrapper.setAlignment(Pos.CENTER);
        constWrapper.setPadding(new Insets(20));

        // main (wraps the constellation below the lists when the window is narrow)
        FlowPane mainRow = new FlowPane(60, 20, leftColumn, constWrapper);
        mainRow.setAlignment(Pos.TOP_CENTER);
        mainRow.setPadding(new Insets(10, 20, 40, 0));

        // final layout
        VBox content = new VBox(20, header, mainRow);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30));

        // scroll instead of overflowing, which pushed the sidebar off-screen in small windows
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("styled-scrollpane");

        return scroll;
    }
}
