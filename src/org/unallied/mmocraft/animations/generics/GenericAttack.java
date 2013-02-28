package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericAttack extends GenericAnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = -5403851876992048374L;
    
    /** True if the living object is in the air. */
    private boolean inAir = false;
    
    private Map<AnimationType, String> animations;
    
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
    public GenericAttack(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.ATTACK, animations.get(AnimationType.ATTACK));
        animation.setAutoUpdate(false);
        animation.setLooping(false);
        
        this.animations = animations;
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing
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
        inAir = false;
    }

    @Override
    public void fall() {
        inAir = true;
    }

    @Override
    public void die() {
        if (animations.containsKey(AnimationType.DEAD)) {
            living.setState(new GenericDead(living, this, animations));
        }
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
        return inAir ? 1.0f : 0;
    }
    
    @Override
    public void update(long delta) {
        super.update(delta);
        
        // If we're done, then we want to return to idle.
        if (elapsedTime > duration) {
            if (living.isShielding() && animations.containsKey(AnimationType.SHIELD)) {
                living.setState(new GenericShield(living, this, animations));
            } else if (animations.containsKey(AnimationType.IDLE)){
                living.setState(new GenericIdle(living, this, animations));
            }
        }
    }
}