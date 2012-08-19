package org.unallied.mmocraft;

/**
 * All objects that can collide with one another must inherit from this class.
 * @author Faythless
 *
 */
public interface Collidable {
    
    /**
     * Determines whether this object will collide with another object.
     * @return true if the object can collide.
     */
    public boolean isCollidable();
}
