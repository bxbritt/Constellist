package application;
import javafx.scene.shape.Line;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;
//import javafx.animation.FadeTransition;

public class StarLink extends Line { //extends the line object from javafx
	private Star star1;
	private Star star2;
	private Line glowLine;
    private Line coreLine;
	
	public StarLink(Star s1, Star s2) { //basic starlink constructor
		this.star1 = s1;
		this.star2 = s2;
		
		//basically our link is collapsed at star1 but will get extended later.
        glowLine = new Line(star1.getCenterX(), star1.getCenterY(),
                            star1.getCenterX(), star1.getCenterY());
        glowLine.setStroke(Color.PURPLE);
        glowLine.setStrokeWidth(8);
        glowLine.setOpacity(0.3);

        // Core line (front)
        coreLine = new Line(star1.getCenterX(), star1.getCenterY(),
                            star1.getCenterX(), star1.getCenterY());
        coreLine.setStroke(Color.LIGHTBLUE);
        coreLine.setStrokeWidth(2);
    }
        
	
	public Line getGlowLine() { return glowLine; }
    public Line getCoreLine() { return coreLine; }
	
    public void draw() {
    	if (star1.isComplete() && star2.isComplete()) {
        	Timeline timeline = new Timeline(
        			new KeyFrame(Duration.seconds(1),
        				new KeyValue(glowLine.endXProperty(), star2.getCenterX()),
                     new KeyValue(glowLine.endYProperty(), star2.getCenterY()),
                     new KeyValue(coreLine.endXProperty(), star2.getCenterX()),
                     new KeyValue(coreLine.endYProperty(), star2.getCenterY())
                 )
        );
        timeline.play();
    	}
    }
}
