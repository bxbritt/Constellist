package application;

public class SaveProgress {
    private int id;
    private int userId;
    private String description;
    private boolean completed;

    //  Constructor for new lists (no id yet)
    public SaveProgress(int userId, String description, boolean completed) {
        this.userId = userId;
        this.description = description;
        this.completed = completed;
    }

    //  Constructor for existing lists loaded from DB (id known)
    public SaveProgress(int id, int userId, String description, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.completed = completed;
    }

    // Getters
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

    // Setter for id (used after saving to DB)
    public void setId(int id) {
        this.id = id;
    }
}