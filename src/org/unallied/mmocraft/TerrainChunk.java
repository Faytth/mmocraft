package org.unallied.mmocraft;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.blocks.DirtBlock;
import org.unallied.mmocraft.blocks.GrassBlock;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.ImagePool;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.sessions.TerrainSession;

public class TerrainChunk {
    
    private Block[][] blocks = null;
    
    /**
     * The number of times required to flush before changes appear.  This is
     * due to double-buffering.
     */
    private static final int FLUSH_COUNT = 2;
    
    /**
     *  > 0 if this image needs to be flushed to the TerrainSession.
     *  Every time the image is flushed, this number is reduced by 1.  The
     *  reason for this is that Slick is double-buffered.  This means the image
     *  needs to be flushed two times for it to appear properly.
     */
    private int dirty = 0;
    
    /**
     *  True if the image needs to be refreshed before being drawn.
     *  This should be set to true after changing the blocks.
     */
    private volatile boolean needsRefresh = false;
    
    private long chunkId;
    
    /**
     * A light matrix that keeps track of the levels of light across this chunk.
     * As levels of light change (block added / removed), this matrix should be
     * updated to reflect those changes.  This matrix can greatly improve the
     * performance of {@link #getLightDistance(int, int)}.
     */
    private float[][] lightMatrix = new float[WorldConstants.WORLD_CHUNK_WIDTH][WorldConstants.WORLD_CHUNK_HEIGHT];
    
    /**
     * A TerrainChunk knows its location given a chunkId.  Based on this, we
     * can query the server to request a specific chunk.
     * @param chunkId (y << 32) | x
     */
    public TerrainChunk(long chunkId) {
        init();
        this.chunkId = chunkId;
        // Send packet to server requesting an update on this chunk
        Game.getInstance().getClient().announce(PacketCreator.getChunk(chunkId));
    }
    
    public TerrainChunk(long chunkId, Block[][] chunkBlocks) {
        init();
        this.chunkId = chunkId;
        this.blocks = chunkBlocks;
        needsRefresh = true;
        dirty = FLUSH_COUNT; // double-buffered, so flush twice
    }

    private void init() {
        // Initialize light matrix to no shadow
        for (int i=0; i < lightMatrix.length; ++i) {
            for (int j=0; j < lightMatrix.length; ++j) {
                lightMatrix[i][j] = 0;
            }
        }
    }
    
    /**
     * 
     * @param container
     * @param game
     * @param g
     * @param x The x coordinate of the buffer for drawing this chunk.
     * Each x is an offset of x*WorldConstants.WORLD_CHUNK_WIDTH *
     * WorldConstants.WORLD_BLOCK_WIDTH
     * @param y The y coordinate of the buffer for drawing this chunk.
     * Each y is an offset of y*WorldConstants.WORLD_CHUNK_HEIGHT *
     * WorldConstants.WORLD_BLOCK_HEIGHT
     * @param forceUpdate If true, forces this chunk to draw itself
     * to the graphics.  If false, the chunk will determine if it
     * needs to update itself or not.
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g,
            float xBase, float yBase, int x, int y, boolean forceUpdate) {        
        int w = WorldConstants.WORLD_BLOCK_WIDTH * WorldConstants.WORLD_CHUNK_WIDTH;
        int h = WorldConstants.WORLD_BLOCK_HEIGHT * WorldConstants.WORLD_CHUNK_HEIGHT;
        
        float xOffset = x * w - xBase;
        float yOffset = y * h - yBase;
        
        synchronized (this) {
            if (blocks != null) {
                Image buffer = ImagePool.getInstance().getImage(this, w, h);

                // See if we have to initialize our buffer
                if (ImagePool.getInstance().needsRefresh() || needsRefresh) {
                    if (TerrainChunkLoadBalancer.getInstance().canLoad()) {
                        refresh(buffer);
                    } else {
                        needsRefresh = true;
                    }
                }

                if ((forceUpdate || dirty > 0) && !(buffer == null)) {
                    // Draw our chunk!
                    g.drawImage(buffer,  xOffset, yOffset, xOffset + w, yOffset + h, 0, 0, w, h);
                    --dirty;
                }
            } else { // Not loaded, so just draw a black rectangle
                if (forceUpdate || dirty > 0) {
                    g.fill(new Rectangle(xOffset, yOffset, w, h), 
                            new GradientFill(0, 0, Color.black,
                                    w, h, Color.black));
                    --dirty;
                }
            }
        }
    }
    
    /**
     * A recursive helper for the relaxation algorithm.
     * Note:  There is an optimization available.  We don't need to check the direction
     * that we're coming from.
     * @param superLightMatrix
     * @param x
     * @param y
     */
    private void relaxLightMatrixHelper(float [][]superLightMatrix, int x, int y) {
        // Correct top
        if (y > 0 && superLightMatrix[x][y-1] > superLightMatrix[x][y] + 1) {
            superLightMatrix[x][y-1] = superLightMatrix[x][y] + 1;
            relaxLightMatrixHelper(superLightMatrix, x, y-1);
        }
        
        // Correct bottom
        if (y + 1 < superLightMatrix[x].length && superLightMatrix[x][y+1] > superLightMatrix[x][y] + 1) {
            superLightMatrix[x][y+1] = superLightMatrix[x][y] + 1;
            relaxLightMatrixHelper(superLightMatrix, x, y+1);
        }
        
        // Correct left
        if (x > 0 && superLightMatrix[x-1][y] > superLightMatrix[x][y] + 1) {
            superLightMatrix[x-1][y] = superLightMatrix[x][y] + 1;
            relaxLightMatrixHelper(superLightMatrix, x-1, y);
        }
    }
    
