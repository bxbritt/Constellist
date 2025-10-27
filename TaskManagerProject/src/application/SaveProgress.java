package application;

public class SaveProgress {
    private int id;
    private int userId;
    private String description;
    private boolean completed;

    public SaveProgress(int userId, String description, boolean completed) {
        this.userId = userId;
        this.description = description;
        this.completed = completed;
    }

    // ✅ Add this constructor if you're loading from database
    public SaveProgress(int id, int userId, String description, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.completed = completed;
    }

    // ✅ Add this getter
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
    
    public void setId(int id) {
    	this.id = id;
    }

    
}