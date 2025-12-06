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


public class C6_Star extends Constellation {
	
	@Override
    protected void createConstellation() {
		
		
		//MAKE CONSTELLATION HERE ********************//
		Star star1 = new Star(250, 400, 6);
		star1.setComplete(true);
		
		Star star2 = new Star(425, 375, 6);
		star2.setComplete(true);
		
		Star star3 = new Star(500, 200, 6);
		star3.setComplete(true);
		
		Star star4 = new Star(575, 375, 6);
		star4.setComplete(true);

		Star star5 = new Star(750, 400, 6);
		star5.setComplete(true);
		
		Star star6 = new Star(600, 500, 6);
		star6.setComplete(true);
		
		Star star7 = new Star(675, 675, 6);
		star7.setComplete(true);
		
		Star star8 = new Star(500, 550, 6);
		star8.setComplete(true);
		
		Star star9 = new Star(325, 675, 6);
		star9.setComplete(true);
		
		Star star10 = new Star(400, 500, 6);
		star10.setComplete(true);
		
		// All stars start incomplete - they'll be lit by ConstellationManager
        for (Star star : new Star[]{star1, star2, star3, star4, star5, 
                                     star6, star7, star8, star9, star10}) {
            star.setComplete(false);
        }
        
        stars = new Star[] { star1, star2, star3, star4, star5, 
                            star6, star7, star8, star9, star10 };
        
        // Create links between stars
        links = new StarLink[] {
            new StarLink(star1, star2), new StarLink(star2, star3), 
            new StarLink(star3, star4), new StarLink(star4, star5), 
            new StarLink(star5, star6), new StarLink(star6, star7),
            new StarLink(star7, star8), new StarLink(star8, star9), 
            new StarLink(star9, star10), new StarLink(star1, star10)
        };
        
        // Add links to the pane (glow layer first, then core)
        for (StarLink link : links) {
            getChildren().addAll(link.getGlowLine(), link.getCoreLine());
        }
        
        // Add stars on top of links
        getChildren().addAll(stars);
        
        // Draw all links
        for (StarLink link : links) {
            link.draw();
        }

    }
    
    @Override
    public String getName() {
        return "Star";
    }
    

    
    @Override
    public Pane createPreview(double width, double height) {
        // Create stars for preview (all complete)
        Star star1 = new Star(250, 400, 6); star1.setComplete(true);
        Star star2 = new Star(425, 375, 6); star2.setComplete(true);
        Star star3 = new Star(500, 200, 6); star3.setComplete(true);
        Star star4 = new Star(575, 375, 6); star4.setComplete(true);
        Star star5 = new Star(750, 400, 6); star5.setComplete(true);
        Star star6 = new Star(600, 500, 6); star6.setComplete(true);
        Star star7 = new Star(675, 675, 6); star7.setComplete(true);
        Star star8 = new Star(500, 550, 6); star8.setComplete(true);
        Star star9 = new Star(325, 675, 6); star9.setComplete(true);
        Star star10 = new Star(400, 500, 6); star10.setComplete(true);
        
        Star[] previewStars = {star1, star2, star3, star4, star5, 
                               star6, star7, star8, star9, star10};
        
        StarLink[] previewLinks = {
            new StarLink(star1, star2), new StarLink(star2, star3),
            new StarLink(star3, star4), new StarLink(star4, star5),
            new StarLink(star5, star6), new StarLink(star6, star7),
            new StarLink(star7, star8), new StarLink(star8, star9),
            new StarLink(star9, star10), new StarLink(star1, star10)
        };
        
        return createScaledPreview(previewStars, previewLinks, width, height, 0.25);
    }
    
    public Pane createShow(double width, double height) {
        // Create stars for show (all complete)
    	  Star star1 = new Star(250, 400, 6); star1.setComplete(true);
          Star star2 = new Star(425, 375, 6); star2.setComplete(true);
          Star star3 = new Star(500, 200, 6); star3.setComplete(true);
          Star star4 = new Star(575, 375, 6); star4.setComplete(true);
          Star star5 = new Star(750, 400, 6); star5.setComplete(true);
          Star star6 = new Star(600, 500, 6); star6.setComplete(true);
          Star star7 = new Star(675, 675, 6); star7.setComplete(true);
          Star star8 = new Star(500, 550, 6); star8.setComplete(true);
          Star star9 = new Star(325, 675, 6); star9.setComplete(true);
          Star star10 = new Star(400, 500, 6); star10.setComplete(true);

        Star[] showStars = {star1, star2, star3, star4, star5,
                            star6, star7, star8, star9, star10};

        StarLink[] showLinks = {
            new StarLink(star1, star2), new StarLink(star2, star3),
            new StarLink(star3, star4), new StarLink(star4, star5),
            new StarLink(star5, star6), new StarLink(star6, star7),
            new StarLink(star7, star8), new StarLink(star8, star9),
            new StarLink(star9, star10), new StarLink(star1, star10)
        };


        return createScaledPreview(showStars, showLinks, width, height, 0.75);
    }

}