    private void relaxLightMatrix(float [][]superLightMatrix, int x, int y) {
        // Do special case
        for (int i=y; i > 0 && superLightMatrix[x][i-1] > superLightMatrix[x][i] + 1; --i) {
            superLightMatrix[x][i-1] = superLightMatrix[x][i] + 1;
        }
        // Recursively correct top, bottom, and left after moving left
        if (x > 0 && superLightMatrix[x-1][y] > superLightMatrix[x][y] + 1) {
            superLightMatrix[x-1][y] = superLightMatrix[x][y] + 1;
            relaxLightMatrixHelper(superLightMatrix, x-1, y);
        }
    }
    
    /**
     * Populates the light matrix with my own algorithm.  (Does it have a name?)
     * Is this the best way to do it?
     */
    private void populateLightMatrix() {
        final int depth = WorldConstants.WORLD_BLOCK_SHADOW_DEPTH; // The max depth that light penetrates.
        final int chunkWidth = WorldConstants.WORLD_CHUNK_WIDTH;
        final int chunkHeight = WorldConstants.WORLD_CHUNK_HEIGHT;
        float [][]superLightMatrix = new float[chunkWidth + depth * 2 - 2][chunkHeight + depth * 2 - 2];
        TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
        final int baseX = getX() * WorldConstants.WORLD_CHUNK_WIDTH;
        final int baseY = getY() * WorldConstants.WORLD_CHUNK_HEIGHT;
        
        for (int x=0; x < superLightMatrix.length; ++x) {
            for (int y=0; y < superLightMatrix[x].length; ++y) {
                Block b = ts.getBlock(new BoundLocation(baseX + x - depth + 1, baseY + y - depth + 1), false);
                if (b != null && !b.isCollidable()) { // Light source
                    relaxLightMatrix(superLightMatrix, x, y);
                } else { // Not a light source
                    superLightMatrix[x][y] = y > 0 && superLightMatrix[x][y-1] < depth-1 ? 
                            superLightMatrix[x][y-1] + 1 : depth;
                    if (x > 0 && superLightMatrix[x-1][y] < superLightMatrix[x][y]-1) {
                        superLightMatrix[x][y] = superLightMatrix[x-1][y] + 1;
                    }
                }
            }
        }
        // Copy from the super matrix
        for (int i=0; i < lightMatrix.length; ++i) {
            for (int j=0; j < lightMatrix.length; ++j) {
                lightMatrix[i][j] = superLightMatrix[i + depth - 1][j + depth - 1];
            }
        }
    }
    
    /**
     * Retrieves the distance that light is from this block.  The returned
     * value will be between 0 and {@link WorldConstants#WORLD_BLOCK_SHADOW_DEPTH}
     * (inclusive).
     * TODO:  As of now, this uses a naive approach.  A much faster method is
     * to store a byte matrix of the light level and update it as blocks are
     * added / removed.
     * @param x The x coordinate of this block, where left block
     * and each block to the right increases x by 1.
     * @param y The y coordinate of this block, where 0 is the top block and
     * each block down increases y by 1.
     * @return lightDistance
     */
    private float getLightDistance(int x, int y) {
        // Keep this code around.  Maybe we'll use it for a "more detailed shadow level."
/*        final int depth = WorldConstants.WORLD_BLOCK_SHADOW_DEPTH;
        float result = depth;
        
        if (lightMatrix == null) {
            for (int i = -depth; i < depth; ++i) {
                for (int j = -depth; j < depth; ++j) {
                    Block b = Game.getInstance().getClient().getTerrainSession().getBlock(
                            new BoundLocation(
                                    getX() * WorldConstants.WORLD_CHUNK_WIDTH + x + i, 
                                    getY() * WorldConstants.WORLD_CHUNK_HEIGHT + y + j), false);
                    if (!b.isCollidable()) {
                        float dist = (float) Math.hypot(i,  j);
                        if (dist < result) {
                            result = dist;
                        }
                    }
                }
            }
        } else {
            result = lightMatrix[x][y];
        }*/
        
        return lightMatrix[x][y];
    }
    
