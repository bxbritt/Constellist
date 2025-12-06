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
        constellations.add(new C2_Capricorn());
        constellations.add(new C3_Smile());
        constellations.add(new C4_Lightning());
        constellations.add(new C5_Dragonfly());
        constellations.add(new C6_Star());
        constellations.add(new C7_Swan());
        constellations.add(new C8_Dinosaur());
        constellations.add(new C9_Dragon());
        constellations.add(new C10_Jellyfish());
        // Add more constellations in order
        // constellations.add(new C2_Capricorn());
        // constellations.add(new C3());
        // etc
    }

    
    public boolean advanceToNextConstellation() {
        if (currentConstellationIndex + 1 < constellations.size()) {
            currentConstellationIndex++;
            currentConstellation = constellations.get(currentConstellationIndex);
            Database.ensureConstellationProgressRow(LoggedInUser.getId(), currentConstellationIndex);
            return true;
        }
        return false;
    }

 

    public Constellation getCurrentConstellation() {
        return currentConstellation;
    }
    
    public Constellation getConstellationByIndex(int index) {
		if (index >= 0 && index < constellations.size()) {
			return constellations.get(index);
		}
		return null;
	}


    
    
    public void saveProgressToDatabase() {
        int userId = LoggedInUser.getId();
        for (int i = 0; i < constellations.size(); i++) {
            Constellation c = constellations.get(i);
            Database.saveConstellationProgress(userId, i, c.getStarsLit(), totalTasksCompleted);
        }
    }

    
    public void loadAllConstellationsFromDatabase() {
    	initializeConstellations();
        int userId = LoggedInUser.getId();
        for (int i = 0; i < constellations.size(); i++) {
            Database.ensureConstellationProgressRow(userId, i);
            int starsLit = Database.getStarsLit(userId, i);
            constellations.get(i).setStarsLit(starsLit);
        }
        totalTasksCompleted = Database.getTotalTasksCompleted(userId);

        // Set current constellation to first incomplete
        currentConstellationIndex = 0;
        for (int i = 0; i < constellations.size(); i++) {
            if (!constellations.get(i).isComplete()) {
                currentConstellationIndex = i;
                break;
            }
        }
        currentConstellation = constellations.get(currentConstellationIndex);
    }
    
    public void completeTask() {
        // Light next star in current constellation
        boolean starLit = currentConstellation.lightNextStar();
        totalTasksCompleted++;

        // Update DB
        Database.incrementConstellationProgress(LoggedInUser.getId(), currentConstellationIndex);

        saveProgressToDatabase();
    }

    
  
}