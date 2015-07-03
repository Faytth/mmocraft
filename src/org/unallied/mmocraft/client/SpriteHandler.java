package org.unallied.mmocraft.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.SpriteSheet;

/**
 * Stores all spritesheets.  These spritesheets are used for animations.
 * @author Faythless
 *
 */
public class SpriteHandler {
    
    private Map<String, SpriteSheetNode> sprites = 
            new ConcurrentHashMap<String, SpriteSheetNode>(8, 0.9f, 1);
    
    private SpriteHandler() {
        init();
    }
    
    private static class SpriteHandlerHolder {
        private static SpriteHandler instance = new SpriteHandler();
    }
    
    public static SpriteHandler getInstance() {
        return SpriteHandlerHolder.instance;
    }
    
    /**
     * Attempts to add <code>resourceName</code> to the sprites.  If it can't
     * make the SpriteSheet, it uses null.
     * @param id The ID of the sprite, such as SpriteID.SWORD_FALL
     * @param resourceName The file name to get the sprite from
     * @param width The width of the sprite
     * @param height The height of the sprite
     * @param horizontalOffset The horizontal offset used when rendering this sprite.
     * @param verticalOffset The vertical offset used when rendering this sprite.
     * @param duration The length in milliseconds of this sprite.
     * @param collision The collision string for this animation, or null if
     * this animation doesn't have any collisions.
     */
    private void tryPut(String id, String resourceName, int width, int height,
            int horizontalOffset, int verticalOffset, int duration, String collision) {
        // Guard
        if (id == null || resourceName == null) {
            return;
        }
        
        SpriteSheet spriteSheet = null;
        
        try {
            spriteSheet = new SpriteSheet(resourceName, width, height);
        } catch (Throwable t) {
            spriteSheet = null;
        }
        sprites.put(id, new SpriteSheetNode(spriteSheet, width, height, resourceName,
                horizontalOffset, verticalOffset, duration, collision));
    }
    