    /**
     * Returns the % of light that can reach this block, from 0 to 1 (inclusive).
     * @param x The x coordinate of this block, where left block
     * and each block to the right increases x by 1.
     * @param y The y coordinate of this block, where 0 is the top block and
     * each block down increases y by 1.
     * @return
     */
    private float getLightPercent(int x, int y) {
        return 1f - ((getLightDistance(x, y) / WorldConstants.WORLD_BLOCK_SHADOW_DEPTH)) * WorldConstants.WORLD_BLOCK_MAX_SHADOW;
    }
    
    /**
     * Refreshes the buffer with the latest information from the blocks.
     * This should be called every time the chunk changes.
     * @param image The image to refresh onto
     */
    public void refresh(Image image) {
        if (image != null && blocks != null) {
            try {
                if (WorldConstants.WORLD_SHADOWS_ENABLED) {
                    populateLightMatrix();
                }
                Graphics g = image.getGraphics();
                // Render background
                // 19 is a magic number which is the "average" chunk in which land appears.
                if (getY() < 19) {
                    g.drawImage(ImageHandler.getInstance().getImage(ImageID.BACKGROUND_SKY.toString()), 0, 0);
                } else if (getY() == 19) {
                    g.drawImage(ImageHandler.getInstance().getImage(ImageID.BACKGROUND_SKY_DIRT_INTERSECT.toString()), 0, 0);
                } else {
                    g.drawImage(ImageHandler.getInstance().getImage(ImageID.BACKGROUND_DIRT.toString()), 0, 0);
                }
                // Render blocks
                for (int xPos=0; xPos < WorldConstants.WORLD_CHUNK_WIDTH; ++xPos) {
                    for (int yPos=0; yPos < WorldConstants.WORLD_CHUNK_HEIGHT; ++yPos) {
                        Block b = blocks[xPos][yPos];
                        Block aboveBlock = Game.getInstance().getClient().getTerrainSession().getBlock(
                                xPos + getX() * WorldConstants.WORLD_CHUNK_WIDTH, 
                                yPos + getY() * WorldConstants.WORLD_CHUNK_HEIGHT - 1);
                        if (b != null) {
                            Color filter = new Color(255, 255, 255);
                            
                            // Render shadows
                            if (WorldConstants.WORLD_SHADOWS_ENABLED && b.isCollidable()) {
                                float lightPercent = getLightPercent(xPos, yPos);
                                filter.r *= lightPercent;
                                filter.g *= lightPercent;
                                filter.b *= lightPercent;
                            }
                            
                            // These magic numbers are determined experimentally.  It might be a good idea to make them constants.
                            if ((getY() < 19 || (getY() == 19 && yPos < 47)) && (b instanceof DirtBlock) && aboveBlock != null && !aboveBlock.isCollidable()) {
                                // We should render grass instead of the dirt
                                // TODO:  Instead of using this kludge, implement REAL grass blocks.
                                new GrassBlock().render(g, xPos * WorldConstants.WORLD_BLOCK_WIDTH, yPos * WorldConstants.WORLD_BLOCK_HEIGHT, filter);
                            } else {
                                int blockX = xPos * WorldConstants.WORLD_BLOCK_WIDTH;
                                int blockY = yPos * WorldConstants.WORLD_BLOCK_HEIGHT;
                                b.render(g, blockX, blockY, filter);
                            }
                        }
                    }
                }
                g.flush(); // display the changes
                dirty = FLUSH_COUNT;
                needsRefresh = false;
            } catch (Throwable t) {
                dirty = 0;
                needsRefresh = true;
            }
        }
    }
    
