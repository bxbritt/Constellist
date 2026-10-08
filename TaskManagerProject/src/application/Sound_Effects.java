package application;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.Objects;

public class Sound_Effects {
	
	 public static final AudioClip piano_key = load("/sfx/piano_key.wav");
	 public static final AudioClip chime = load("/sfx/chime.wav");
	 
	private static AudioClip load(String path) {
		URL url = Objects.requireNonNull(
		        Sound_Effects.class.getResource(path),
		        "Missing audio resource: " + path + " (check folder & path)"
		    );
		
		
		// sound effects are optional: return null instead of crashing if audio can't load
		try {
			AudioClip clip = new AudioClip(url.toExternalForm());
			//this is super low to help it not drown out the song
	        clip.setVolume(0.08);
	        return clip;
		} catch (Exception e) {
			System.out.println("Sound effect disabled (" + path + "): " + e.getMessage());
			return null;
		}
    }
	
	//adds randomness to piano pitch
	public static void playPianoKey() {
		if (piano_key == null) return;
        double pitch = 0.95 + (Math.random() * 0.05);
        piano_key.setRate(pitch);
        piano_key.play();
    }
	
	public static void playChime() {
		if (chime == null) return;
        chime.play();
    }
	
}
