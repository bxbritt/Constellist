package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    public static Connection connect() {
        Connection conn = null;
        try {
            // This will create users.db in your project folder if it doesn't exist
            String url = "jdbc:sqlite:users.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }
    
    //keeps username and password and email information 
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "username TEXT NOT NULL," +
                     "email TEXT NOT NULL," +
                     "password TEXT NOT NULL" +
                     ");";

        try (Connection conn = connect();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
    
    //keeps the task table information 
    public static void createTasksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "description TEXT NOT NULL," +
                     "is_done INTEGER NOT NULL DEFAULT 0," +
                     "FOREIGN KEY (user_id) REFERENCES users(id)" +
                     ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tasks table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Tasks table creation failed: " + e.getMessage());
        }
    }


    public static boolean validateLogin(String input, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, input); // username
            stmt.setString(2, input); // email
            stmt.setString(3, password); // password

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if match found

        } catch (SQLException e) {
            System.out.println("Login check failed: " + e.getMessage());
            return false;
        }
    }
    
    
    
}