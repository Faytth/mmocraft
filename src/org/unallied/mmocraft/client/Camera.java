package org.unallied.mmocraft.client;

import org.unallied.mmocraft.BoundLocation;

/**
 * The camera used by the game to determine which section of the game should be
 * rendered.  This camera fluidly changes its direction.
 * 
 * @author Alexandria
 *
 */
public class Camera {
    /** 
     * The maximum time in milliseconds that is allowed to pass before reaching
     * the destination.  The time delta (time between calls to 
     * {@link #getLocation()}) is divided by this number to get the percent
     * change between currentLocation and destination.
     * 
     * Higher values makes the camera change slower.  Lower values make it
     * change faster.
     */
    public static final long CAMERA_CHANGE_RATE = 300;
    
    /** The time in milliseconds (from {@link System#currentTimeMillis()}). */
    private long lastUpdateTime = 0;
    
    /** The current Camera location.  This location will tend toward the destination. */
    private BoundLocation currentLocation;
    
    /** The destination.  The Camera will move towards this gradually. */
    private BoundLocation destination;
    
    public Camera(BoundLocation location) {
        this.currentLocation = location;
        this.destination = this.currentLocation;
    }
    
    /**
     * Retrieves the current location.  This method updates the current location
     * using the current system time.
     * @return location
     */
    public BoundLocation getLocation() {
        
        // Update currentLocation if needed
        if (destination != null && currentLocation != destination) {
            if (currentLocation == null) {
                currentLocation = destination;
            } else {
                long delta = System.currentTimeMillis() - lastUpdateTime;
                delta = delta < 0 ? 0 : delta; // This should never happen, but just in case...
                // At this point, higher delta means faster movement towards the destination
                float deltaX = (float) (destination.getDeltaX(currentLocation));
                float deltaY = (float) (destination.getDeltaY(currentLocation));
                
                // We have a "hard cap" which prevents the player from flying off-screen.
                float maxDeltaX = Game.getInstance().getContainer().getWidth() / 3;
                float maxDeltaY = Game.getInstance().getContainer().getHeight() / 3;
                
                if (deltaX > maxDeltaX) {
                    currentLocation.moveRight(deltaX - maxDeltaX);
                    deltaX = maxDeltaX;
                } else if (-deltaX > maxDeltaX) {
                    currentLocation.moveRight(deltaX + maxDeltaX);
                    deltaX = -maxDeltaX;
                }
                if (deltaY > maxDeltaY) {
                    currentLocation.moveDown(deltaY - maxDeltaY);
                    deltaY = maxDeltaY;
                } else if (-deltaY > maxDeltaY) {
                    currentLocation.moveDown(deltaY + maxDeltaY);
                    deltaY = -maxDeltaY;
                }
                
                /*
                 *  We now have the difference between destination and 
                 * currentLocation.  Multiply by our percent change, and that's 
                 * how far we should move to the right and down.
                 */
                float percentChange = 1.0f * delta / CAMERA_CHANGE_RATE;
                percentChange = percentChange < 0 ? 0 : percentChange;
                percentChange = percentChange > 1f ? 1f : percentChange;
                
                // TODO:  Set a minimum speed
                
                currentLocation.moveRight(deltaX * percentChange);
                currentLocation.moveDown(deltaY * percentChange);
            }
        }
        
        // Update the last update time
        lastUpdateTime = System.currentTimeMillis();
        
        return currentLocation;
    }
    
    /**
     * Sets the {@link #destination}.  This is the location that the camera will tend towards
     * over time.  If {@link #currentLocation} is currently null, it will likewise be set.
     * @param location The new destination location.
     */
    public void setLocation(BoundLocation location) {
        if (location != null) {
            // Make sure you create a COPY of this.
            this.destination = new BoundLocation(location);
            
            // Update the current location if it isn't already set.
            if (this.currentLocation == null) {
                this.currentLocation = this.destination;
            }
        }
    }
}
