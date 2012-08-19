package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.Collidable;

/**
 * A parent class that all blocks inherit from.
 * @author Faythless
 *
 */
public abstract class Block implements Collidable {
    
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
    protected abstract BlockType getType();
    
    /**
     * 
     * @param g
     * @param x the x position (in pixels) to use when drawing this block.
     * Far left is 0 x.  (Screen coordinates)
     * @param y the y position (in pixels) to use when drawing this block.
     * Top is 0 y.  (Screen coordinates)
     */
    public void render(Graphics g, int x, int y) {
        g.drawImage(getImage(), x, y);
    }
    
    /**
     * Determines whether this block collides with objects, such as players.
     * @return true if it collides with other objects, else false.  Air is an
     * example of a non-collidable block.
     */
    public boolean isCollidable() {
        return true;
    }
}
