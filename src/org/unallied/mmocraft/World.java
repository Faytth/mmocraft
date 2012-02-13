package org.unallied.mmocraft;

/**
 * Contains statistics for the game world, such as total width and height.
 * @author Ryokko
 *
 */
public class World {
    
    // The width of the entire game world
    private long width = 0;
    
    // The height of the entire game world
    private long height = 0;
    
    /**
     * Returns the width of the entire game world
     * @return the game world's width
     */
    public long getWidth() {
        return width;
    }
    
    /**
     * Returns the height of the entire game world
     * @return the game world's height
     */
    public long getHeight() {
        return height;
    }

    public Terrain getTerrain() {
        // TODO Auto-generated method stub
        return null;
    }

}
