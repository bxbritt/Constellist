package application;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;

/**
  Abstract base class for all constellations.
  Each constellation has 10 stars that light up as tasks are completed.
 */

public abstract class Constellation extends Pane {
	    protected Star[] stars;
	    protected StarLink[] links;
	    protected int starsLit = 0; // How many stars are currently lit (0-10)
	    
	    public Constellation() {
	        createConstellation();
	    }
	    
	 
	     //Each constellation subclass must implement this to create shape
	     
	    protected abstract void createConstellation();
	    
	    
	    // Get the name of this constellation ("Heart", "Capricorn")
	     
	    public abstract String getName();
	    
	    
	     // Get the constellation number (1-10 for display purposes)
	     
	    public abstract int getConstellationNumber();
	    
	    /**
	      Light up the next star in sequence
	      @return true if a star was lit, false if all stars already lit
	     */
	    public boolean lightNextStar() {
	        if (starsLit >= 10) {
	            return false; // meaning constellation is already complete
	        }
	        
	        stars[starsLit].setComplete(true);
	        starsLit++;
	        
	        // redraw the links to show the glowing effect
	        if (links != null) {
	            for (StarLink link : links) {
	                link.draw();
	            }
	        }
	        
	        return true;
	    }
	    
	    /**
	     * Set how many stars should be lit (used when loading from database)
	     */
	    
	    public void setStarsLit(int count) {
	        if (count < 0 || count > 10) {
	            throw new IllegalArgumentException("Stars lit must be between 0 and 10");
	        }
	        
	        // Reset all stars first
	        for (int i = 0; i < stars.length; i++) {
	            stars[i].setComplete(false);
	        }
	        
	        // Light up the specified number
	        for (int i = 0; i < count; i++) {
	            stars[i].setComplete(true);
	        }
	        
	        starsLit = count;
	        
	        // Redraw links
	        if (links != null) {
	            for (StarLink link : links) {
	                link.draw();
	            }
	        }
	    }
	    
	    /**
	     * Check if this constellation is complete (all 10 stars lit)
	     */
	    public boolean isComplete() {
	        return starsLit >= 10;
	    }
	    
	    /**
	     * Get number of stars currently lit
	     */
	    public int getStarsLit() {
	        return starsLit;
	    }
	    
	    /**
	     * Get the stars array (for subclass use)
	     */
	    protected Star[] getStars() {
	        return stars;
	    }
	    
	    /**
	     * Create a small preview version of this constellation for the gallery
	     * This should show the constellation fully completed
	     */
	    public abstract Pane createPreview(double width, double height);
	    
	    /**
	     * Helper method for subclasses to create a scaled preview
	     */
	    protected Pane createScaledPreview(Star[] previewStars, StarLink[] previewLinks, 
	                                       double width, double height, double scaleFactor) {
	        Group constellationGroup = new Group();
	        
	        // Add links first (so they appear behind stars)
	        for (StarLink link : previewLinks) {
	            constellationGroup.getChildren().addAll(link.getGlowLine(), link.getCoreLine());
	            link.draw();
	        }
	        
	        // Add stars on top
	        constellationGroup.getChildren().addAll(previewStars);
	        
	        // Scale the constellation
	        constellationGroup.setScaleX(scaleFactor);
	        constellationGroup.setScaleY(scaleFactor);
	        
	        // Wrap in a StackPane to center automatically
	        StackPane wrapper = new StackPane(constellationGroup);
	        wrapper.setPrefSize(width, height);
	        wrapper.setMinSize(width, height);
	        wrapper.setMaxSize(width, height);
	        wrapper.setStyle("-fx-background-color: black;");
	        
	        return wrapper;
	    }
	    
	    public void normalize() {
	        // Force layout so bounds are valid
	        this.applyCss();
	        this.layout();

	        Bounds bounds = this.getBoundsInLocal();
	        double minX = bounds.getMinX();
	        double minY = bounds.getMinY();
	        double width = bounds.getWidth();
	        double height = bounds.getHeight();

	        // Compute the center of the constellation
	        double centerX = minX + width / 2;
	        double centerY = minY + height / 2;

	        // Shift constellation so its center is at (0,0)
	        this.setTranslateX(-centerX);
	        this.setTranslateY(-centerY);
	    }
	    
	    public Pane createShow(double width, double height) {
	        // Force constellation to normalize its position
	        this.normalize();

	        // Wrap the constellation in a StackPane so it stays centered
	        StackPane wrapper = new StackPane(this);
	        wrapper.setPrefSize(width, height);
	        wrapper.setMinSize(width, height);
	        wrapper.setMaxSize(width, height);
	        wrapper.setStyle("-fx-background-color: black;");

	        // Scale constellation to fit the wrapper
	        wrapper.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
	            double scaleX = newVal.getWidth() / this.getBoundsInParent().getWidth();
	            double scaleY = newVal.getHeight() / this.getBoundsInParent().getHeight();
	            double scale = Math.min(scaleX, scaleY);
	            this.setScaleX(scale);
	            this.setScaleY(scale);
	        });

	        return wrapper;
	    }

	    public Pane createProgressView(double width, double height) {
	        //  Use the constellation’s current stars/links state
	        return createScaledPreview(this.stars, this.links, width, height, 0.85);
	    }
	}

