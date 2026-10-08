package application;

public class LoggedInUser {

    private static int userId;
    
    // addition from caitlyn
    // this will have safe username storage so it can be automatically retrieved
    private static String username;
    // end of addition

    // last viewed list id for progress page navigation
    private static int lastViewedListId = -1;

    public static void setId(int id) {
        userId = id;
    }

    public static int getId() {
        return userId;
    }
    
    // addition from caitlyn
    public static void setUsername(String name) {
        username = name;
    }
    
    public static String getUsername() {
        return username;
    }
    
    public static boolean hasSeenWelcome = false;    // ensures the screen only shows once per login
    // end of addition

    // save last viewed list id
    public static void setLastViewedListId(int id) {
        lastViewedListId = id;
    }

    // retrieve last viewed list id
    public static int getLastViewedListId() {
        return lastViewedListId;
    }

    // clear session state so the next user starts fresh
    public static void logout() {
        userId = 0;
        username = null;
        lastViewedListId = -1;
        hasSeenWelcome = false;
    }
}
