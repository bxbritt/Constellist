import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color; //used for coloring shapes
import javafx.scene.shape.Circle; //(x, y, radius, fillColor)
import javafx.util.Duration;


public class Star extends Circle{  //extends the circle object from javafx
    //private Circle shape;
    private boolean complete;

    public Star(double x, double y, double radius) {
    	super(x, y, radius, Color.GREY); //stars will star dim n greyed out
        complete = false;
    
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1), this); //creates a 1 second animation for star
    	pulse.setFromX(1); //scaling factor 1 means default size, no scaling
        pulse.setFromY(1);
        pulse.setToX(1.5); //150% of the original size, or 1.5x bigger
        pulse.setToY(1.5);
        pulse.setAutoReverse(true); //when the animation ends it loops back
        pulse.setCycleCount(ScaleTransition.INDEFINITE); //indefinitely loops
        pulse.play();
    
    
    
    
    
    }

    public boolean isComplete() { //for whether a star has been completed ie task completed
        return complete;
    }
    
    

    public void setComplete(boolean value) {
        complete = value;
        if (value) {
            setFill(Color.WHITE); //lights up when complete
            DropShadow glow = new DropShadow(); //this will be used to add a glow behind the star
            glow.setRadius(20); //radius of the glow
            glow.setColor(Color.PURPLE); //color of the glow
            glow.setSpread(0.6); //intensity or opacity of the glow
            setEffect(glow); //applies the glow to the star
            
        } else {
            setFill(Color.GREY); //stays dim if incomplete
        }
    }
    
}