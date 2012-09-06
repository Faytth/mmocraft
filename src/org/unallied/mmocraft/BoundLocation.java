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
    public BoundLocation(long x, long y, float xOffset, float yOffset) {
        super(x, y, xOffset, yOffset);
        
        // Wrap around if necessary
        x %= WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH;
        // Correct for negative
        if (x < 0) {
            x = WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH + x;
        }
    }

    public BoundLocation(Location location) {
        super(location);
        
        // Wrap around if necessary
        x %= WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH;
        // Correct for negative
        if (x < 0) {
            x = WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH + x;
        }
    }

    public BoundLocation(long x, long y) {
        super(x, y);
        
        // Wrap around if necessary
        x %= WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH;
        // Correct for negative
        if (x < 0) {
            x = WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH + x;
        }
    }
        
    @Override
    /**
     * Move to the left some number of pixels
     * @param distance number of pixels to move left
     */
    public void moveLeft(float distance) {
        xOffset -= distance;
        
        x += ((int)xOffset) / WorldConstants.WORLD_BLOCK_WIDTH;
        xOffset %= WorldConstants.WORLD_BLOCK_WIDTH;
        
        while (xOffset < 0) {
            --x;
            xOffset += WorldConstants.WORLD_BLOCK_WIDTH;
        }
        
        // Wrap around if necessary
        x %= WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH;
        // Correct for negative
        if (x < 0) {
            x = WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH + x;
        }
    }
    
    @Override
    /**
     * Move to the right some number of pixels
     * @param distance number of pixels to move right
     */
    public void moveRight(float distance) {
        xOffset += distance;
        
        x += ((int)xOffset) / WorldConstants.WORLD_BLOCK_WIDTH;
        xOffset %= WorldConstants.WORLD_BLOCK_WIDTH;
        
        while (xOffset < 0) {
            --x;
            xOffset += WorldConstants.WORLD_BLOCK_WIDTH;
        }
        
        // Wrap around if necessary
        x %= WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH;
        
        // Correct for negative
        if (x < 0) {
            x = WorldConstants.WORLD_CHUNKS_WIDE * (long)WorldConstants.WORLD_CHUNK_WIDTH + x;
        }
     }
    
    @Override
    /**
     * Move up some number of pixels
     * @param distance number of pixels to move up
     */
    public void moveUp(float distance) {
        yOffset -= distance;
        
        y += ((int)yOffset) / WorldConstants.WORLD_BLOCK_HEIGHT;
        yOffset %= WorldConstants.WORLD_BLOCK_HEIGHT;
        
        while (yOffset < 0) {
            --y;
            yOffset += WorldConstants.WORLD_BLOCK_HEIGHT;
        }
        
        // If we're < min height, then set us to min height
        if (y < 0) {
            y = 0;
            yOffset = 0;
        }
        
        // If we're > max height, then set to max height
        long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT;
        if (y > maxHeight || (y == maxHeight && yOffset > 0)) {
            y = maxHeight;
            yOffset = 0;
        }
        // Correct for negative
        if (y < 0) {
            y = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT + y;
        }
    }
    
    @Override
    /**
     * Move up some number of pixels
     * @param distance number of pixels to move up
     */
    public void moveDown(float distance) {
        yOffset += distance;
        
        y += ((int)yOffset) / WorldConstants.WORLD_BLOCK_HEIGHT;
        yOffset %= WorldConstants.WORLD_BLOCK_HEIGHT;
        
        while (yOffset < 0) {
            --y;
            yOffset += WorldConstants.WORLD_BLOCK_HEIGHT;
        }
        
        // If we're < min height, then set us to min height
        if (y < 0) {
            y = 0;
            yOffset = 0;
        }
        
        // If we're > max height, then set to max height
        long maxHeight = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT;
        if (y > maxHeight || (y == maxHeight && yOffset > 0)) {
            y = maxHeight;
            yOffset = 0;
        }
        // Correct for negative
        if (y < 0) {
            y = WorldConstants.WORLD_CHUNKS_TALL * (long)WorldConstants.WORLD_CHUNK_HEIGHT + y;
        }
    }
    
    public static BoundLocation getLocation(SeekableLittleEndianAccessor slea) {
        if (slea == null || slea.available() < 24) {
            return null;
        }
        
        long x = slea.readLong();
        long y = slea.readLong();
        float xOffset = slea.readFloat();
        float yOffset = slea.readFloat();
        return new BoundLocation(x, y, xOffset, yOffset);
    }
}