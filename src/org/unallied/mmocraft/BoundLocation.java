package org.unallied.mmocraft;

import java.io.Serializable;

import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;


/**
 * A simple class containing coordinates in the world.  Also contains
 * offsets between coordinates.  The way this works is say you have
 * some block at (x,y).  You then have a player who is almost at the
 * middle of that block, but a little to the right.  You would have a
 * slightly positive xOffset.  1 xOffset is the equivalent of 1 pixel.
 * @author Faythless
 *
 */
public class BoundLocation extends Location implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3158656874779294473L;
        
    /**
     * Creates a specific Location in the world.
     * @param x The x coordinate of a specific block
     * @param y The y coordinate of a specific block
     * @param xOffset X offset from the block at (x,y)
     * @param yOffset Y offset from the block at (x,y)
     */
    public BoundLocation(int x, int y, float xOffset, float yOffset) {
        super(x, y, xOffset, yOffset);
        
        fixBorders();
    }

    /**
     * Copy constructor.  Creates a specific Location in the world.
     * @param location The location to create a bound location from.
     */
    public BoundLocation(Location location) {
        super(location);
        
        fixBorders();
    }

    /**
     * Creates a specific Location in the world.
     * @param x The x coordinate of a specific block
     * @param y The y coordinate of a specific block
     */
    public BoundLocation(int x, int y) {
        this(x, y, 0, 0);
    }
        
    @Override
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
        
        fixBorders();
    }
    
    @Override
    /**
     * Retrieves the x difference in pixels between two locations.
     * This difference is (this.x - other.x) (where x is number of pixels).
     * This distance is appropriately wrapped around as needed.
     * 
     * @param other The other location to compare this location to.
     * @return deltaX
     */
    public double getDeltaX(Location other) {
        double result;
        
        // Technically this is a kludge, but no one really cares
        long delta = this.x - other.x;
        if (delta < WorldConstants.WORLD_WIDTH / 2 * BLOCK_GRANULARITY && 
                delta > -WorldConstants.WORLD_WIDTH / 2 * BLOCK_GRANULARITY) {
            result = delta;
        } else { // We need to wrap
            result = delta;
            result += result < 0 ? WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY : -WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY;
        }

        return result / BLOCK_GRANULARITY * WorldConstants.WORLD_BLOCK_WIDTH;
    }
    
    @Override
    /**
     * Retrieves the raw x difference in pixels between two locations.
     * This difference is (this.x - other.x) (where x is number of pixels).
     * This distance is appropriately wrapped around as needed.
     * 
     * @param other The other location to compare this location to.
     * @return rawDeltaX
     */
    public long getRawDeltaX(Location other) {
        long result;
        
        // Technically this is a kludge, but no one really cares
        long delta = this.x - other.x;
        if (delta < WorldConstants.WORLD_WIDTH / 2 * BLOCK_GRANULARITY && 
                delta > -WorldConstants.WORLD_WIDTH / 2 * BLOCK_GRANULARITY) {
            result = delta;
        } else { // We need to wrap
            result = delta;
            result += result < 0 ? WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY : -WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY;
        }

        return result;
    }
    
    /**
     * Retrieves the x difference in blocks between the two locations.
     * @param other The other location to compare this location to.
     * @return deltaX
     */
    public int getBlockDeltaX(Location other) {
        int result;
        
        int delta = this.getX() - other.getX();
        if (delta < WorldConstants.WORLD_WIDTH / 2 && delta > -WorldConstants.WORLD_WIDTH / 2) {
            result = delta;
        } else { // We need to wrap
            result = delta;
            result += result < 0 ? WorldConstants.WORLD_WIDTH : -WorldConstants.WORLD_WIDTH;
        }
        
        return result;
    }
    
    @Override
    /**
     * Moves to the left a raw number of location units.
     * @param distance The distance to go to the left
     */
    public void moveRawLeft(long distance) {
        x -= distance;
        fixBorders();
    }
    
    @Override
    /**
     * Moves to the right a raw number of location units.
     * @param distance The distance to go to the right
     */
    public void moveRawRight(long distance) {
        x += distance;
        fixBorders();
    }
    
    @Override
    /**
     * Moves down a raw number of location units.
     * @param distance The distance to go down.
     */
    public void moveRawDown(long distance) {
        y += distance;
        fixBorders();
    }
    
    @Override
    /**
     * Moves up a raw number of location units.
     * @param distance The distance to go up.
     */
    public void moveRawUp(long distance) {
        y -= distance;
        fixBorders();
    }
    
    @Override
    /**
     * Fixes all x and y coordinates of a BoundLocation to ensure that they
     * remain within the bounds.
     */
    public void fixBorders() {
        // Wrap around if necessary
        x %= WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY;
        // Correct for negative
        while (x < 0) {
            x += WorldConstants.WORLD_WIDTH * BLOCK_GRANULARITY;
        }
        
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
    
    @Override
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
        
        fixBorders();
    }
    
    public static BoundLocation getLocation(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 16) {
            return null;
        }
        
        BoundLocation result = new BoundLocation(0, 0);
        result.x = slea.readLong();
        result.y = slea.readLong();
        result.fixBorders();
        return result;
    }
    
    @Override
    /**
     * Moves x by the smallest possible amount to the right.
     */
    public void incrementX() {
        ++x;
        fixBorders();
    }
    
    @Override
    /**
     * Moves x by the smallest possible amount to the left.
     */
    public void decrementX() {
        --x;
        fixBorders();
    }
    
    @Override
    /**
     * Sets the raw x value.  Use this when you need subpixel-perfect changes.
     * @param x The raw x value
     */
    public void setRawX(long x) {
        this.x = x;
        fixBorders();
    }
    
    @Override
    /**
     * Sets the raw y value.  Use this when you need subpixel-perfect changes.
     * @param y The raw y value
     */
    public void setRawY(long y) {
        this.y = y;
        fixBorders();
    }

    /**
     * Returns whether this location is contained in the rectangle specified by
     * topLeft and bottomRight.  Points on the line of the rectangle are counted.
     * @param topLeft The top left corner of the rectangle.
     * @param bottomRight The bottom right corner of the rectangle.
     * @return true if this location is contained in the rectangle, else false.
     */
    public boolean contains(Location topLeft, Location bottomRight) {
        return getRawDeltaX(topLeft) >= 0 && getRawDeltaX(bottomRight) <= 0 &&
               getRawDeltaY(topLeft) >= 0 && getRawDeltaY(bottomRight) <= 0;
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of a Location.
     * @return location
     */
    public static BoundLocation fromBytes(SeekableLittleEndianAccessor slea) {
        // Guard
        if (slea == null || slea.available() < 16) {
            return null;
        }
        
        BoundLocation result = new BoundLocation(0, 0);
        result.x = slea.readLong();
        result.y = slea.readLong();
        return result;
    }

    /**
     * Retrieves the distance (in pixels) between two locations.  The distance
     * calculation used is:  (sqrt((x1-x2)^2 + (y1-y2)^2)), where x1,y1 is <code>this</code>
     * and x2,y2 is <code>other</code>.
     * @param other The location to get the distance to.
     * @return the distance in pixels between the locations.
     */
    public double getDistance(BoundLocation other) {
        double deltaX = getDeltaX(other);
        double deltaY = getDeltaY(other);
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    /**
     * Retrieves the distance (in blocks) between two locations.  The distance
     * calculation used is:  (sqrt((x1-x2)^2 + (y1-y2)^2)), where x1,y1 is <code>this</code>
     * and x2,y2 is <code>other</code>.
     * @param other The location to get the distance to.
     * @return the distance in blocks between the locations.
     */
    public double getBlockDistance(BoundLocation other) {
        double deltaX = getBlockDeltaX(other);
        double deltaY = getBlockDeltaY(other);
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
