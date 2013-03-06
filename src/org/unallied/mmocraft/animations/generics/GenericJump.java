package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericJump extends GenericAnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = -5403851876992048374L;
    
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
    public GenericJump(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.JUMP, animations.get(AnimationType.JUMP));
        animation.setAutoUpdate(false);
        living.setFallSpeed(-300f - living.getMovementSpeed() * 0.251f);
        this.animations = animations;
    }

    @Override
    public void idle() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveLeft(boolean smash) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveRight(boolean smash) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveUp(boolean smash) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void attack() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Returns whether or not this character is in a state which can move left
     * @return
     */
    public boolean canMoveLeft() {
        return true;
    }
    
    /**
     * Returns whether or not this character is in a state which can move right
     * @return
     */
    public boolean canMoveRight() {
        return true;
    }

    @Override
    public void shieldOff() {
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
        return 1.0f;
    }

    @Override
    /**
     * Returns whether or not the current state is in the air (such as jumping
     * or falling).
     * @return
     */
    public boolean isInAir() {
        return true;
    }
    
    @Override
    public void land() {
        if (animations.containsKey(AnimationType.IDLE)) {
            living.setState(new GenericIdle(living, this, animations));
        }
    }

    @Override
    public void fall() {
        if (animations.containsKey(AnimationType.FALL)) {
            living.setState(new GenericFall(living, this, animations));
        }
    }

    @Override
    public void die() {
        if (animations.containsKey(AnimationType.DEAD)) {
            living.setState(new GenericDead(living, this, animations));
        }
    }

    @Override
    public boolean isLooping() {
        return true;
    }

}
