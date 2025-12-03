package application;

public class LoggedInUser {

    private static int userId;
    
    // addition from caitlyn
    // this will have safe username storage so it can be automatically retreieved
    private static String username;
    // end of addition

    public static void setId(int id) {
        userId = id;
        System.out.println("Loading progress for user ID: " + LoggedInUser.getId());
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
    
    public static boolean hasSeenWelcome = false;	// ensures the screen only shows once per login
    // end of addition
}