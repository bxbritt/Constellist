package application;

import java.sql.Connection;
import java.sql.DriverManager;
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
}