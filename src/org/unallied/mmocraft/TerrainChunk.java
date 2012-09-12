package org.unallied.mmocraft;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.blocks.*;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.ImagePool;
import org.unallied.mmocraft.constants.WorldConstants;
import org.unallied.mmocraft.net.PacketCreator;

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
     * A TerrainChunk knows its location given a chunkId.  Based on this, we
     * can query the server to request a specific chunk.
     * @param chunkId (y << 32) | x
     */
    public TerrainChunk(long chunkId) {

        this.chunkId = chunkId;
                
        // Send packet to server requesting an update on this chunk
        Game.getInstance().getClient().announce(PacketCreator.getChunk(chunkId));
    }
    
    public TerrainChunk(long chunkId, Block[][] chunkBlocks) {
        this.chunkId = chunkId;
        this.blocks = chunkBlocks;
        needsRefresh = true;
        dirty = FLUSH_COUNT; // double-buffered, so flush twice
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
		
        System.out.println(xBase + " " + yBase);
        
		synchronized (this) {
            if (blocks != null) {
                Image buffer = ImagePool.getInstance().getImage(this, w, h);

                // See if we have to initialize our buffer
                if (ImagePool.getInstance().needsRefresh() || needsRefresh) {
                    refresh(buffer);
                }

                if ((forceUpdate || dirty > 0) && !(buffer == null)) {
                    // Draw our chunk!
                    g.drawImage(buffer,  xOffset, yOffset, xOffset + w, yOffset + h, 0, 0, w, h);
                    
//                    g.flush();
                    --dirty;
                }
            } else { // Not loaded, so just draw a black rectangle
                if (forceUpdate || dirty > 0) {
                    g.fill(new Rectangle(xOffset, yOffset, w, h), 
                            new GradientFill(0, 0, Color.black,
                                    w, h, Color.black));
//                    g.flush();
                    --dirty;
                }
            }
        }
    }
    
    /**
     * Refreshes the buffer with the latest information from the blocks.
     * This should be called every time the chunk changes.
     * @param image The image to refresh onto
     */
    public void refresh(Image image) {
        
        if (image != null && blocks != null) {
            try {
                Graphics g = image.getGraphics();
                g.drawImage(ImageHandler.getInstance().getImage(ImageID.BACKGROUND_SKY.toString()), 0, 0);
                for (int xPos=0; xPos < WorldConstants.WORLD_CHUNK_WIDTH; ++xPos) {
                    for (int yPos=0; yPos < WorldConstants.WORLD_CHUNK_HEIGHT; ++yPos) {
                        Block b = blocks[xPos][yPos];
                        if (b != null) {
                            b.render(g, xPos * WorldConstants.WORLD_BLOCK_WIDTH, 
                                    yPos * WorldConstants.WORLD_BLOCK_HEIGHT);
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
}
