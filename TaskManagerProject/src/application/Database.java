package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import application.PasswordUtils;
//import application.SaveProgress;

public class Database {


    public static Connection connect() {
        try {
            String url = "jdbc:sqlite:users.db"; // creates users.db if not exists
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            // every caller dereferences the connection, so fail loudly instead of returning null
            throw new IllegalStateException("Could not open users.db (is sqlite-jdbc on the classpath?)", e);
        }
    }

    // users
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "username TEXT NOT NULL," +
                     "email TEXT NOT NULL," +
                     "password TEXT NOT NULL" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

 // Validate login (hashed passwords only)
    public static boolean validateLogin(String usernameOrEmail, String password) {
        String sql = "SELECT password FROM users WHERE username = ? OR email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("password");
                return stored.equals(PasswordUtils.hashPassword(password));
            }
        } catch (SQLException e) {
            System.out.println("Login validation failed: " + e.getMessage());
        }
        return false;
    }

    // Get user ID
    public static int getUserId(String usernameOrEmail) {
        String sql = "SELECT id FROM users WHERE username = ? OR email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Failed to get user ID: " + e.getMessage());
        }
        return -1;
    }

    // task lists
    public static void createTasksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "description TEXT NOT NULL," +
                     "completed INTEGER NOT NULL DEFAULT 0," +
                     "FOREIGN KEY(user_id) REFERENCES users(id)" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tasks table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Failed to create tasks table: " + e.getMessage());
        }
    }

    // Save a new list
    public static void saveProgress(SaveProgress progress) {
        String sql = "INSERT INTO tasks(user_id, description, completed) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, progress.getUserId());
            pstmt.setString(2, progress.getDescription());
            pstmt.setInt(3, progress.isCompleted() ? 1 : 0);

            int rows = pstmt.executeUpdate();
            System.out.println("Rows inserted into tasks: " + rows);

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                progress.setId(rs.getInt(1));
                System.out.println("New list ID: " + progress.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Load lists for a user
    public static List<SaveProgress> loadProgressForUser(int userId) {
        List<SaveProgress> progressList = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SaveProgress progress = new SaveProgress(
                    rs.getInt("id"),
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

    // Delete entire list + items
    public static void deleteTaskList(int listId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to delete list: " + e.getMessage());
        }

        String sqlItems = "DELETE FROM task_items WHERE list_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlItems)) {
            pstmt.setInt(1, listId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to delete task items: " + e.getMessage());
        }
    }
    
 // Delete individual task item
    public static void deleteTaskItem(int listId, String content) {
        String sql = "DELETE FROM task_items WHERE list_id = ? AND content = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            pstmt.setString(2, content.trim());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to delete task: " + e.getMessage());
        }
    }
    
    // task items
    public static void createTaskItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS task_items (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "list_id INTEGER NOT NULL," +
                     "content TEXT NOT NULL," +
                     "completed INTEGER NOT NULL DEFAULT 0," +
                     "FOREIGN KEY(list_id) REFERENCES tasks(id)" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create task_items table: " + e.getMessage());
        }
    }

    // Schema fix
    public static void ensureCompletedColumnExists() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("PRAGMA table_info(task_items);");
            boolean hasCompleted = false;

            while (rs.next()) {
                if ("completed".equalsIgnoreCase(rs.getString("name"))) {
                    hasCompleted = true;
                    break;
                }
            }

            if (!hasCompleted) {
                stmt.execute("ALTER TABLE task_items ADD COLUMN completed INTEGER NOT NULL DEFAULT 0;");
            }

        } catch (SQLException e) {
            System.out.println("Error ensuring completed column: " + e.getMessage());
        }
    }
    
    public static void saveTaskItem(int listId, String content) {
        String sql = "INSERT INTO task_items (list_id, content, completed) VALUES (?, ?, 0)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            pstmt.setString(2, content.trim());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void markTaskItemCompleted(int listId, String content) {
        String sql = "UPDATE task_items SET completed = 1 WHERE list_id = ? AND content = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            pstmt.setString(2, content.trim());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to mark task item completed: " + e.getMessage());
        }
    }

    public static List<String> loadActiveTaskItemsForList(int listId) {
        List<String> items = new ArrayList<>();
        String sql = "SELECT content FROM task_items WHERE list_id = ? AND completed = 0";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) items.add(rs.getString("content"));
        } catch (SQLException e) {
            System.out.println("Failed to load active task items: " + e.getMessage());
        }
        return items;
    }

    public static List<String> loadCompletedTaskItemsForList(int listId) {
        List<String> items = new ArrayList<>();
        String sql = "SELECT content FROM task_items WHERE list_id = ? AND completed = 1";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) items.add(rs.getString("content"));
        } catch (SQLException e) {
            System.out.println("Failed to load completed task items: " + e.getMessage());
        }
        return items;
    }
    
    // Get the newest (most recently created) list ID for a user
    public static int getLastListIdForUser(int userId) {
        String sql = "SELECT id FROM tasks WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Failed to get last list ID: " + e.getMessage());
        }

        return -1;
    }


    // password resets
    public static void createPasswordResetsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS password_resets (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "token TEXT NOT NULL," +
                     "expires_at DATETIME NOT NULL," +
                     "FOREIGN KEY(user_id) REFERENCES users(id)" +
                     ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Password resets table creation failed: " + e.getMessage());
        }
    }

    public static void saveResetToken(String email, String token) {
        String sqlUser = "SELECT id FROM users WHERE email = ?";
        String sqlInsert = "INSERT INTO password_resets(user_id, token, expires_at) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement findUser = conn.prepareStatement(sqlUser)) {

            findUser.setString(1, email);
            ResultSet rs = findUser.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                try (PreparedStatement insert = conn.prepareStatement(sqlInsert)) {
                    insert.setInt(1, userId);
                    insert.setString(2, token);
                    insert.setString(3, java.time.LocalDateTime.now().plusHours(1).toString());
                    insert.executeUpdate();
                }
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // addition from caitlyn
    // this will fetch username from DB
    public static String getUsernameById(int id) {
    	String sql = "SELECT username FROM users WHERE id = ?";
    	try (Connection conn = connect();
    		PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		
    		pstmt.setInt(1, id);
    		ResultSet rs = pstmt.executeQuery();
    		
    		if (rs.next()) return rs.getString("username");
    	
    	} catch(SQLException e) {
    		System.out.println("Failed to fetch username: " + e.getMessage());
    	}
    	return null;
    }
    // end of addition
    
    // constellation progress
    public static void createConstellationProgressTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS constellation_progress (" +
                         "user_id INTEGER, " +
                         "constellation_index INTEGER, " +
                         "stars_lit INTEGER, " +
                         "total_tasks_completed INTEGER, " +
                         "PRIMARY KEY (user_id, constellation_index), " +
                         "FOREIGN KEY (user_id) REFERENCES users(id))";

            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConstellationProgress(int userId, int constellationIndex, int starsLit, int totalTasksCompleted) {
        try {
            String updateSql =
                "UPDATE constellation_progress SET stars_lit = ?, total_tasks_completed = ? " +
                "WHERE user_id = ? AND constellation_index = ?";

            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, starsLit);
                pstmt.setInt(2, totalTasksCompleted);
                pstmt.setInt(3, userId);
                pstmt.setInt(4, constellationIndex);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated == 0) {
                    String insertSql =
                        "INSERT INTO constellation_progress (user_id, constellation_index, stars_lit, total_tasks_completed) " +
                        "VALUES (?, ?, ?, ?)";

                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, constellationIndex);
                        insertStmt.setInt(3, starsLit);
                        insertStmt.setInt(4, totalTasksCompleted);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    //counter for constellations progress 
    
    public static void incrementConstellationProgress(int userId, int constellationIndex) {
        String sql = "UPDATE constellation_progress " +
                     "SET stars_lit = stars_lit + 1, total_tasks_completed = total_tasks_completed + 1 " +
                     "WHERE user_id = ? AND constellation_index = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, constellationIndex);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to increment constellation progress: " + e.getMessage());
        }
    }
    
 // Get stars lit for a specific constellation
    public static int getStarsLit(int userId, int constellationIndex) {
        String sql = "SELECT stars_lit FROM constellation_progress WHERE user_id = ? AND constellation_index = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, constellationIndex);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("stars_lit");
        } catch (SQLException e) {
            System.out.println("Failed to read stars_lit: " + e.getMessage());
        }
        return 0;
    }

 // Ensure a row exists for this constellation
    public static void ensureConstellationProgressRow(int userId, int constellationIndex) {
        String selectSql = "SELECT 1 FROM constellation_progress WHERE user_id = ? AND constellation_index = ?";
        String insertSql = "INSERT INTO constellation_progress (user_id, constellation_index, stars_lit, total_tasks_completed) VALUES (?, ?, 0, 0)";
        try (Connection conn = connect();
             PreparedStatement sel = conn.prepareStatement(selectSql)) {
            sel.setInt(1, userId);
            sel.setInt(2, constellationIndex);
            ResultSet rs = sel.executeQuery();

            if (!rs.next()) {
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, userId);
                    ins.setInt(2, constellationIndex);
                    ins.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to ensure progress row: " + e.getMessage());
        }
    }

    // Get total tasks completed across all constellations
    public static int getTotalTasksCompleted(int userId) {
        String sql = "SELECT COALESCE(SUM(total_tasks_completed),0) AS total FROM constellation_progress WHERE user_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("Failed to read total_tasks_completed: " + e.getMessage());
        }
        return 0;
    }
}