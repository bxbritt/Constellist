package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.control.TextInputDialog;
import java.util.List;
import java.util.Optional;

public class TaskApp extends Application {

    // static root reference
    private static BorderPane rootLayoutStatic;

    // main root
    private BorderPane rootLayout;

    // main scrollpane (task page)
    private ScrollPane mainScroll;

    @Override
    public void start(Stage stage) {

        // root setup
        rootLayout = new BorderPane();
        rootLayoutStatic = rootLayout;

        // background layer
        StackPane layeredRoot = new StackPane();
        layeredRoot.setStyle("-fx-background-color: linear-gradient(to bottom, #071229, #0d234f, #280c4c);");

        StarOverlay stars = new StarOverlay(180);

        layeredRoot.getChildren().addAll(stars, rootLayout);

        // load user
        int userId = LoggedInUser.getId();
        boolean returningUser = !Database.loadProgressForUser(userId).isEmpty();

        // welcome screen (first time through after login)
        if (!LoggedInUser.hasSeenWelcome) {
            LoggedInUser.hasSeenWelcome = true;
            // load this user's stars so tasks, gallery and progress all start from saved state
            ConstellationManager.getInstance().loadAllConstellationsFromDatabase();
            new WelcomeScreen().show(stage, returningUser);
            return;
        }

        // sidebar
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().addAll("sidebar", "sidebar-border");

        Label welcomeLabel = new Label("Hello, " + LoggedInUser.getUsername());
        welcomeLabel.getStyleClass().addAll("welcome-label", "glow-text");
        sidebar.getChildren().add(welcomeLabel);

        Button mainButton = new Button(" My Tasks");
        Button galleryButton = new Button("Gallery");
        Button progressButton = new Button("Progress");
        Button logoutButton = new Button("Logout");

        mainButton.getStyleClass().add("sidebar-button");
        galleryButton.getStyleClass().add("sidebar-button");
        progressButton.getStyleClass().add("sidebar-button");
        logoutButton.getStyleClass().add("sidebar-button");

        sidebar.getChildren().addAll(mainButton, galleryButton, progressButton, logoutButton);
        rootLayout.setLeft(sidebar);

        // top controls
        Button createListButton = new Button("Create New List");
        createListButton.getStyleClass().addAll("bubble-button", "glow-text");

        AffirmationBubble affirmationBubble = new AffirmationBubble();

        // task list container
        FlowPane listContainer = new FlowPane();
        listContainer.setHgap(20);
        listContainer.setVgap(20);
        listContainer.setPrefWrapLength(900);
        listContainer.getStyleClass().add("list-container");

        // main content layout
        VBox contentLayout = new VBox(20, createListButton, affirmationBubble, listContainer);
        contentLayout.getStyleClass().add("content-root");

        mainScroll = new ScrollPane(contentLayout);
        mainScroll.setFitToWidth(true);
        mainScroll.setStyle("-fx-background-color: transparent;");
        mainScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // load saved lists
        List<SaveProgress> savedLists = Database.loadProgressForUser(userId);
        for (SaveProgress p : savedLists) {
            TaskList list = new TaskList(p.getDescription(), p.getId());
            for (String item : Database.loadActiveTaskItemsForList(p.getId())) {
                list.addItem(item);
            }
            listContainer.getChildren().add(list);
        }

        // create new list
        createListButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("New List");
            dialog.initOwner(stage);
            dialog.setHeaderText("Enter a name for your list:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String name = result.get().trim();
                if (name.isEmpty()) name = "Untitled List";

                SaveProgress sp = new SaveProgress(userId, name, false);
                Database.saveProgress(sp);

                TaskList newList = new TaskList(name, sp.getId());

                FadeTransition ft = new FadeTransition(Duration.millis(500), newList);
                ft.setFromValue(0);
                ft.setToValue(1);

                ScaleTransition st = new ScaleTransition(Duration.millis(500), newList);
                st.setFromX(0.8);
                st.setFromY(0.8);
                st.setToX(1);
                st.setToY(1);

                ft.play();
                st.play();

                listContainer.getChildren().add(newList);
            }
        });

        // sidebar nav
        mainButton.setOnAction(e -> setCenterContent(mainScroll));

        galleryButton.setOnAction(e -> {
            ConstellationGallery gallery = new ConstellationGallery();
            setCenterContent(gallery.getView());
        });

        progressButton.setOnAction(e -> {
            int listId = LoggedInUser.getLastViewedListId();
            if (listId == -1) {
                List<SaveProgress> lists = Database.loadProgressForUser(userId);
                if (!lists.isEmpty()) listId = lists.get(0).getId();
            }
            ProgressScene ps = new ProgressScene(listId);
            setCenterContent(ps.getView());
        });

        logoutButton.setOnAction(e -> {
            LoggedInUser.logout();
            try { new Main().start(stage); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        // default view = tasks page
        rootLayout.setCenter(mainScroll);

        // music
        music.play("/music/menu_music.mp3", 0.25);

        stage.setTitle("Constellist");
        ScreenManager.show(stage, layeredRoot);
    }

    private void setCenterContent(Node content) {

        Node old = rootLayout.getCenter();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), old);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(ev -> {
            rootLayout.setCenter(content);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), content);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
    }

    // static version used by other screens
    public static void setCenterContentStatic(Node content) {
        if (rootLayoutStatic != null) {
            rootLayoutStatic.setCenter(content);
        }
    }

    public static void main(String[] args) { launch(); }
}
