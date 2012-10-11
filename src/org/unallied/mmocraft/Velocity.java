package org.unallied.mmocraft;

/**
 * A class used in player movement among other things.  Contains an x and y
 * element of speed.  This is the number of pixels / millisecond.
 * @author Alexandria
 *
 */
public class Velocity {
    /** The horizontal component of the velocity in pixels / millisecond. */
    private float x;
    /** The vertical component of the velocity in pixels / millisecond. */
    private float y;
    
    /**
     * Creates a new velocity with the given x and y components.
     * @param x The horizontal velocity in pixels / millisecond.
     * @param y The vertical velocity in pixels / millisecond.
     */
    public Velocity(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Retrieves the x component of the velocity.  This is the number of horizontal
     * pixels that should be traversed per millisecond.
     * Positive values mean right, negative means left.
     * @return x
     */
    public float getX() {
        return x;
    }
    
    /**
     * Sets the horizontal component of the velocity.
     * @param x The new horizontal velocity in pixels / millisecond.
     */
    public void setX(float x) {
        this.x = x;
    }
    
    /**
     * Retrieves the y component of the velocity.  This is the number of vertical
     * pixels that should be traversed per millisecond.
     * Positive values mean down, negative values mean up.
     * @return y
     */
    public float getY() {
        return y;
    }
    
    /**
     * Sets the vertical component of the velocity.
     * @param y The new vertical velocity in pixels / millisecond.
     */
    public void setY(float y) {
        this.y = y;
    }
}
