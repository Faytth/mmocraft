package org.unallied.mmocraft;

import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

/**
 * Contains a list of all collisions for animations.  Use this when you need
 * to get a collision blob for a specific animation.
 * 
 * @author Alexandria
 *
 */
public enum Collision {
    SWORD_BACK_AIR(SpriteID.SWORD_BACK_AIR_ARC),
    SWORD_DASH_ATTACK(SpriteID.SWORD_DASH_ATTACK_ARC),
    SWORD_DOWN_SMASH(SpriteID.SWORD_DOWN_SMASH_ARC),
    SWORD_FLOOR_ATTACK(SpriteID.SWORD_FLOOR_ATTACK_ARC),
    SWORD_FRONT_AIR(SpriteID.SWORD_FRONT_AIR_ARC),
    SWORD_HORIZONTAL_ATTACK(SpriteID.SWORD_HORIZONTAL_ATTACK_ARC),
    SWORD_HORIZONTAL_ATTACK_2(SpriteID.SWORD_HORIZONTAL_ATTACK_2_ARC),
    SWORD_NEUTRAL_AIR(SpriteID.SWORD_NEUTRAL_AIR_ARC),
    SWORD_SIDE_SMASH(SpriteID.SWORD_SIDE_SMASH_ARC),
    SWORD_UP_AIR(SpriteID.SWORD_UP_AIR_ARC),
    SWORD_UP_SMASH(SpriteID.SWORD_UP_SMASH_ARC);
    private CollisionBlob[] collisionArc;
    
    Collision(SpriteID spriteID) {
        this.collisionArc = CollisionBlob.generateCollisionArc(SpriteHandler.getInstance().get(spriteID.toString()));
    }
    
    /**
     * Retrieves the collision arc from the given collision.
     * @return collisionArc
     */
    public CollisionBlob[] getCollisionArc() {
        return collisionArc;
    }
}
