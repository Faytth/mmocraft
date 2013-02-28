package org.unallied.mmocraft;

import org.newdawn.slick.Input;

/**
 * An interface for controls.  Used by {@link Controls} and AI.
 * @author Alexandria
 *
 */
public interface ControlIntf {
    /**
     * Returns whether the player is attempting to move left.
     * @param input The input to check
     * @return movingLeft
     */
    public boolean isMovingLeft(Input input);
    
    /**
     * Returns whether the player is attempting to move right.
     * @param input The input to check
     * @return movingRight
     */
    public boolean isMovingRight(Input input);
    
    /**
     * Returns whether the player is attempting to move up.
     * @param input The input to check
     * @return movingUp
     */
    public boolean isMovingUp(Input input);
    
    /**
     * Returns whether the player is attempting to move down.
     * @param input The input to check
     * @return movingDown
     */
    public boolean isMovingDown(Input input);
    
    /**
     * Returns whether the player is attempting to perform a basic attack.
     * @param input The input to check
     * @return basicAttack
     */
    public boolean isBasicAttack(Input input);
    
    /**
     * Returns whether the player is attempting to raise their shield.
     * @param input The input to check
     * @return shielding
     */
    public boolean isShielding(Input input);
    
    /**
     * Returns the ControlType associated with this key.  Returns null if no
     * such ControlType was found.
     * @param key the key to search for, such as W, A, S, D, etc.
     * @return controlType or null if not found
     */
    public ControlType getKeyType(int key);

}
