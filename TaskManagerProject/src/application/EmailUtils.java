package application;

public class EmailUtils {
    public static void sendResetEmail(String email, String token) {
        // For now, just print the link to console
        System.out.println("Password reset link for " + email + ": https://yourapp.com/reset?token=" + token);
    }
}