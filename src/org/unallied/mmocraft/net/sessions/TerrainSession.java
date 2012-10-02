package org.unallied.mmocraft.net.sessions;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Location;
import org.unallied.mmocraft.TerrainChunk;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.constants.WorldConstants;

/**
 * Handles all in-game terrain blocks, chunks, and regions.  Also queries the
 * server for new chunks as needed.
 * @author Faythless
 *
 */
public class TerrainSession {
    
    /**
     *  Contains all terrain chunks in the world.  These are culled from time to time.
     *  The key is a unique
     */
    private Map<Long, TerrainChunk> chunks = new HashMap<Long, TerrainChunk>();
        
    /**
     * Culls terrain that is no longer needed
     */
    void cull() {
        
        /**
         *  For all entries, remove them if the time since last visit > threshold 
         *  and the player's distance from the chunk is > threshold
         */
    }
    
    public void refresh() {
        
    }
    
    /**
     * Returns the block at location (x,y) where each value is a block.
     * @param location the location containing (x,y) of the block
     * @return the block at location (x,y).  Returns null if chunk is missing
     */
    public Block getBlock(Location location) {
        return getBlock(location.getX(), location.getY());
    }
    
    /**
     * Returns the block at location (x,y) where each value is a block.
     * If x > <code>WorldConstants.WORLD_WIDTH</code>, then it is wrapped
     * around.  If x is negative, it is wrapped up to ONE time.  Large
     * negatives are NOT handled and are considered an error.
     * @param x the horizontal coordinate starting at x=0 from the left
     * @param y the vertical coordinate starting at y=0 from the top
     * @return the block at location (x,y).  Returns null if chunk is missing
     */
    public Block getBlock(long x, long y) {
        // Get chunk x and y coordinates
        x = x >= 0 ? x % WorldConstants.WORLD_WIDTH : WorldConstants.WORLD_WIDTH + x;
        long cx = x / WorldConstants.WORLD_CHUNK_WIDTH;
        long cy = y / WorldConstants.WORLD_CHUNK_HEIGHT;
        int  bx = (int) (x % WorldConstants.WORLD_CHUNK_WIDTH);
        int  by = (int) (y % WorldConstants.WORLD_CHUNK_HEIGHT);
        
        long chunkId = ((long) (cy) << 32) | cx;
        
        if (!chunks.containsKey(chunkId)) {
            chunks.put(chunkId, new TerrainChunk(chunkId));
        }
        return chunks.get(chunkId).getBlock(bx, by);
    }
    
    /**
     * Render the blocks of the world.  This should probably be the first thing rendered.
     * @param container
     * @param game
     * @param g
     * @param location Location of the player.  Rendering will be centralized around this point.
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation location) {
        
        // Screen width and height
        int w = container.getWidth();
        int h = container.getHeight();
        
    	int chunkWidth = WorldConstants.WORLD_BLOCK_WIDTH * WorldConstants.WORLD_CHUNK_WIDTH;
    	int chunkHeight = WorldConstants.WORLD_BLOCK_HEIGHT * WorldConstants.WORLD_CHUNK_HEIGHT;
    	int chunksAcross = (w / chunkWidth) + 1;
		int chunksHigh   = (h / chunkHeight) + 1;
		chunksAcross = chunksAcross * 2 + 1;
		chunksHigh   = chunksHigh   * 2 + 1;
		            
        // Grab the unique chunk IDs based on player's center chunk
        long x = location.getX();
        long y = location.getY();
        
        // The maximum value of x.  Any higher will wrap
        long maxX = WorldConstants.WORLD_CHUNKS_WIDE;
        
        // The maximum value of y.  Any higher will wrap
        long maxY = WorldConstants.WORLD_CHUNKS_TALL;
        
        // Divide by size of a chunk
        x /= WorldConstants.WORLD_CHUNK_WIDTH;
        y /= WorldConstants.WORLD_CHUNK_HEIGHT;
        
        // x and y now define a chunk
        
        // Render the frame
        float xBase = location.getX() - (x - (chunksAcross-1)/2) * WorldConstants.WORLD_CHUNK_WIDTH;
        float yBase = location.getY() - (y - (chunksHigh-1)/2) * WorldConstants.WORLD_CHUNK_HEIGHT;
        xBase *= WorldConstants.WORLD_BLOCK_WIDTH;
        yBase *= WorldConstants.WORLD_BLOCK_HEIGHT;
        xBase += location.getXOffset();
        yBase += location.getYOffset();

        
        for (int i=0; i < chunksAcross; ++i) { // columns
            for (int j=0; j < chunksHigh; ++j) { // rows
                // (y << 32) | x
                int chunkX = (int) ((x+i-(chunksAcross-1)/2) % maxX);
                int chunkY = (int) (y+j-(chunksHigh-1)/2);
                
                // Make sure we don't get negative coordinates
                chunkX = (int) (chunkX < 0 ? maxX+chunkX : chunkX);
                chunkY = (int) (chunkY < 0    ? 0 : 
                                chunkY > maxY ? maxY :
                                                chunkY);
                
                long chunkId = ((long) (chunkY) << 32) | chunkX;

                if (!chunks.containsKey(chunkId)) {
                    chunks.put(chunkId, new TerrainChunk(chunkId));
                }
                chunks.get(chunkId).render(container, game, g, xBase, yBase,
                        i, j, true);
            }
        }
    }

    /**
     * Assigns an array of BlockTypes to the chunk with the given <code>chunkId</code>.
     * If no chunk with <code>chunkId</code> exists, it will be created.
     * @param chunkBlocks Type of blocks to create
     */
    public void setChunk(long chunkId, Block[][] chunkBlocks) {
        if (!chunks.containsKey(chunkId)) {
            chunks.put(chunkId, new TerrainChunk(chunkId, chunkBlocks));
        } else {
            chunks.get(chunkId).load(chunkBlocks);
        }
    }

