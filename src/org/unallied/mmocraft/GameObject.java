package org.unallied.mmocraft;

import java.io.Serializable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Contains methods that all objects in the game world should have.
 * An object is defined as anything that can be on screen that is not
 * a part of the block world, such as a monster, player, bed, chair,
 * and so on.
 * @author Faythless
 *
 */
public abstract class GameObject implements Serializable, Collidable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1558629546822943114L;
    
    /**
     *  The location of the object (most bottom-left corner of the object)
     */
    protected BoundLocation location;
    
    public GameObject() {
        
    }
    
    public GameObject(GameObject other) {
        this.location = other.location;
    }
    
    /**
     * Returns the location of the object in the game world.
     * @return current location of the object
     */
    public BoundLocation getLocation() {
        return location;
    }
    
    /**
     * Sets the current location of this object in the game world.
     * @param location location of the object
     */
    public void setLocation(BoundLocation location) {
        this.location = location;
    }

    @Override
    public boolean isCollidable() {
        // Almost all game objects are collidable.  Override where necessary
        return true;
    }
    
    /**
     * Renders the game object on the screen.  This should be done before the UI
     * but after pretty much everything else.
     * @param container
     * @param game
     * @param g
     * @param camera 
     */
    public abstract void render(GameContainer container, StateBasedGame game, Graphics g, BoundLocation camera);
}
