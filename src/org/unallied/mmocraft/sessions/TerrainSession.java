package org.unallied.mmocraft.sessions;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Location;
import org.unallied.mmocraft.TerrainChunk;
import org.unallied.mmocraft.TerrainChunkLoadBalancer;
import org.unallied.mmocraft.blocks.AirGreenBlock;
import org.unallied.mmocraft.blocks.AirRedBlock;
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
     *  The key is a unique to the chunk and is based on the x and y coords of the chunk.
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
     * @param location the location containing (x,y) of the block
     * @param queryChunk True if we should query this chunk, false if we should
     * just assume it's filled with nothingness.
     * @return the block at location (x,y).  Returns null if chunk is missing
     */
    public Block getBlock(Location location, boolean queryChunk) {
        return getBlock(location.getX(), location.getY(), queryChunk);
    }
    
    /**
     * Returns the block at location (x,y) where each value is a block.
     * If x >= <code>WorldConstants.WORLD_WIDTH</code>, then it is wrapped
     * around.  If x is negative, it is wrapped up to ONE time.  Large
     * negatives are NOT handled and are considered an error.
     * @param x the horizontal coordinate starting at x=0 from the left
     * @param y the vertical coordinate starting at y=0 from the top
     * @return the block at location (x,y).  Returns null if chunk is missing.
     */
    public Block getBlock(long x, long y) {
        return getBlock(x, y, true);
    }
    
    /**
     * Returns the block at location (x,y) where each value is a block.
     * If x >= <code>WorldConstants.WORLD_WIDTH</code>, then it is wrapped
     * around.  If x is negative, it is wrapped up to ONE time.  Large
     * negatives are NOT handled and are considered an error.
     * @param x the horizontal coordinate starting at x=0 from the left
     * @param y the vertical coordinate starting at y=0 from the top
     * @param queryChunk True if we should query this chunk, false if we should
     * just assume it's filled with nothingness.
     * @return the block at location (x,y).  Returns null if chunk is missing.
     */
    public Block getBlock(long x, long y, boolean queryChunk) {
        // Get chunk x and y coordinates
        x = x >= 0 ? x % WorldConstants.WORLD_WIDTH : WorldConstants.WORLD_WIDTH + x;
        long cx = x / WorldConstants.WORLD_CHUNK_WIDTH;
        long cy = y / WorldConstants.WORLD_CHUNK_HEIGHT;
        int  bx = (int) (x % WorldConstants.WORLD_CHUNK_WIDTH);
        int  by = (int) (y % WorldConstants.WORLD_CHUNK_HEIGHT);
        
        long chunkId = ((long) (cy) << 32) | cx;
        
        synchronized (this) {
            if (!chunks.containsKey(chunkId) ) {
                if (!queryChunk) { // Don't add the chunk!!!  We don't want it to be queried.
                    return null;
                }
                chunks.put(chunkId, new TerrainChunk(chunkId));
            }
            return chunks.get(chunkId).getBlock(bx, by);
        }
    }
    
    /**
     * Render the blocks of the world.
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
        
        // Render the frame to the offscreen buffer
        int xBase = (int) (location.getX() - (x - (chunksAcross-1)/2) * WorldConstants.WORLD_CHUNK_WIDTH);
        int yBase = (int) (location.getY() - (y - (chunksHigh-1)/2) * WorldConstants.WORLD_CHUNK_HEIGHT);
        xBase *= WorldConstants.WORLD_BLOCK_WIDTH;
        yBase *= WorldConstants.WORLD_BLOCK_HEIGHT;
        xBase += location.getXOffset();
        yBase += location.getYOffset();
        
        // Subtract 1 to account for offsets
        --xBase;
        --yBase;

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

                synchronized (this) {
                    if (!chunks.containsKey(chunkId)) {
                        chunks.put(chunkId, new TerrainChunk(chunkId));
                    }
                    chunks.get(chunkId).render(container, game, g, xBase, yBase,
                            i, j, true);
                }
            }
        }
        // Reset our load balancer so that it can do its job on the next render loop.
        TerrainChunkLoadBalancer.getInstance().resetLoad();
    }

    /**
     * Assigns an array of BlockTypes to the chunk with the given <code>chunkId</code>.
     * If no chunk with <code>chunkId</code> exists, it will be created.
     * @param chunkBlocks Type of blocks to create
     */
    public void setChunk(long chunkId, Block[][] chunkBlocks) {
        synchronized (this) {
            if (!chunks.containsKey(chunkId)) {
                chunks.put(chunkId, new TerrainChunk(chunkId, chunkBlocks));
            } else {
                chunks.get(chunkId).load(chunkBlocks);
            }
        }
    }

    /**
     * Returns the location for complex collisions.  These are collisions in
     * which there is a change in more than one dimension (x and y).
     * 
     * NOTE:  This function does NOT work for non-complex max locations.  This
     *        function should only be called by 
     *        {@link #getMaxLocation(Location, Location, BoundLocation).
     * @param start The location to start at.
     * @param end The old end location.
     * @param collision The collision location.
     * @return The location right before the collision location.  Returns null if any parameter is null.
     */
    public static Location getComplexMaxLocation(Location start, Location end, BoundLocation collision) {
        // Guard
        if (start == null || end == null || collision == null) {
            return null;
        }
        // Assumptions
        collision.setXOffset(0);
        collision.setYOffset(0);
        
        // m = (y2 - y1) / (x2 - x1)
        double slope = (end.getRawY() - start.getRawY()) * 1.0 / (end.getRawX() - start.getRawX());
        // y = mx + b, solving for b
        double b = start.getRawY() - (slope * start.getRawX());
        
        /*
         * Now, we need to determine the sides we should check.  Because of the
         * checks we make, It's only possible to collide with a single side.
         */
        if (start.getRawX() < end.getRawX()) { // Check left side
            long x = collision.getRawX();
            long y = (long) (slope * x + b);
            // We collided!
            if (y >= collision.getRawY() && y <= collision.getRawY() + Location.BLOCK_GRANULARITY) {
                collision.setRawY(y);
                collision.setRawX(x - 1);
                return collision;
            }
        } else { // Check right side
            long x = collision.getRawX() + Location.BLOCK_GRANULARITY - 1;
            long y = (long) (slope * x + b);
            // We collided!
            if (y >= collision.getRawY() && y <= collision.getRawY() + Location.BLOCK_GRANULARITY) {
                collision.setRawY(y);
                collision.setRawX(x + 1);
                return collision;
            }
        }
        if (start.getRawY() < end.getRawY()) { // Check top side
            long y = collision.getRawY();
            long x = (long) ((y - b) / slope);
            // We collided!
            if (x >= collision.getRawX() && x <= collision.getRawX() + Location.BLOCK_GRANULARITY) {
                collision.setRawX(x);
                collision.setRawY(y - 1);
                return collision;
            }
        } else { // Check bottom side
            long y = collision.getRawY() + Location.BLOCK_GRANULARITY - 1;
            long x = (long) ((y - b) / slope);
            // We collided!
            if (x >= collision.getRawX() && x <= collision.getRawX() + Location.BLOCK_GRANULARITY) {
                collision.setRawX(x);
                collision.setRawY(y + 1);
                return collision;
            }
        }
        /*
         *  If we got here, then this block was NOT on our path.  This can 
         *  occur because Bresenham's line algorithm uses the CENTER of a block
         *  instead of the player's actual location / destination.
         */
        return collision;
    }
    
    /**
     * Get the location that is just before <code>end</code> based off of the
     * starting location.
     * TODO:  This function needs to be thought out better.  It's messing with 
     * {@link #collideWithBlock(Location, Location)}collideWithBlock.
     * @param start the location to start at
     * @param end the old end location
     * @param collision the new end location
     * @return the location right before the end location; returns null if any parameter is null
     */
    public Location getMaxLocation(Location start, Location end, BoundLocation collision) {
        // Guard
        if (start == null || end == null || collision == null) {
            return null;
        }
        
        // Special case for same block
        if (start.getX() == collision.getX() && start.getY() == collision.getY()) {
            return start;
        }
        
        boolean simpleMaxLocation = false; // true if this max location is a change in only one dimension (x or y)
        
        // Fix offsets for collision.
        if (start.getX() == end.getX() && start.getXOffset() == end.getXOffset()) {
            collision.setRawX(start.getRawX());
            simpleMaxLocation = true;
        }
        if (start.getY() == end.getY() && start.getYOffset() == end.getYOffset()) {
            collision.setRawY(start.getRawY());
            simpleMaxLocation = true;
        }
        
        if (simpleMaxLocation) {
            // Fix the x location
            if (collision.getBlockDeltaX(start) > 0) {
                collision.decrementX();
            } else if (collision.getBlockDeltaX(start) < 0){
                collision.moveRawRight(Location.BLOCK_GRANULARITY);
            }
            
            // Fix the y location
            if (collision.getBlockDeltaY(start) > 0) {
                collision.decrementY();
            } else if (collision.getBlockDeltaY(start) < 0) {
                collision.moveRawDown(Location.BLOCK_GRANULARITY);
            }
        } else {
            /*
             *  Special case:  Because Bresenham's line algorithm is using whole
             *  block coordinates (and not subpixel coordinates), we need to use
             *  a do-while loop to ensure that collision is not part of a block
             *  that has collision.
             */
            Block b;
            BoundLocation prevCollision;
            do {
                prevCollision = collision;
                collision = new BoundLocation(getComplexMaxLocation(start, end, collision));
                b = getBlock(collision.getX(), collision.getY());
            } while (b != null && b.isCollidable() && !collision.equals(prevCollision));
        }
        
        return collision;
    }
    
    /**
     * Returns the farthest distance before colliding with a block from start
     * to end.
     * Uses a Bresenham-based supercover line algorithm.
     * @param start the starting location (before moving)
     * @param end the ending location (after moving)
     * @return modified location; null if any parameter is null
     */
    public Location collideWithBlock(Location start, Location end) {
        // Guard
        if (start == null || end == null) {
            return null;
        }
        
        long x0 = start.getX();
        long y0 = start.getY();
        int blockDeltaX = Math.abs((int) start.getBlockDeltaX(end));
        int blockDeltaY = Math.abs((int) start.getBlockDeltaY(end));
        
        // Begin at the start
        long x = x0;
        long y = y0;
        
        // First point
        Block b = getBlock(x, y);
        if (b == null || b.isCollidable()) {
            return start; // we started at a block with collision...
        }
        
        // The deltax and deltay are used to calculate slope
        long deltax = end.getRawDeltaX(start);
        long deltay = end.getRawDeltaY(start);

        long xStep = deltax < 0 ? -1 : 1;
        long yStep = deltay < 0 ? -1 : 1;
        deltax *= xStep;
        deltay *= yStep;
        // Double the value for full precision
        deltax += deltax;
        deltay += deltay;
        
        boolean steep = deltax < deltay;
        long error = 0;
        long xError;
        long yError;
        if (xStep > 0) {
            xError = deltay * (Location.BLOCK_GRANULARITY - start.getRawXOffset()) / Location.BLOCK_GRANULARITY;
        } else {
            xError = deltay * start.getRawXOffset() / Location.BLOCK_GRANULARITY;
        }
        if (yStep < 0) {
            yError = deltax * (Location.BLOCK_GRANULARITY - start.getRawYOffset()) / Location.BLOCK_GRANULARITY;
        } else {
            yError = deltax * start.getRawYOffset() / Location.BLOCK_GRANULARITY;
        }

        long errorprev;
        if (!steep) {
            error = yError;
            error += xError - (deltay / 2);
            for (int i=0; i < blockDeltaX; ++i) {
                errorprev = error;
                x += xStep;
                error += deltay;
                if (error > deltax) {
                    y += yStep;
                    error -= deltax;
                    if (error + errorprev <= deltax) { // bottom square
                        b = getBlock(x, y - yStep);
                        if (b == null || b.isCollidable()) {
                            BoundLocation newStart = new BoundLocation(0, 0);
                            newStart.setRawX( (x * Location.BLOCK_GRANULARITY) - (xStep > 0 ? 1 : Location.BLOCK_GRANULARITY));
                            newStart.setRawY( (y - yStep) * Location.BLOCK_GRANULARITY + start.getRawXOffset() ); // TODO:  Fix this
                            return getMaxLocation(newStart, end, new BoundLocation(x, y - yStep));
                        }
                    }
                    if (error + errorprev >= deltax) { // left square
                        b = getBlock(x - xStep, y);
                        if (b == null || b.isCollidable()) {
                            BoundLocation newStart = new BoundLocation(0, 0);
                            newStart.setRawX( (x - xStep) * Location.BLOCK_GRANULARITY + start.getRawYOffset() ); // TODO:  Fix this
                            newStart.setRawY( (y * Location.BLOCK_GRANULARITY) - (yStep > 0 ? 1 : Location.BLOCK_GRANULARITY) );
                            return getMaxLocation(newStart, end, new BoundLocation(x - xStep, y));
                        }
                    }
                }
                
                b = getBlock(x, y);
                if (b == null || b.isCollidable()) {
                    return getMaxLocation(start, end, new BoundLocation(x, y));
                }
            }
        } else { // steep
            error = xError;
            error += yError - (deltax / 2);
            for (int i=0; i < blockDeltaY; ++i) {
                errorprev = error;
                y += yStep;
                error += deltax;
                if (error > deltay) {
                    x += xStep;
                    error -= deltay;
                    if (error + errorprev <= deltay) { // left
                        b = getBlock(x - xStep, y);
                        if (b == null || b.isCollidable()) { 
                            BoundLocation newStart = new BoundLocation(0, 0);
                            newStart.setRawX( (x - xStep) * Location.BLOCK_GRANULARITY + start.getRawYOffset() ); // TODO:  Fix this
                            newStart.setRawY( (y * Location.BLOCK_GRANULARITY) - (yStep > 0 ? 1 : Location.BLOCK_GRANULARITY) );
                            return getMaxLocation(newStart, end, new BoundLocation(x - xStep, y));
                        }
                    }
                    if (error + errorprev >= deltay) { // up
                        b = getBlock(x, y - yStep);
                        if (b == null || b.isCollidable()) {
                            BoundLocation newStart = new BoundLocation(0, 0);
                            newStart.setRawX( (x * Location.BLOCK_GRANULARITY) - (xStep > 0 ? 1 : Location.BLOCK_GRANULARITY));
                            newStart.setRawY( (y - yStep) * Location.BLOCK_GRANULARITY + start.getRawXOffset() ); // TODO:  Fix this
                            return getMaxLocation(newStart, end, new BoundLocation(x, y - yStep));
                        }
                    }
                }
                b = getBlock(x, y);
                if (b == null || b.isCollidable()) {
                    return getMaxLocation(start, end, new BoundLocation(x, y));
                }
            }
        }
        
        return end; // no collision
    }
    
    // TESTING ONLY
    public Location collideWithBlock(Location start, Location end, boolean color) {
        // Guard
        if (start == null || end == null) {
            return null;
        }
        
        long x0 = start.getX();
        long y0 = start.getY();
        long x1 = end.getX();
        long y1 = end.getY();
        
        // Begin at the start
        long x = x0;
        long y = y0;
        
        // First point
        Block b = getBlock(x, y).getCopy();
        setBlock(x, y, new AirGreenBlock());
        if (b == null || b.isCollidable()) {
            setBlock(x, y, new AirRedBlock());
            return start; // we started at a block with collision...
        }
        
        // The deltax and deltay are used to calculate slope
        long deltax = end.getRawDeltaX(start);
        long deltay = end.getRawDeltaY(start);

        long xStep = deltax < 0 ? -1 : 1;
        long yStep = deltay < 0 ? -1 : 1;
        deltax *= xStep;
        deltay *= yStep;
        // Double the value for full precision
        deltax += deltax;
        deltay += deltay;
        
        boolean steep = deltax < deltay;
        long error = 0;
        long xError;
        long yError;
        if (xStep > 0) {
            xError = deltay * (Location.BLOCK_GRANULARITY - start.getRawXOffset()) / Location.BLOCK_GRANULARITY;
        } else {
            xError = deltay * start.getRawXOffset() / Location.BLOCK_GRANULARITY;
        }
        if (yStep < 0) {
            yError = deltax * (Location.BLOCK_GRANULARITY - start.getRawYOffset()) / Location.BLOCK_GRANULARITY;
        } else {
            yError = deltax * start.getRawYOffset() / Location.BLOCK_GRANULARITY;
        }
        //error += (steep ? yError - deltax / 2: xError - deltay / 2);

        long errorprev;
        if (!steep) {
            error = yError;
            error += xError - (deltay / 2);
            for (int i=0; i < Math.abs(x0 - x1); ++i) {
                errorprev = error;
                x += xStep;
                error += deltay;
                if (error > deltax) {
                    y += yStep;
                    error -= deltax;
                    if (error + errorprev <= deltax) { // bottom square
                        b = getBlock(x, y - yStep).getCopy();
                        setBlock(x, y - yStep, new AirGreenBlock());
                        if (b == null || b.isCollidable()) {
                            setBlock(x, y - yStep, new AirRedBlock());
                            if (x == x0) { // same block
                                return start;
                            }
                            return getMaxLocation(start, end, new BoundLocation(x, y - yStep));
                        }
                    }
                    if (error + errorprev >= deltax) { // left square
                        b = getBlock(x - xStep, y).getCopy();
                        setBlock(x - xStep, y, new AirGreenBlock());
                        if (b == null || b.isCollidable()) {
                            setBlock(x - xStep, y, new AirRedBlock());
                            if (x == x0) { // same block
                                return start;
                            }
                            return getMaxLocation(start, end, new BoundLocation(x - xStep, y));
                        }
                    }
                }
                
                b = getBlock(x, y).getCopy();
                setBlock(x, y, new AirGreenBlock());
                if (b == null || b.isCollidable()) {
                    setBlock(x, y, new AirRedBlock());
                    return start; // we started at a block with collision...
                }
            }
        } else { // steep
            error = xError;
            error += yError - (deltax / 2);
            for (int i=0; i < Math.abs(y0 - y1); ++i) {
                errorprev = error;
                y += yStep;
                error += deltax;
                if (error > deltay) {
                    x += xStep;
                    error -= deltay;
                    if (error + errorprev <= deltay) {
                        b = getBlock(x - xStep, y).getCopy();
                        setBlock(x - xStep, y, new AirGreenBlock());
                        if (b == null || b.isCollidable()) {
                            setBlock(x - xStep, y, new AirRedBlock());
                            if (x == x0) { // same block
                                return start;
                            }
                            return getMaxLocation(start, end, new BoundLocation(x - xStep, y));
                        }
                    }
                    if (error + errorprev >= deltay) {
                        b = getBlock(x, y - yStep).getCopy();
                        setBlock(x, y - yStep, new AirGreenBlock());
                        if (b == null || b.isCollidable()) {
                            setBlock(x, y - yStep, new AirRedBlock());
                            if (x == x0) { // same block
                                return start;
                            }
                            return getMaxLocation(start, end, new BoundLocation(x, y - yStep));
                        }
                    }
                }
                b = getBlock(x, y).getCopy();
                setBlock(x, y, new AirGreenBlock());
                if (b == null || b.isCollidable()) {
                    setBlock(x, y, new AirRedBlock());
                    return start; // we started at a block with collision...
                }
            }
        }
        
        return end; // no collision
    }

    /**
     * Removes all cached chunks from the terrain session.  They will need to
     * be queried from the server again.
     */
    public void clear() {
        synchronized (this) {
            chunks.clear();
        }
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
        
        // Only get the chunk if it exists
        synchronized (this) {
            if (chunks.containsKey(chunkId)) {
                chunks.get(chunkId).setBlock(bx, by, block);
            }
        }
    }

    /**
     * Tells the chunk at (<code>x</code>, <code>y</code>) that it needs to
     * refresh.  This is used when a surrounding chunk's blocks change and
     * a shadow recalculation is needed.
     * @param x
     * @param y
     */
    public void refreshChunk(long x, long y) {
        synchronized (this) {
            long chunkId = ((long) (y) << 32) | x;
            if (chunks.containsKey(chunkId)) {
                chunks.get(chunkId).setNeedsRefresh(true);
            }
        }
    }
}
