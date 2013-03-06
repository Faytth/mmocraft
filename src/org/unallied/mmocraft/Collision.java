package org.unallied.mmocraft;

import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteSheetNode;

/**
 * Contains a list of all collisions for animations.  Use this when you need
 * to get a collision blob for a specific animation.
 * 
 * @author Alexandria
 *
 */
public class Collision {
    private CollisionBlob[] collisionArc = null;
    
    private String spriteID;
    
    /**
     * The collision class uses deferred loading of the collision arc.  This is
     * needed because the class is created when the sprite handler is initialized,
     * so it's not necessarily ready to be accessed.
     * @param spriteID
     */
    public Collision(String spriteID) {
        this.spriteID = spriteID;
    }
    
    /**
     * Retrieves the collision arc from the given collision.
     * @return collisionArc
     */
    public CollisionBlob[] getCollisionArc() {
        if (collisionArc == null) { // Deferred loading
            SpriteSheetNode node = SpriteHandler.getInstance().getNode(spriteID);
            SpriteSheet spriteSheet = node.getSpriteSheet();
            if (spriteSheet != null) {
                this.collisionArc = CollisionBlob.generateCollisionArc(spriteSheet);
            } else {
                this.collisionArc = CollisionBlob.generateCollisionArc(node);
            }
        }
        return collisionArc;
    }
}
