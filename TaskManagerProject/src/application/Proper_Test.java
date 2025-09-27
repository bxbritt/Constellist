import javafx.animation.ScaleTransition; //changes an objects size over time
import javafx.animation.ParallelTransition; //allows for simultaneous animations
import javafx.application.Application; //not really sure what this is yet
import javafx.scene.Scene; //content area of a window
import javafx.scene.layout.Pane; //holds and positions UI elements
import javafx.scene.paint.Color; //used for coloring shapes
import javafx.scene.shape.Circle; //(x, y, radius, fillColor)
import javafx.stage.Stage; //the window, scenes are attached here
import javafx.util.Duration; //length of time for animating
import javafx.scene.effect.DropShadow; //we can use this to add a shadow or glowing effect behind a shape
import javafx.scene.shape.Line; //lines to connect shapes


public class Proper_Test extends Application {
	
	@Override
    public void start(Stage stage) {
		
		
		//MAKE CONSTELLATION HERE ********************//
		Star star1 = new Star(300, 300, 6);
		star1.setComplete(true);
		
		Star star2 = new Star(400, 200, 6);
		star2.setComplete(true);
		
		Star star3 = new Star(500, 300, 6);
		star3.setComplete(true);
		
		Star star4 = new Star(400, 500, 6);
		star4.setComplete(true);

		Star star5 = new Star(400, 300, 6);
		star5.setComplete(true);
		StarLink link1 = new StarLink(star1, star2);
		StarLink link2 = new StarLink(star2, star3);
		StarLink link3 = new StarLink(star3, star4);
		StarLink link4 = new StarLink(star4, star1);
		StarLink link51 = new StarLink(star5, star1);
		StarLink link52 = new StarLink(star5, star2);
		StarLink link53 = new StarLink(star5, star3);
		StarLink link54 = new StarLink(star5, star4);
		
		//*********************************************//
		
		Pane root = new Pane(link1.getGlowLine(), link1.getCoreLine(),link2.getGlowLine(), link2.getCoreLine(), link3.getGlowLine(), link3.getCoreLine(), link4.getGlowLine(), link4.getCoreLine(), 
				link51.getGlowLine(), link51.getCoreLine(), link52.getGlowLine(), link52.getCoreLine(), link53.getGlowLine(), link53.getCoreLine(), link54.getGlowLine(), link54.getCoreLine(), star1, star2, star3, star4, star5);
		root.setStyle("-fx-background-color: black;");
		link1.draw();
		link2.draw();
		link3.draw();
		link4.draw();
		link51.draw();
		link52.draw();
		link53.draw();
		link54.draw();

        Scene scene = new Scene(root, 1000, 1000); //our canvas and we put our container with a star on it, the numbers are the window size

        stage.setTitle("Constellation Demo");
        stage.setScene(scene);
        stage.show();   //so we can actually see lol
		
	}
	
	
	public static void main(String[] args) {
        launch(args);
    }

}
