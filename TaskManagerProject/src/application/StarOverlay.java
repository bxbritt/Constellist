package application;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarOverlay extends Pane {

	private int starCount;
	private final Random rand = new Random();
	private final List<Circle> stars = new ArrayList<>();
	
	public StarOverlay(int starCount) {
		this.starCount = starCount;
		
		setMouseTransparent(true);
		setStyle("-fx-background-color: transparent;");
		
		// create initial stars
		generateStars();
		
		// re-generate stars whenever window size changes
		ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> regenerateStars();
		widthProperty().addListener(resizeListener);
		heightProperty().addListener(resizeListener);
	}
	
	private void generateStars() {
		stars.clear();
		getChildren().clear();
		
		for (int i = 0; i < starCount; i++) {
			Circle star = new Circle(
					1 + rand.nextDouble() * 1.3,
					Color.rgb(255,  215,  110, 0.85)
			);
			
			// random placement with no stretching
			star.setTranslateX(rand.nextDouble() * getWidth());
			star.setTranslateY(rand.nextDouble() * getHeight());
			
			animateStar(star);
			stars.add(star);
		}
		
		getChildren().addAll(stars);
	}
	
	private void regenerateStars() {
		if (getWidth() > 0 && getHeight() > 0) {
			generateStars();
		}
	}
	
	private void animateStar(Circle star) {
		FadeTransition fade = new FadeTransition(
			Duration.seconds(2 + rand.nextDouble() * 2),
			star
		);
		fade.setFromValue(0.25);
		fade.setToValue(0.65);
		fade.setCycleCount(FadeTransition.INDEFINITE);
		fade.setAutoReverse(true);
		fade.play();
	}
}