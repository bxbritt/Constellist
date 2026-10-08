package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class music {

    private static MediaPlayer player;

    public static void play(String filePath, double volume) {
        if (player != null) {
            player.stop();
        }

        // audio is optional: missing codecs/devices shouldn't stop the app from running
        try {
            Media media = new Media(music.class.getResource(filePath).toExternalForm());
            player = new MediaPlayer(media);
            player.setVolume(volume);  // between 0 and 1
            player.setCycleCount(MediaPlayer.INDEFINITE); // makes the music loop
            player.play();
        } catch (Exception e) {
            player = null;
            System.out.println("Music disabled: " + e.getMessage());
        }
    }

    public static void stop() {
        if (player != null) {
            player.stop();
        }
    }
}
