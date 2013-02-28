package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericIdle extends GenericAnimationState {

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
    public GenericIdle(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.IDLE, animations.get(AnimationType.IDLE));
        
        this.animations = animations;
        
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing.
    }

    @Override
    public void moveLeft(boolean smash) {
        living.updateDirection(Direction.LEFT);

        if (smash && animations.containsKey(AnimationType.RUN)) {
            living.setState(new GenericWalk(living, this, animations));
        } else if (animations.containsKey(AnimationType.WALK)) {
            living.setState(new GenericWalk(living, this, animations));
        }
    }

    @Override
    public void moveRight(boolean smash) {
        living.updateDirection(Direction.RIGHT);
        
        if (smash && animations.containsKey(AnimationType.RUN)) {
            living.setState(new GenericRun(living, this, animations));
        } else if (animations.containsKey(AnimationType.WALK)) {
            living.setState(new GenericWalk(living, this, animations));
        }
    }

    @Override
    public void moveUp(boolean smash) {
        if (animations.containsKey(AnimationType.JUMP)){
            living.setState(new GenericJump(living, this, animations));
        }
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub
        
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
        return true;
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
    public void attack() {
        if (animations.containsKey(AnimationType.ATTACK)) {
            living.setState(new GenericAttack(living, this, animations));
        }
    }

    @Override
    public void shield() {
        if (animations.containsKey(AnimationType.SHIELD)) {
            living.setState(new GenericShield(living, this, animations));
        }
    }

    @Override
    public void shieldOff() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void land() {
        // TODO Auto-generated method stub
        
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

}
