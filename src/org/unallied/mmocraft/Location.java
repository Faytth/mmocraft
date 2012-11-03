package org.unallied.mmocraft;

import java.io.Serializable;

import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;
import org.unallied.mmocraft.utils.HashCodeUtil;

/**
 * A special type of Location that is not restricted along the x axis.  Use
 * this when you need special collisions for edge of world detection.  This is
 * still bounded along the y axis.
 * @author Faythless
 *
 */
public class Location implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6181843042410877482L;

    /** 
     * The number of units per block.  Since a block's coordinates must be
     * no larger than an int, and it's represented by longs, the absolute
     * maximum granularity is about 2^32-1.  However, we're keeping it simple.
     * Because a float can have about 7 decimal digits of precision, we want to
     * keep this value small.
     * */
    public static final long BLOCK_GRANULARITY = 100000;
    
    private transient int hashCodeValue = 0;
    
    /** The x coordinate of this location. */
    protected long x;

    /** The y coordinate of this location. */
    protected long y;
    
    /**
     * Creates a specific Location in the world.
     * @param x The x coordinate of a specific block
     * @param y The y coordinate of a specific block
     * @param xOffset X offset from the block at (x,y)
     * @param yOffset Y offset from the block at (x,y)
     */
    public Location(long x, long y, float xOffset, float yOffset) {
        this.x = x * BLOCK_GRANULARITY;
        this.y = y * BLOCK_GRANULARITY;
        if (xOffset != 0) {
            this.x +=  BLOCK_GRANULARITY * xOffset / WorldConstants.WORLD_BLOCK_WIDTH;
        }
        if (yOffset != 0) {
            this.y += BLOCK_GRANULARITY * yOffset / WorldConstants.WORLD_BLOCK_HEIGHT;
        }
    }

    public Location(Location location) {
        this.x = location.x;
        this.y = location.y;
    }

    public Location(long x, long y) {
        this(x, y, 0, 0);
    }
    
    @Override
    public boolean equals(Object obj) {

        // self-comparison check
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof Location)) {
            return false;
        }
        
        Location o = (Location)obj;
        
        return  x == o.x && y == o.y;
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
    
    /**
     * Returns the x coordinate of a specific block
     * @return the x coordinate of a specific block
     */
    public long getX() {
        return x >= 0 ? x / BLOCK_GRANULARITY : x / BLOCK_GRANULARITY - 1;
    }
    
    /**
     * Returns the y coordinate of a specific block
     * @return the y coordinate of a specific block
     */
    public long getY() {
        return y / BLOCK_GRANULARITY;
    }
    
    /**
     * Returns the x offset from the block at (x,y)
     * @return the x offset from the block at (x,y)
     */
    public float getXOffset() {
        return (float) (1.0 * (x % BLOCK_GRANULARITY) / BLOCK_GRANULARITY * WorldConstants.WORLD_BLOCK_WIDTH);
    }
    
    /**
     * Returns the y offset from the block at (x,y)
     * @return the y offset from the block at (x,y)
     */
    public float getYOffset() {
        return (float) (1.0 * (y % BLOCK_GRANULARITY) / BLOCK_GRANULARITY * WorldConstants.WORLD_BLOCK_HEIGHT);
    }
    
    /**
     * Retrieves the raw x offset from the block at (x,y).
     * This value is between 0 and BLOCK_GRANULARITY - 1.
     * @return rawXOffset
     */
    public long getRawXOffset() {
        return x % BLOCK_GRANULARITY;
    }
    
    /**
     * Retrieves the raw y offset from the block at (x,y).
     * This value is between 0 and BLOCK_GRANULARITY - 1.
     * @return rawYOffset
     */
    public long getRawYOffset() {
        return y % BLOCK_GRANULARITY;
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new x value.
     * @param x the new x value (each value of x is one block)
     */
    public void setX(long x) {
        this.x = x * BLOCK_GRANULARITY + this.x % BLOCK_GRANULARITY;
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new y value.
     * @param y the new y value (each value of y is one block)
     */
    public void setY(long y) {
        this.y = y * BLOCK_GRANULARITY + this.y % BLOCK_GRANULARITY;
    }

    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new x offset.
     * @param xOffset the new x offset (each value is 1 pixel)
     */
    public void setXOffset(float xOffset) {
        this.x = (this.x / BLOCK_GRANULARITY) * BLOCK_GRANULARITY;
        if (xOffset != 0) {
            this.x += (long)(xOffset / WorldConstants.WORLD_BLOCK_WIDTH * BLOCK_GRANULARITY);
        }
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new y offset.
     * @param yOffset the new y offset (each value is 1 pixel)
     */
    public void setYOffset(float yOffset) {
        this.y = (this.y / BLOCK_GRANULARITY) * BLOCK_GRANULARITY;
        if (yOffset != 0) {
            this.y += (long)(yOffset / WorldConstants.WORLD_BLOCK_HEIGHT * BLOCK_GRANULARITY);
        }
    }
    
    /**
     * Fixes all x and y coordinates of a BoundLocation to ensure that they
     * remain within the bounds.
     */
    public void fixBorders() {
        // If we're < min height, then set us to min height
        if (y < 0) {
            y = 0;
        }
        
        // If we're > max height, then set to max height
        long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT * BLOCK_GRANULARITY - 1;
        if (y > maxHeight) {
            y = maxHeight;
        }
        // Correct for negative
        if (y < 0) {
            y = maxHeight + 1 + y;
        }
    }
    
    /**
     * Move to the left some number of pixels
     * @param distance number of pixels to move left
     */
    public void moveLeft(float distance) {
        if (distance == 0) {
            return;
        }
        
        if (distance < 0) {
            moveRight(-distance);
        } else {
            x -= distance / WorldConstants.WORLD_BLOCK_WIDTH * BLOCK_GRANULARITY;
        }
    }
    
    /**
     * Moves to the left a raw number of location units.
     * @param distance The distance to go to the left
     */
    public void moveRawLeft(long distance) {
        x -= distance;
        fixBorders();
    }
    
    /**
     * Moves to the right a raw number of location units.
     * @param distance The distance to go to the right
     */
    public void moveRawRight(long distance) {
        x += distance;
        fixBorders();
    }
    
    /**
     * Moves down a raw number of location units.
     * @param distance The distance to go down.
     */
    public void moveRawDown(long distance) {
        y += distance;
        fixBorders();
    }
    
    /**
     * Moves up a raw number of location units.
     * @param distance The distance to go up.
     */
    public void moveRawUp(long distance) {
        y -= distance;
        fixBorders();
    }
    
    /**
     * Retrieves the x difference in pixels between two locations.
     * This difference is (this.x - other.x) (where x is number of pixels)
     * @param other The other location to compare this location to.
     * @return deltaX
     */
    public double getDeltaX(Location other) {
        double result;
        
        result = this.x - other.x;
        result = result / BLOCK_GRANULARITY * WorldConstants.WORLD_BLOCK_WIDTH;
        
        return result;
    }
    
    /**
     * Retrieves the x difference in blocks between the two locations.
     * @param other The other location to compare this location to.
     * @return deltaX
     */
    public long getBlockDeltaX(Location other) {
        long result;
        
        result = this.getX() - other.getX();
        
        return result;
    }
    
    /**
     * Retrieves the raw x difference in pixels between two locations.
     * This difference is (this.x - other.x) (where x is number of pixels)
     * @param other The other location to compare this location to.
     * @return rawDeltaX
     */
    public long getRawDeltaX(Location other) {
        return this.x - other.x;
    }
    
    /**
     * Retrieves the y difference in pixels between two locations.
     * This difference is (this.y - other.y) (where y is number of pixels)
     * @param other The other location to compare this location to.
     * @return deltaY
     */
    public double getDeltaY(Location other) {
        double result;
        
        result = this.y - other.y;
        result = result / BLOCK_GRANULARITY * WorldConstants.WORLD_BLOCK_HEIGHT;
        
        return result;
    }
    
    /**
     * Retrieves the y difference in blocks between the two locations.
     * @param other The other location to compare this location to.
     * @return deltaY
     */
    public long getBlockDeltaY(Location other) {
        long result;
        
        result = this.getY() - other.getY();
        
        return result;
    }
    
    /**
     * Retrieves the raw y difference in pixels between two locations.
     * This difference is (this.y - other.y) (where y is number of pixels)
     * @param other The other location to compare this location to.
     * @return deltaY
     */
    public long getRawDeltaY(Location other) {
        return this.y - other.y;
    }
    
    /**
     * Move to the right some number of pixels
     * @param distance number of pixels to move right
     */
    public void moveRight(float distance) {
        if (distance == 0) {
            return;
        }
        
        if (distance < 0) {
            moveLeft(-distance);
        } else {
            x += distance / WorldConstants.WORLD_BLOCK_WIDTH * BLOCK_GRANULARITY;
        }
    }
    
    /**
     * Move up some number of pixels
     * @param distance number of pixels to move up
     */
    public void moveUp(float distance) {
        if (distance == 0) {
            return;
        }
        
        if (distance < 0) {
            moveDown(-distance);
        } else {
            y -= distance / WorldConstants.WORLD_BLOCK_HEIGHT * BLOCK_GRANULARITY;
            
            // If we're < min height, then set us to min height
            if (y < 0) {
                y = 0;
            }
            
            // If we're > max height, then set to max height
            long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT * BLOCK_GRANULARITY - 1;
            if (y > maxHeight) {
                y = maxHeight;
            }
            // Correct for negative
            if (y < 0) {
                y = maxHeight + 1 + y;
            }
        }
     }
    
    /**
     * Move down some number of pixels.  If distance is negative, it will move
     * up -distance pixels.
     * @param distance number of pixels to move down
     */
    public void moveDown(float distance) {
        if (distance == 0) {
            return;
        }
        
        if (distance < 0) {
            moveUp(-distance);
        } else {
            y += distance / WorldConstants.WORLD_BLOCK_HEIGHT * BLOCK_GRANULARITY;
            
            // If we're < min height, then set us to min height
            if (y < 0) {
                y = 0;
            }
            
            // If we're > max height, then set to max height
            long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT * BLOCK_GRANULARITY - 1;
            if (y > maxHeight) {
                y = maxHeight;
            }
            // Correct for negative
            if (y < 0) {
                y = maxHeight + 1 + y;
            }
        }
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * @return objectBytes
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeLong(x);
        writer.writeLong(y);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of a Location.
     * @return location
     */
    public static Location fromBytes(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 16) {
            return null;
        }
        
        Location result = new Location(0, 0);
        result.x = slea.readLong();
        result.y = slea.readLong();
        return result;
    }
    
    /**
     * Moves x by the smallest possible amount to the right.
     */
    public void incrementX() {
        ++x;
    }
    
    /**
     * Moves x by the smallest possible amount to the left.
     */
    public void decrementX() {
        --x;
    }
    
    /**
     * Moves y by the smallest possible amount down.
     */
    public void incrementY() {
        long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT * BLOCK_GRANULARITY - 1;
        if (y < maxHeight) {
            ++y;
        }
    }
    
    /**
     * Moves y by the smallest possible amount up.
     */
    public void decrementY() {
        if (y > 0) {
            --y;
        }
    }
    
    /**
     * Returns the raw x value.  This value should be used when subpixel-perfect
     * movements are required.
     * @return x
     */
    public long getRawX() {
        return x;
    }
    
    /**
     * Returns the raw y value.  This value should be used when subpixel-perfect
     * movements are required.
     * @return y
     */
    public long getRawY() {
        return y;
    }
    
    /**
     * Sets the raw x value.  Use this when you need subpixel-perfect changes.
     * @param x The raw x value
     */
    public void setRawX(long x) {
        this.x = x;
    }
    
    /**
     * Sets the raw y value.  Use this when you need subpixel-perfect changes.
     * @param y The raw y value
     */
    public void setRawY(long y) {
        this.y = y;
        fixBorders();
    }
}
