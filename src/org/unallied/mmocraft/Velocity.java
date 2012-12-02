package org.unallied.mmocraft;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

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
    
    public Velocity(Velocity velocity) {
        this.x = velocity.x;
        this.y = velocity.y;
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

    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra" 
     * stuff that isn't particularly useful in this case.
     * @return objectBytes
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeFloat(x);
        writer.writeFloat(y);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of a Velocity object.
     * @return velocity
     */
    public static Velocity fromBytes(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 8) {
            return null;
        }
        
        Velocity result = new Velocity(0, 0);
        result.x = slea.readFloat();
        result.y = slea.readFloat();
        
        return result;
    }
}
