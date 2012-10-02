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

    private transient int hashCodeValue = 0;
    
    protected long x;       // The x coordinate of a specific block
    protected long y;       // The y coordinate of a specific block
    protected float xOffset; // offset from the block at (x,y)
    protected float yOffset; // offset from the block at (x,y)
    
    /**
     * Creates a specific Location in the world.
     * @param x The x coordinate of a specific block
     * @param y The y coordinate of a specific block
     * @param xOffset X offset from the block at (x,y)
     * @param yOffset Y offset from the block at (x,y)
     */
    public Location(long x, long y, float xOffset, float yOffset) {
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Location(Location location) {
        this.x = location.x;
        this.y = location.y;
        this.xOffset = location.xOffset;
        this.yOffset = location.yOffset;
    }

    public Location(long x, long y) {
        this.x = x;
        this.y = y;
        this.xOffset = 0.0f;
        this.yOffset = 0.0f;
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
        
        return  x == o.x &&
                y == o.y &&
                xOffset == o.xOffset &&
                yOffset == o.yOffset;
    }
    
    @Override
    public int hashCode() {
        if (hashCodeValue == 0) {
            int result = 0;
            result = HashCodeUtil.hash(result, x);
            result = HashCodeUtil.hash(result, y);
            result = HashCodeUtil.hash(result, xOffset);
            result = HashCodeUtil.hash(result, yOffset);
            hashCodeValue = result;
        }
        
        return hashCodeValue;
    }
    
    /**
     * Returns the x coordinate of a specific block
     * @return the x coordinate of a specific block
     */
    public long getX() {
        return x;
    }
    
    /**
     * Returns the y coordinate of a specific block
     * @return the y coordinate of a specific block
     */
    public long getY() {
        return y;
    }
    
    /**
     * Returns the x offset from the block at (x,y)
     * @return the x offset from the block at (x,y)
     */
    public float getXOffset() {
        return xOffset;
    }
    
    /**
     * Returns the y offset from the block at (x,y)
     * @return the y offset from the block at (x,y)
     */
    public float getYOffset() {
        return yOffset;
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new x value.
     * @param x the new x value (each value of x is one block)
     */
    public void setX(long x) {
        // TODO Auto-generated method stub
        this.x = x;
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new y value.
     * @param y the new y value (each value of y is one block)
     */
    public void setY(long y) {
        this.y = y;
    }

    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new x offset.
     * @param xOffset the new x offset (each value is 1 pixel)
     */
    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }
    
    /**
     * This should only be used in special cases.  For all movement, use the
     * move functions instead.  This function assigns a new y offset.
     * @param yOffset the new y offset (each value is 1 pixel)
     */
    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
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
            xOffset -= distance;
            
            x += ((int)xOffset) / WorldConstants.WORLD_BLOCK_WIDTH;
            xOffset %= WorldConstants.WORLD_BLOCK_WIDTH;
            
            while (xOffset < 0) {
                --x;
                xOffset += WorldConstants.WORLD_BLOCK_WIDTH;
            }
        }
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
            xOffset += distance;
            
            x += ((int)xOffset) / WorldConstants.WORLD_BLOCK_WIDTH;
            xOffset %= WorldConstants.WORLD_BLOCK_WIDTH;
            
            while (xOffset < 0) {
                --x;
                xOffset += WorldConstants.WORLD_BLOCK_WIDTH;
            }
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
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * @return
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeLong(x);
        writer.writeLong(y);
        writer.writeFloat(xOffset);
        writer.writeFloat(yOffset);
        
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
        long x = slea.readLong();
        long y = slea.readLong();
        float xOffset = slea.readFloat();
        float yOffset = slea.readFloat();
        return new Location(x, y, xOffset, yOffset);
    }
}