    /**
     * Get the location that is just before <code>end</code> based off of the
     * starting location.
     * @param end the location to start at
     * @param end2 the old end location
     * @param collision the new end location
     * @return the location right before the end location
     */
    private Location getMaxLocation(Location end, Location end2, BoundLocation collision) {
        
        // Fix offsets for collision.  FIXME:  This is a kludge!
        if (end.getX() == end2.getX() && end.getXOffset() == end2.getXOffset()) {
            collision.setXOffset(end.getXOffset());
        }
        if (end.getY() == end2.getY() && end.getYOffset() == end2.getYOffset()) {
            collision.setYOffset(end.getYOffset());
        }
        
        // Special case for same block
        if (end.getX() == collision.getX() && end.getY() == collision.getY()) {
            collision.moveLeft(1);
            collision.moveUp(1);
        } else {
            // Fix the x location
            if (end.getX() < collision.getX()) {
                collision.moveLeft(1);
            } else if (end.getX() > collision.getX()){
                collision.moveRight(WorldConstants.WORLD_BLOCK_WIDTH);
            }
            
            // Fix the y location
            if (end.getY() < collision.getY()) {
                collision.moveUp(1);
            } else if (end.getY() > collision.getY()) {
                collision.moveDown(WorldConstants.WORLD_BLOCK_HEIGHT);
            }
        }
        
        return collision;
    }
    
    /**
     * Returns true if the path between <code>start</code> and <code>end</code>
     * collides with a block.
     * Uses Bresenham's line algorithm.
     * @param end the starting location (before moving)
     * @param end2 the ending location (after moving)
     * @return modified location
     */
    public Location collideWithBlock(Location end, Location end2) {
        Location specialEnd = new Location(end2); // used for sub-pixel checks
        long x0 = end.getX();
        long y0 = end.getY();
        long x1;
        long y1;

        // Special check for sub-pixels
        // In order for "jitter" to occur, start and end must be the same block
        if (end.getX() == end2.getX() && end.getY() == end2.getY()) {
            
            if (end2.getXOffset() < 1.0f && end.getXOffset() > end2.getXOffset()) {
                specialEnd.moveLeft(1);
            } else if (end2.getXOffset() > (WorldConstants.WORLD_BLOCK_WIDTH-1) && end.getXOffset() < end2.getXOffset()) {
                specialEnd.moveRight(1);
            }
            if (end2.getYOffset() < 1.0f && end.getYOffset() > end2.getYOffset()) {
                specialEnd.moveUp(1);
            } else if (end2.getYOffset() > (WorldConstants.WORLD_BLOCK_HEIGHT-1) && end.getYOffset() < end2.getYOffset()) {
                specialEnd.moveDown(1);
            }
        }
        x1 = specialEnd.getX();
        y1 = specialEnd.getY();
        
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        long tmp;
        if (steep) {
            tmp = x0;
            x0 = y0;
            y0 = tmp;
            
            tmp = x1;
            x1 = y1;
            y1 = tmp;
        }
        long deltax = Math.abs(x1 - x0);
        long deltay = Math.abs(y1 - y0);
        long error  = deltax / 2;
        long ystep;
        long y = y0;
        if (y0 < y1) {
            ystep = 1;
        } else {
            ystep = -1;
        }
        
        long inc;
        if (x0 < x1) {
            inc = 1;
        } else {
            inc = -1;
        }
        // We want to ignore starting position.  Return if we get a solid block
        // TODO:  Should we allow collisions on our current block?
        boolean isLast = false;
        for (long x=x0; x != x1 || isLast; x+=inc) {
            if (steep) {
                Block b = getBlock(y,x);
                if (b == null || b.isCollidable()) {
                    return getMaxLocation(end, end2, new BoundLocation(y,x));
                }
            } else {
                Block b = getBlock(x,y);
                if (b == null || b.isCollidable()) {
                    return getMaxLocation(end, end2, new BoundLocation(x,y));
                }
            }
            error -= deltay;
            if (error < 0) {
                y += ystep;
                error += deltax;
            }
            if (isLast) {
                break;
            }
            isLast = x+inc == x1;
        }
                
        return end2; // no collision
    }

    /**
     * Removes all cached chunks from the terrain session.  They will need to
     * be queried from the server again.
     */
    public void clear() {
        chunks.clear();
    }

    /**
     * Sets a block at a specific (x,y).  If the chunk does not yet exist it
     * will not be created, and the block will not be set.
     * @param x The x coordinate of the block to set.
     * @param y The y coordinate of the block to set.
     * @param block The new block.
     */
    public void setBlock(long x, long y, Block block) {
        // Get chunk x and y coordinates
        x = x >= 0 ? x % WorldConstants.WORLD_WIDTH : WorldConstants.WORLD_WIDTH + x;
        long cx = x / WorldConstants.WORLD_CHUNK_WIDTH;
        long cy = y / WorldConstants.WORLD_CHUNK_HEIGHT;
        int  bx = (int) (x % WorldConstants.WORLD_CHUNK_WIDTH);
        int  by = (int) (y % WorldConstants.WORLD_CHUNK_HEIGHT);
        
        long chunkId = ((long) (cy) << 32) | cx;
        
        // The chunk does not yet exist.  Don't set the block.
        if (!chunks.containsKey(chunkId)) {
            return;
        }
        
        chunks.get(chunkId).setBlock(bx, by, block);
    }
}
