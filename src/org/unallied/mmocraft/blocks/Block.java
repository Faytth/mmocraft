package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.Collidable;
import org.unallied.mmocraft.items.ItemData;

/**
 * A parent class that all blocks inherit from.
 * @author Faythless
 *
 */
public abstract class Block implements Collidable {
    
	private static final long DEFAULT_BLOCK_MAXIMUM_HEALTH = 1;
	
	/** 
	 * The maximum health of the block. The block must take this amount of 
	 * damage before breaking.
	 */
	protected long maximumHealth = DEFAULT_BLOCK_MAXIMUM_HEALTH;
	
    /**
     * Implements the Template Method pattern for image
     * @return color
     */
    protected abstract Image getImage();
    
    /**
     * Implements the Template Method pattern for the type of block.  This is
     * used when we need a unique ID for the block type (for example, to
     * serialize across a network).
     * @return type of block
     */
    public abstract BlockType getType();
    
    /**
     * Renders this block, displaying its image at the location specified.
     * @param g The graphics context to render to.
     * @param x the x position (in pixels) to use when drawing this block.
     * Far left is 0 x.  (Screen coordinates)
     * @param y the y position (in pixels) to use when drawing this block.
     * Top is 0 y.  (Screen coordinates)
     */
    public void render(Graphics g, int x, int y) {
        g.drawImage(getImage(), x, y);
    }
    
    /**
     * Renders this block, displaying its image at the location specified.
     * @param g The graphics context to render to.
     * @param x the x position (in pixels) to use when drawing this block.
     * Far left is 0 x.  (Screen coordinates)
     * @param y the y position (in pixels) to use when drawing this block.
     * Top is 0 y.  (Screen coordinates)
     * @param filter The filter to apply to the image.  This is great for things
     * like shadows or fiery effects.  A filter is the MAXIMUM value of a
     * color that the image can have.  For example, a filter of 50,100,150,0.5f
     * can be up to 50% opaque with up to 50 red, 100 green, and 150 blue.
     */
    public void render(Graphics g, int x, int y, Color filter) {
        g.drawImage(getImage(), x, y, filter);
    }
    
    /**
     * Determines whether this block collides with objects, such as players.
     * @return true if it collides with other objects, else false.  Air is an
     * example of a non-collidable block.
     */
    public boolean isCollidable() {
        return true;
    }
    
    /**
     * Retrieves a copy of the block.
     * @return block
     */
    public abstract Block getCopy();
    
    /**
     * Gets the maximum health of the block type
     * @return
     */
    public long getMaximumHealth() {
    	return maximumHealth;
    }
    
    /**
     * Retrieves the item associated with this block.<br />
     * Returns null if there is no item associated with the block.
     * @return item
     */
    public abstract ItemData getItem();
}
