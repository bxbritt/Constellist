package application;

public class LoggedInUser {

    private static int userId;

    public static void setId(int id) {
        userId = id;
        System.out.println("Loading progress for user ID: " + LoggedInUser.getId());
    }

    public static int getId() {
        return userId;
    }
}