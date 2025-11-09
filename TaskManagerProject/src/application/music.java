package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class music {

    private static MediaPlayer player;

    public static void play(String filePath, double volume) {
        if (player != null) {
            player.stop();
        }

        Media media = new Media(music.class.getResource(filePath).toExternalForm());
        player = new MediaPlayer(media);
        player.setVolume(volume);  // between 0 and 1
        player.setCycleCount(MediaPlayer.INDEFINITE); // makes the music loop
        player.play();
    }

    public static void stop() {
        if (player != null) {
            player.stop();
        }
    }
}
