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


public class C2_Capricorn extends Pane {

    private Star[] stars;
    private StarLink[] links;

    public C2_Capricorn() {
        createConstellation();
    }

    private void createConstellation() {
        // Create stars
        Star star1 = new Star(85, 300, 6);
        star1.setComplete(true);
        
        Star star2 = new Star(135, 320, 6);
        star2.setComplete(true);
        
        Star star3 = new Star(240, 335, 6);
        star3.setComplete(true);
        
        Star star4 = new Star(350, 360, 6);
        star4.setComplete(true);
        
        Star star5 = new Star(700, 275, 6);
        star5.setComplete(true);
        
        Star star6 = new Star(775, 260, 6);
        star6.setComplete(true);
        
        Star star7 = new Star(735, 320, 6);
        star7.setComplete(false);
        
        Star star8 = new Star(550, 700, 6);
        star8.setComplete(false);
        
        Star star9 = new Star(500, 725, 6);
        star9.setComplete(false);
        
        Star star10 = new Star(175, 525, 6);
        star10.setComplete(false);

        stars = new Star[] { star1, star2, star3, star4, star5, star6, star7, star8, star9, star10 };

        links = new StarLink[] {
            new StarLink(star1, star2), new StarLink(star2, star3), new StarLink(star3, star4),
            new StarLink(star4, star5), new StarLink(star5, star6), new StarLink(star6, star7),
            new StarLink(star7, star8), new StarLink(star8, star9), new StarLink(star9, star10),
            new StarLink(star1, star10)
        };

        for (StarLink link : links) {
            getChildren().addAll(link.getGlowLine(), link.getCoreLine());
        }
        getChildren().addAll(stars);

      //  setStyle("-fx-background-color: black;");
        for (StarLink link : links) link.draw();
    }

}

//NEED TO IMPLEMENT A CLASS THAT CHOOSES CONSTELLATION CLASS BASED ON SIZE, INCLUDES A METHOD TO LIGHT UP STARS WHEN USERS COMPLETE TASKS,
    //WHEN THE SIZE IS CHOOSEN WE CAN CALL THE createConstellation() METHOD

