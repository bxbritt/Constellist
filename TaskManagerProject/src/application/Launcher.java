package application;

// Entry point for IDE "Run" buttons and plain `java` launches.
// Java refuses to start a class that extends Application unless JavaFX is on the module path,
// so this class (which does not extend Application) hands off to Main instead.
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
