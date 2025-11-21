package application;

import java.util.ArrayList;
import java.util.List;

public class ConstellationManager {
    
    private static ConstellationManager instance;

    // List of constellations in order (levels)
    private List<Constellation> constellations;

    // Current constellation
    private Constellation currentConstellation;

    // Current constellation index
    private int currentConstellationIndex;

    // Total tasks completed
    private int totalTasksCompleted;

    // Private constructor to enforce singleton,  only one instance 
    private ConstellationManager() {
        initializeConstellations();
        currentConstellationIndex = 0;
        totalTasksCompleted = 0;
        
        // Set initial constellation
        if (!constellations.isEmpty()) {
            currentConstellation = constellations.get(0);
        }
    }

    // access method,
    public static synchronized ConstellationManager getInstance() {
        if (instance == null) {
            instance = new ConstellationManager();
        }
        return instance;
    }

    // Initialize constellation sequence
    private void initializeConstellations() {
        constellations = new ArrayList<>();
        constellations.add(new C1_Heart());
        
        // Add more constellations in order
        // constellations.add(new C2_Capricorn());
        // constellations.add(new C3());
        // etc
    }

    /**
     Complete a task and update constellation progress
      @return true if a star was lit, false if no star could be lit
     */
    public boolean completeTask() {
        // Light up the next star in the current constellation
        boolean starLit = currentConstellation.lightNextStar();
        
        // Increment total tasks completed
        totalTasksCompleted++;

        // Check if constellation is complete
        if (currentConstellation.isComplete()) {
            // Attempt to move to next constellation
            return advanceToNextConstellation();
        }

        // Save progress to database
        saveProgressToDatabase();

        return starLit;
    }

    /**
     * Move to the next constellation if available
     * @return true if next constellation is available, false otherwise
     */
    private boolean advanceToNextConstellation() {
        // Check if there are more constellations
        if (currentConstellationIndex + 1 < constellations.size()) {
            currentConstellationIndex++;
            currentConstellation = constellations.get(currentConstellationIndex);
            
            // Save progress to database
            saveProgressToDatabase();
            
            return true;
        }
        
        return false;
    }
    
    
    /**
      Save constellation progress to database
     */
    private void saveProgressToDatabase() {
        try {
           
            Database.saveConstellationProgress(
                LoggedInUser.getId(),  // Current user's ID
                currentConstellationIndex,  // Current constellation index
                currentConstellation.getStarsLit(),  // Stars lit in current constellation
                totalTasksCompleted  
            );
        } catch (Exception e) {
            System.err.println("Failed to save constellation progress: " + e.getMessage());
        }
    }

    /**
     * Load constellation progress from database
     */
    public void loadProgressFromDatabase() {
        try {
            // Assuming a method in Database class to load constellation progress
            Object[] progress = Database.loadConstellationProgress(LoggedInUser.getId());
            
            if (progress != null) {
                currentConstellationIndex = (int) progress[0];
                int starsLit = (int) progress[1];
                totalTasksCompleted = (int) progress[2];

                // Set current constellation
                currentConstellation = constellations.get(currentConstellationIndex);
                
                // Restore stars lit
                currentConstellation.setStarsLit(starsLit);
            }
        } catch (Exception e) {
            System.err.println("Failed to load constellation progress: " + e.getMessage());
        }
    }

    // Getters for current state
    /**
     * Get the current constellation
     * @return Current active Constellation
     */
    public Constellation getCurrentConstellation() {
        return currentConstellation;
    }
    
    public Constellation getConstellationByIndex(int index) {
		if (index >= 0 && index < constellations.size()) {
			return constellations.get(index);
		}
		return null;
	}

    /**
     * Get total tasks completed across all constellations
     * @return Total completed tasks
     */
    public int getTotalTasksCompleted() {
        return totalTasksCompleted;
    }

    /**
     * Get tasks completed in current constellation
     * @return Number of tasks completed in current constellation
     */
    public int getCurrentConstellationTasksCompleted() {
        return currentConstellation.getStarsLit();
    }

    /**
     * Get current constellation index
     * @return Current constellation number (0-indexed)
     */
    public int getCurrentConstellationIndex() {
        return currentConstellationIndex;
    }
    


    /**
     * Check if all constellations are completed
     * @return true if all constellations are done, false otherwise
     */
    public boolean areAllConstellationsCompleted() {
        return currentConstellationIndex == constellations.size() - 1 
               && currentConstellation.isComplete();
    }

    /**
     * Get list of completed constellations
     * @return List of constellations completed so far
     */
    public List<Constellation> getCompletedConstellations() {
        return constellations.subList(0, currentConstellationIndex + 1);
    }

    /**
     * Reset constellation progress (for testing or user request)
     */
    public void resetProgress() {
        currentConstellationIndex = 0;
        totalTasksCompleted = 0;
        
        if (!constellations.isEmpty()) {
            currentConstellation = constellations.get(0);
            currentConstellation.setStarsLit(0);
        }
        
 
        saveProgressToDatabase();
    }
}

