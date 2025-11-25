package application;

import java.util.ArrayList;
import java.util.List;

public class UnlockedGallery {
    private static final List<String> unlockedConstellations = new ArrayList<>();

    public static void addConstellation(String name) {
        unlockedConstellations.add(name);
    }

    public static List<String> getUnlockedConstellations() {
        return unlockedConstellations;
    }
}