    /**
     * Adds all sword animations to the sprite handler.
     */
    private void putSwords() {
        // Sword
        tryPut( SpriteID.SWORD_AIR_DODGE.toString(),
                "resources/animations/sword/air_dodge.png", 59, 64, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_BACK_AIR.toString(),
                "resources/animations/sword/back_air.png", 138, 107, -1, -1, -1, SpriteID.SWORD_BACK_AIR_ARC.toString());
        tryPut( SpriteID.SWORD_BACK_AIR_ARC.toString(),
                "resources/animations/sword/back_air_arc.png", 138, 107, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_DASH_ATTACK.toString(),
                "resources/animations/sword/dash_attack.png", 85, 60, -1, -1, -1, SpriteID.SWORD_DASH_ATTACK_ARC.toString());
        tryPut( SpriteID.SWORD_DASH_ATTACK_ARC.toString(),
                "resources/animations/sword/dash_attack_arc.png", 85, 60, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_DOUBLE_JUMP.toString(),
                "resources/animations/sword/double_jump.png", 63, 66, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_DOWN_SMASH.toString(),
                "resources/animations/sword/down_smash.png", 70, 70, -1, -1, -1, SpriteID.SWORD_DOWN_SMASH_ARC.toString());
        tryPut( SpriteID.SWORD_DOWN_SMASH_ARC.toString(),
                "resources/animations/sword/down_smash_arc.png", 70, 70, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_FALL.toString(),
                "resources/animations/sword/fall.png",44,76, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_FLOOR_ATTACK.toString(),
                "resources/animations/sword/floor_attack.png", 70, 70, -1, -1, -1, SpriteID.SWORD_FLOOR_ATTACK_ARC.toString());
        tryPut( SpriteID.SWORD_FLOOR_ATTACK_ARC.toString(),
                "resources/animations/sword/floor_attack_arc.png", 70, 70, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_FRONT_AIR.toString(),
                "resources/animations/sword/front_air.png", 100, 100, -1, -1, -1, SpriteID.SWORD_FRONT_AIR_ARC.toString());
        tryPut( SpriteID.SWORD_FRONT_AIR_ARC.toString(), 
                "resources/animations/sword/front_air_arc.png", 100, 100, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_HELPLESS.toString(),
                "resources/animations/sword/helpless.png", 58, 37, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK.toString(),
                "resources/animations/sword/horizontal_attack.png",150,100, -1, -1, -1, SpriteID.SWORD_HORIZONTAL_ATTACK_ARC.toString());
        tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_ARC.toString(),
                "resources/animations/sword/horizontal_attack_arc.png",150,100, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_2.toString(),
                "resources/animations/sword/horizontal_attack_2.png",150,100, -1, -1, -1, SpriteID.SWORD_HORIZONTAL_ATTACK_2_ARC.toString());
        tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_2_ARC.toString(),
                "resources/animations/sword/horizontal_attack_2_arc.png",150,100, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_IDLE.toString(), 
                "resources/animations/sword/idle.png",60,60, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_JUMP.toString(),
                "resources/animations/sword/jump.png",59,64, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_NEUTRAL_AIR.toString(),
                "resources/animations/sword/neutral_air.png", 100, 100, -1, -1, -1, SpriteID.SWORD_NEUTRAL_AIR_ARC.toString());
        tryPut( SpriteID.SWORD_NEUTRAL_AIR_ARC.toString(),
                "resources/animations/sword/neutral_air_arc.png", 100, 100, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_ROLL.toString(),
                "resources/animations/sword/roll.png",63,66, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_RUN.toString(),
                "resources/animations/sword/run.png",60,60, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_SHIELD.toString(),
                "resources/animations/sword/shield.png",64,54, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_SIDE_SMASH.toString(),
                "resources/animations/sword/side_smash.png", 145, 113, -1, -1, -1, SpriteID.SWORD_SIDE_SMASH_ARC.toString());
        tryPut( SpriteID.SWORD_SIDE_SMASH_ARC.toString(),
                "resources/animations/sword/side_smash_arc.png", 145, 113, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_SIDESTEP.toString(),
                "resources/animations/sword/sidestep.png", 59, 64, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_UP_AIR.toString(),
                "resources/animations/sword/up_air.png", 119, 102, -1, -1, -1, SpriteID.SWORD_UP_AIR_ARC.toString());
        tryPut( SpriteID.SWORD_UP_AIR_ARC.toString(),
                "resources/animations/sword/up_air_arc.png", 119, 102, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_UP_SMASH.toString(),
                "resources/animations/sword/up_smash.png", 138, 107, -1, -1, -1, SpriteID.SWORD_UP_SMASH_ARC.toString());
        tryPut( SpriteID.SWORD_UP_SMASH_ARC.toString(),
                "resources/animations/sword/up_smash_arc.png", 138, 107, -1, -1, -1, null);
        tryPut( SpriteID.SWORD_WALK.toString(),
                "resources/animations/sword/walk.png",60,50, -1, -1, -1, null);
    }
    
    private void putMonsters() {
        tryPut( "resources/animations/monster/bunny/idle.png",
                "resources/animations/monster/bunny/idle.png", 30, 20, 0, 0, 1500, null);
        tryPut( "resources/animations/monster/bunny/walk.png",
                "resources/animations/monster/bunny/walk.png", 30, 33, 0, 13, 800, null);
        tryPut( "resources/animations/monster/bunny/attack.png",
                "resources/animations/monster/bunny/attack.png", 89, 24, 0, 4, 800,
                "resources/animations/monster/bunny/attack_arc.png");
        tryPut( "resources/animations/monster/bunny/attack_arc.png",
                "resources/animations/monster/bunny/attack_arc.png", 89, 24, 0, 4, 800, null);
        
        tryPut( "resources/animations/monster/book_worm/idle.png",
                "resources/animations/monster/book_worm/idle.png", 56, 21, 0, 0, 800, null);
        tryPut( "resources/animations/monster/book_worm/walk.png",
                "resources/animations/monster/book_worm/walk.png", 64, 32, 6, 11, 800, null);
        tryPut( "resources/animations/monster/book_worm/attack.png",
                "resources/animations/monster/book_worm/attack.png", 100, 70, 21, 22, 800,
                "resources/animations/monster/book_worm/attack_arc.png");
        tryPut( "resources/animations/monster/book_worm/attack_arc.png",
                "resources/animations/monster/book_worm/attack_arc.png", 100, 70, 21, 22, 800, null);
    }
    
    private void init() {
        try {
            putSwords();
            putMonsters();
        } catch (Throwable t) {
            
        }
    }
    
    /**
     * Returns the sprite sheet containing the given key.  If no spritesheet
     * is found, then it is queried from the server.
     * @param key the string that references this sprite sheet
     * @return spriteSheet if key exists, else null
     */
    public SpriteSheet get(String key) {
        // Guard
        if (key == null) {
            return null;
        }
        
        if (sprites.containsKey(key)) {
            return sprites.get(key).sheet;
        }
        // Doesn't exist, so query it
        
        return null;
    }
    
    /**
     * Returns a node containing the sprite sheet as well as the sprite sheet's
     * width, height, and resource name.  This is useful in the event that the
     * sprite sheet failed to load (if the sprite sheet is null).
     * @param key the string that references this sprite sheet
     * @return spriteSheetNode if key exists, else null
     */
    public SpriteSheetNode getNode(String key) {
        // Guard
        if (key == null) {
            return null;
        }
        
        if (sprites.containsKey(key)) {
            return sprites.get(key);
        }
        // Doesn't exist, so query it
        
        return null;
    }
}
