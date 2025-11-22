package application;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

//THIS IS HOW THE FORMAT FOR EACH COSNTELLATION SHOULD BE WRITTEN, IT EXTENDS THE CONSTELLATION BASE CLASS	

public class C1_Heart extends Constellation {
    
    @Override
    protected void createConstellation() {
        // Create the heart constellation with 10 stars
        Star star1 = new Star(500, 700, 6);
        Star star2 = new Star(350, 550, 6);
        Star star3 = new Star(300, 400, 6);
        Star star4 = new Star(350, 300, 6);
        Star star5 = new Star(425, 325, 6);
        Star star6 = new Star(500, 400, 6);
        Star star7 = new Star(575, 325, 6);
        Star star8 = new Star(650, 300, 6);
        Star star9 = new Star(700, 400, 6);
        Star star10 = new Star(650, 550, 6);
        
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
        return "Heart";
    }
    
    @Override
    public int getConstellationNumber() {
        return 1;
    }
    
    @Override
    public Pane createPreview(double width, double height) {
        // Create stars for preview (all complete)
        Star star1 = new Star(500, 700, 6); star1.setComplete(true);
        Star star2 = new Star(350, 550, 6); star2.setComplete(true);
        Star star3 = new Star(300, 400, 6); star3.setComplete(true);
        Star star4 = new Star(350, 300, 6); star4.setComplete(true);
        Star star5 = new Star(425, 325, 6); star5.setComplete(true);
        Star star6 = new Star(500, 400, 6); star6.setComplete(true);
        Star star7 = new Star(575, 325, 6); star7.setComplete(true);
        Star star8 = new Star(650, 300, 6); star8.setComplete(true);
        Star star9 = new Star(700, 400, 6); star9.setComplete(true);
        Star star10 = new Star(650, 550, 6); star10.setComplete(true);
        
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

}