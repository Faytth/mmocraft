package org.unallied.mmocraft.client;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Stores all spritesheets.  These spritesheets are used for animations.
 * @author Faythless
 *
 */
public class SpriteHandler {
    
    private Map<String, SpriteSheet> sprites = new HashMap<String, SpriteSheet>();
    
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
     */
    private void tryPut(String id, String resourceName, int width, int height) {
        SpriteSheet spriteSheet = null;
        
        try {
            spriteSheet = new SpriteSheet(resourceName, width, height);
        } catch (Throwable t) {
            spriteSheet = null;
        }
        sprites.put(id, spriteSheet);
    }
    
    private void init() {
        try {
            // Sword
            tryPut( SpriteID.SWORD_FALL.toString(),
                    "resources/animations/sword/fall.png",44,76);
            tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK.toString(),
                    "resources/animations/sword/horizontal_attack.png",150,100);
            tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_ARC.toString(),
                    "resources/animations/sword/horizontal_attack_arc.png",150,100);
            tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_2.toString(),
                    "resources/animations/sword/horizontal_attack_2.png",150,100);
            tryPut( SpriteID.SWORD_HORIZONTAL_ATTACK_2_ARC.toString(),
                    "resources/animations/sword/horizontal_attack_2_arc.png",150,100);
            tryPut( SpriteID.SWORD_IDLE.toString(), 
                    "resources/animations/sword/idle.png",60,60);
            tryPut( SpriteID.SWORD_JUMP.toString(),
                    "resources/animations/sword/jump.png",59,64);
            tryPut( SpriteID.SWORD_ROLL.toString(),
                    "resources/animations/sword/roll.png",63,66);
            tryPut( SpriteID.SWORD_RUN.toString(),
                    "resources/animations/sword/run.png",60,60);
            tryPut( SpriteID.SWORD_SHIELD.toString(),
                    "resources/animations/sword/shield.png",64,54);
            tryPut( SpriteID.SWORD_WALK.toString(),
                    "resources/animations/sword/walk.png",60,50);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the sprite sheet containing the given key.  If no spritesheet
     * is found, then it is queried from the server.
     * @param key the string that references this sprite sheet
     * @return sprite sheet if key exists, else null
     */
    public SpriteSheet get(String key) {
        if (sprites.containsKey(key)) {
            return sprites.get(key);
        }
        // Doesn't exist, so query it
        
        return null;
    }
}
