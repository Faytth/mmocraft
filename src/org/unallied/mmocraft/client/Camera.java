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
    
    /** The minimum number of pixels / second that the camera should move at. */
    public static final long CAMERA_MIN_CHANGE_RATE = 50;
    
    /** The time in milliseconds that must pass before the camera is allowed to snap again. */
    public static final long SNAP_COOLDOWN_TIME = 50;
    
    /** 
     * The time in milliseconds (from {@link System#currentTimeMillis()} that
     * the camera last snapped to the player's location.  Used to prevent jitter.
     */
    private long lastSnap = 0;
    
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
        
        long delta = System.currentTimeMillis() - lastUpdateTime;
        
        // Update currentLocation if needed
        if (destination != null && currentLocation != destination) {
            if (currentLocation == null) {
                currentLocation = destination;
            } else if (delta > 0) {
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
                                /* Check for minimum change (prevents jitter).  
                 * The "cooldown" here prevents the camera from snapping to the
                 * player's location (in other words, prevents it from being
                 * always on the character).
                 */
                if (System.currentTimeMillis() - lastSnap > SNAP_COOLDOWN_TIME) {
                    if (deltaX > -1f && deltaX < 1f && deltaX != 0) {
                        currentLocation.setRawX(destination.getRawX());
                        deltaX = 0;
                        lastSnap = System.currentTimeMillis();
                    }
                    if (deltaY > -1f && deltaY < 1f && deltaY != 0) {
                        currentLocation.setRawY(destination.getRawY());
                        deltaY = 0;
                        lastSnap = System.currentTimeMillis();
                    }
                }
                
                // Check for minimum speed and adjust as necessary.                if (deltaX != 0 && ((Math.abs(deltaX) * percentChange * (1000.0 / delta)) < CAMERA_MIN_CHANGE_RATE)) {                    deltaX = deltaX < 0 ? -1.0f : 1.0f;                    deltaX = deltaX * CAMERA_MIN_CHANGE_RATE * delta / 1000;                } else if (deltaX != 0) {                    deltaX *= percentChange;
                }                                 if (deltaY != 0 && ((Math.abs(deltaY) * percentChange * (1000.0 / delta)) < CAMERA_MIN_CHANGE_RATE)) {                    deltaY = deltaY < 0 ? -1.0f : 1.0f;                    deltaY = deltaY * CAMERA_MIN_CHANGE_RATE * delta / 1000;                } else if (deltaY != 0) {                    deltaY *= percentChange;                }                
                // Adjust for "overshooting" the destination
                BoundLocation testLocation = new BoundLocation(currentLocation);
                testLocation.moveRight(deltaX);
                testLocation.moveDown(deltaY);
                if (currentLocation.getRawDeltaX(destination) < 0) { // Moving to the right
                    if (testLocation.getRawDeltaX(destination) >= 0) {
                        deltaX = 0;
                        currentLocation.setRawX(destination.getRawX());
                    }
                } else { // Moving to the left
                    if (testLocation.getRawDeltaX(destination) <= 0) {
                        deltaX = 0;
                        currentLocation.setRawX(destination.getRawX());
                    }
                }
                if (currentLocation.getRawDeltaY(destination) < 0) { // Moving down
                    if (testLocation.getRawDeltaY(destination) >= 0) {
                        deltaY = 0;
                        currentLocation.setRawY(destination.getRawY());
                    }
                } else { // Moving up
                    if (testLocation.getRawDeltaY(destination) <= 0) {
                        deltaY = 0;
                        currentLocation.setRawY(destination.getRawY());
                    }
                }
                                currentLocation.moveRight(deltaX);                currentLocation.moveDown(deltaY);

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