    /**
     * Loads the blocks in this chunk from an array.  This array corresponds
     * to the BlockType enumeration.
     * @param chunkBlocks an array of blocks going across and then down.
     * Size of the array should be <code>WorldConstants.WORLD_CHUNK_WIDTH</code>
     * by <code>WorldConstants.WORLD_CHUNK_HEIGHT</code>
     */
    public void load(Block[][] chunkBlocks) {
        synchronized (this) {
            blocks = chunkBlocks;
            needsRefresh = true;
            dirty = FLUSH_COUNT;
            
            // We need to tell our surrounding chunks that they need to refresh as well
            // if shadows are enabled
            if (WorldConstants.WORLD_SHADOWS_ENABLED) {
                TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
                for (long i = -1; i <= 1; ++i) {
                    for (long j = -1; j <= 1; ++j) {
                        if (!(i == 0 && j == 0)) { // Don't tell our current chunk to refresh
                            // If we can refresh the chunk,t hen do so
                            if (getX() + i >= 0 && getX() + i < WorldConstants.WORLD_CHUNKS_WIDE &&
                                    getY() + j >= 0 && getY() + j < WorldConstants.WORLD_CHUNKS_TALL) {
                                ts.refreshChunk(getX() + i, getY() + j);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the block at location (x,y), which is the offset from the
     * top-left corner of this chunk.
     * @param x x coordinate
     * @param y y coordinate
     * @return the block at (x,y) or null if the block doesn't exist.
     */
    public Block getBlock(int x, int y) {
        try {
            synchronized (this) {
                if (blocks != null) {
                    return blocks[x][y];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        
        return null;
    }

    /**
     * Sets a block to the given block.  Note that this will not create a block
     * array if one does not yet exist.
     * @param bx The 0-based index of the block in this chunk to set.
     * @param by The 0-based index of the block in this chunk to set.
     * @param block The new block.
     */
    public void setBlock(int bx, int by, Block block) {
        // Guard
        if (blocks == null || block == null) {
            return;
        }
        
        synchronized (this) {
            boolean needsShadowRefresh = WorldConstants.WORLD_SHADOWS_ENABLED && 
                    (blocks[bx][by].isCollidable() != block.isCollidable());
            blocks[bx][by] = block;
            dirty = 0;
            needsRefresh = true;
            
            // We need to tell our surrounding chunks that they need to refresh as well
            // if shadows are enabled
            if (needsShadowRefresh) {
                TerrainSession ts = Game.getInstance().getClient().getTerrainSession();
                final int depth = WorldConstants.WORLD_BLOCK_SHADOW_DEPTH;
                final int cWidth = WorldConstants.WORLD_CHUNK_WIDTH;
                final int cHeight = WorldConstants.WORLD_CHUNK_HEIGHT;
                for (long i = (bx < depth - 1 ? -1 : 0); i <= ((cWidth - bx - 1) < depth -1 ? 1 : 0); ++i) {
                    for (long j = (by < depth - 1 ? -1 : 0); j <= ((cHeight - by - 1) < depth - 1 ? 1 : 0); ++j) {
                        if (!(i == 0 && j == 0)) { // Don't tell our current chunk to refresh
                            // If we can refresh the chunk, then do so
                            long cx = getX() + i;
                            cx = cx < 0 ? cx + WorldConstants.WORLD_CHUNKS_WIDE : cx;
                            cx = cx >= WorldConstants.WORLD_CHUNKS_WIDE ? cx - WorldConstants.WORLD_CHUNKS_WIDE : cx;
                            long cy = getY() + j;
                            if (cy >= 0 && cy < WorldConstants.WORLD_CHUNKS_TALL) {
                                ts.refreshChunk(cx, cy);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Retrieves the chunk ID for this chunk.
     * @return chunkId
     */
    public long getId() {
        return chunkId;
    }
    
    /**
     * Retrieves the x coordinate of this chunk ID.  A chunk has a width of 1.
     * Coordinates start at the top left from (0,0).
     * @return x
     */
    public int getX() {
        return (int) (chunkId & 0x00000000FFFFFFFFL);
    }
    
    /**
     * Retrieves the y coordinate of this chunk ID.  A chunk has a height of 1.
     * Coordinates start from the top-left at (0,0).
     * @return y
     */
    public int getY() {
        return (int) ((chunkId >> 32) & 0x00000000FFFFFFFFL);
    }

    /**
     * Sets {@link #needsRefresh}, which tells the chunk whether it needs to
     * redraw itself.
     * @param needsRefresh
     */
    public void setNeedsRefresh(boolean needsRefresh) {
        this.needsRefresh = needsRefresh;
    }
}
