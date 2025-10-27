package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // Connect to SQLite database
    public static Connection connect() {
        try {
            // This will create users.db in your project folder if it doesn't exist
            String url = "jdbc:sqlite:users.db";
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite.");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    // Create users table if it doesn't exist
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "username TEXT NOT NULL," +
                     "email TEXT NOT NULL," +
                     "password TEXT NOT NULL" +
                     ");";

        Connection conn = connect();
        if (conn == null) {
            System.out.println("Database connection is null — aborting table creation.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
    
    //to check if login is correct 
    public static boolean validateLogin(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            pstmt.setString(3, password);

            java.sql.ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true if a match is found

        } catch (SQLException e) {
            System.out.println("Login validation failed: " + e.getMessage());
            return false;
        }
    }
    
    //table for save function 
    public static void createTasksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "description TEXT NOT NULL," +
                     "completed INTEGER NOT NULL DEFAULT 0," +
                     "FOREIGN KEY(user_id) REFERENCES users(id)" +
                     ");";

        try (Connection conn = connect();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tasks table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Task table creation failed: " + e.getMessage());
        }
    }
    
    public static void saveProgress(SaveProgress progress) {
        String sql = "INSERT INTO tasks(user_id, description, completed) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, progress.getUserId());
            pstmt.setString(2, progress.getDescription());
            pstmt.setInt(3, progress.isCompleted() ? 1 : 0);
            pstmt.executeUpdate();

            java.sql.ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                progress.setId(generatedId); // ✅ Store the ID in the object
            }

            System.out.println("Saving progress: " + progress.getDescription() + " for user " + progress.getUserId());

        } catch (SQLException e) {
            System.out.println("Failed to save progress: " + e.getMessage());
        }
    }
    

    public static List<SaveProgress> loadProgressForUser(int userId) {
        List<SaveProgress> progressList = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SaveProgress progress = new SaveProgress(
                    rs.getInt("user_id"),
                    rs.getString("description"),
                    rs.getInt("completed") == 1
                );
                progressList.add(progress);
            }

        } catch (SQLException e) {
            System.out.println("Failed to load progress: " + e.getMessage());
        }

        return progressList;
    }
    
    public static int getUserId(String usernameOrEmail) {
        String sql = "SELECT id FROM users WHERE username = ? OR email = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);

            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Failed to get user ID: " + e.getMessage());
        }

        return -1; // fallback if not found
    }
    
    //to save the itms inside the task list 
    
    public static void createTaskItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS task_items (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "list_id INTEGER NOT NULL," +
                     "content TEXT NOT NULL," +
                     "FOREIGN KEY(list_id) REFERENCES tasks(id)" +
                     ");";

        try (Connection conn = connect();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Task items table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Failed to create task_items table: " + e.getMessage());
        }
    }
    
    public static void saveTaskItem(int listId, String content) {
        String sql = "INSERT INTO task_items (list_id, content) VALUES (?, ?)";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            System.out.println("Saved task item: " + content);

        } catch (SQLException e) {
            System.out.println("Failed to save task item: " + e.getMessage());
        }
    }
    
    public static List<String> loadTaskItemsForList(int listId) {
        List<String> items = new ArrayList<>();
        String sql = "SELECT content FROM task_items WHERE list_id = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(rs.getString("content"));
            }

        } catch (SQLException e) {
            System.out.println("Failed to load task items: " + e.getMessage());
        }

        return items;
    }
    
    public static void deleteTaskItem(int listId, String content) {
        String sql = "DELETE FROM task_items WHERE list_id = ? AND content = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            pstmt.setString(2, content);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("🗑️ Deleted task item: " + content + " from list " + listId);
            } else {
                System.out.println("⚠️ No matching task item found to delete: " + content);
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete task item: " + e.getMessage());
        }
    }
    
    public static boolean taskListExists(int userId, String title) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND description = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, title);

            java.sql.ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Failed to check task list existence: " + e.getMessage());
            return false;
        }
    }
}