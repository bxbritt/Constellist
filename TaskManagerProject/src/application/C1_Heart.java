package application;
import javafx.animation.ScaleTransition; //changes an objects size over time
//import application.StarLink;
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


public class C1_Heart extends Application {
	
	@Override
    public void start(Stage stage) {
		
		
		//MAKE CONSTELLATION HERE ********************//
		Star star1 = new Star(500, 700, 6);
		star1.setComplete(true);
		
		Star star2 = new Star(350, 550, 6);
		star2.setComplete(true);
		
		Star star3 = new Star(300, 400, 6);
		star3.setComplete(true);
		
		Star star4 = new Star(350, 300, 6);
		star4.setComplete(true);

		Star star5 = new Star(425, 325, 6);
		star5.setComplete(true);
		
		Star star6 = new Star(500, 400, 6);
		star6.setComplete(true);
		
		Star star7 = new Star(575, 325, 6);
		star7.setComplete(true);
		
		Star star8 = new Star(650, 300, 6);
		star8.setComplete(true);
		
		Star star9 = new Star(700, 400, 6);
		star9.setComplete(true);
		
		Star star10 = new Star(650, 550, 6);
		star10.setComplete(true);
		
		StarLink[] links = {
	            new StarLink(star1, star2), new StarLink(star2, star3), new StarLink(star3, star4), new StarLink(star4, star5), new StarLink(star5, star6),
	            new StarLink(star6, star7), new StarLink(star7, star8), new StarLink(star8, star9), new StarLink(star9, star10), new StarLink(star1, star10)  };
		
		//*********************************************//
		Pane root = new Pane();
		for (StarLink link : links) {
            root.getChildren().addAll(link.getGlowLine(), link.getCoreLine());
        }
        root.getChildren().addAll(star1, star2, star3, star4, star5, star6, star7, star8, star9, star10);
		
		root.setStyle("-fx-background-color: black;");
		for (StarLink link : links) link.draw();

        Scene scene = new Scene(root, 1000, 1000); //our canvas and we put our container with a star on it, the numbers are the window size

        stage.setTitle("Constellation Demo");
        stage.setScene(scene);
        stage.show();   //so we can actually see lol
		
	}
	
	
	public static void main(String[] args) {
        launch(args);
    }

}