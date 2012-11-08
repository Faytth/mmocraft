package org.unallied.mmocraft.client;

import org.newdawn.slick.SpriteSheet;

/**
 * A node that bundles a sprite sheet with its width, height, and resource.
 * This is important because it lets the server know a spritesheet's width,
 * height, and resource even if the spritesheet failed to be created
 * (because of a lack of an OpenGL context).
 * @author Alexandria
 *
 */
public class SpriteSheetNode {
    protected SpriteSheet sheet;
    protected int width;
    protected int height;
    protected String resource;
    
    /**
     * Creates a sprite sheet node.  A sprite sheet node combines a sprite sheet
     * with its width, height, and resource information.
     * @param sheet A sprite sheet
     * @param width The width of the sprite sheet
     * @param height The height of the sprite sheet
     * @param resource The resource name of the sprite sheet
     */
    public SpriteSheetNode(SpriteSheet sheet, int width, int height, String resource) {
        this.sheet = sheet;
        this.width = width;
        this.height = height;
        this.resource = resource;
    }
    
    /**
     * Returns the sprite sheet associated with this sprite sheet node.  The
     * sprite sheet returned can be null, in which case it's best to determine
     * the sprite sheet through the resource, width, and height.
     * @return spriteSheet
     */
    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
    
    /**
     * Returns the width of a single frame in the sprite sheet.  The width of
     * the sprite sheet is some multiple of this.
     * @return
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns the height of a single frame in the sprite sheet.  The height of
     * the sprite sheet is some multiple of this.
     * @return height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns the resource name associated with this sprite sheet.
     * @return resourceName
     */
    public String getResourceName() {
        return resource;
    }
}