package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericDead extends GenericAnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 1288070397688841412L;
    
    /**
     * 
     * @param living The creature / player that this animation belongs to.
     * @param last The last animation that this creature / player was in.
     * @param animationType The type of this animation
     * @param spriteId
     * @param horizontalOffset
     * @param verticalOffset
     * @param duration
     * @param animations
     */
    public GenericDead(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.DEAD, animations.get(AnimationType.DEAD));
        animation.setAutoUpdate(false);
        animation.setLooping(true);
    }
    
    @Override
    public void idle() {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Returns a multiplier for the movement speed downwards.  Default is 1.0.
     * This should be multiplied by the player's movement speed to determine
     * how fast they're moving.  A result of 0 signifies that the player is
     * unable to move down.
     * @return downMultiplier
     */
    public float moveDownMultiplier() {
        return 0;
    }
    
    @Override
    public void moveLeft(boolean smash) {
    }

    @Override
    public void moveRight(boolean smash) {
    }

    @Override
    public void moveUp(boolean smash) {
    }

    @Override
    public void moveDown(boolean smash) {
    }
    
    @Override
    /**
     * Returns whether or not this character is in a state which can move left
     * @return
     */
    public boolean canMoveLeft() {
        return false;
    }
    
    @Override
    /**
     * Returns whether or not this character is in a state which can move right
     * @return
     */
    public boolean canMoveRight() {
        return false;
    }
    
    @Override
    /**
     * Returns whether or not this character is in a state which can move up
     * @return
     */
    public boolean canMoveUp() {
        return false;
    }

    @Override
    public void attack() {
    }

    @Override
    public void shield() {
    }

    @Override
    public void shieldOff() {
    }

    @Override
    public void land() {
    }

    @Override
    public void fall() {
    }

    @Override
    public void die() {
    }
}
