package org.unallied.mmocraft;

import org.unallied.mmocraft.utils.HashCodeUtil;

/**
 * Point2D was insufficient because I needed subpixel-perfect points.  Since
 * Location defines a minimum granularity for these points, it makes sense, then,
 * to have a point class capable of handling this information.
 * 
 * @author Alexandria
 *
 */
public class RawPoint {
    private long x;
    private long y;
    
    private int hashCodeValue;
    
    /**
     * Creates a new raw point at (0, 0).
     */
    public RawPoint() {
        this(0, 0);
    }
    
    /**
     * Creates a new raw point with the given coordinates.
     * @param x The x coordinate of the raw point.
     * @param y The y coordinate of the raw point.
     */
    public RawPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Retrieves the raw x value of this point.
     * @return x
     */
    public long getX() {
        return x;
    }
    
    /**
     * Retrieves the raw y value of this point.
     * @return y
     */
    public long getY() {
        return y;
    }
    
    /**
     * Sets the raw x value of this point.
     * @param x The new x value.
     */
    public void setX(long x) {
        this.x = x;
    }
    
    /**
     * Sets the raw y value of this point.
     * @param y The new y value.
     */
    public void setY(long y) {
        this.y = y;
    }
    
    /**
     * Sets the raw x and y values of this point.
     * @param x The new x value.
     * @param y The new y value.
     */
    public void setLocation(long x, long y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        // self-comparison check
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof RawPoint)) {
            return false;
        }
        
        RawPoint o = (RawPoint)obj;
        
        return x == o.x && y == o.y;
    }
    
    @Override
    public int hashCode() {
        if (hashCodeValue == 0) {
            int result = 0;
            result = HashCodeUtil.hash(result, x);
            result = HashCodeUtil.hash(result, y);
            hashCodeValue = result;
        }
        
        return hashCodeValue;
    }
}
