package application;

public class StarManager {
    private int starCount = 0;
    private int threshold = 5; // ⭐ how many tasks before constellation unlock
    private Runnable onConstellationUnlocked; // callback

    public StarManager(Runnable onConstellationUnlocked) {
        this.onConstellationUnlocked = onConstellationUnlocked;
    }

    public void earnStar() {
        starCount++;
        System.out.println("Task completed! Current count: " + starCount);

        if (starCount >= threshold) {
            System.out.println("✨ Constellation unlocked!");
            if (onConstellationUnlocked != null) {
                onConstellationUnlocked.run();
            }
            // reset or keep counting depending on your design
            starCount = 0;
        }
    }
}