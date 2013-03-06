package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericWalk extends GenericAnimationState {

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
    public GenericWalk(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.WALK, animations.get(AnimationType.WALK));
        animation.setAutoUpdate(false);
        
        this.animations = animations;
        
    }

    @Override
    public void idle() {
        if (animations.containsKey(AnimationType.IDLE)) {
            living.setState(new GenericIdle(living, this, animations));
        }
    }

    @Override
    public void moveLeft(boolean smash) {
        living.updateDirection(Direction.LEFT);
        
        if (smash && animations.containsKey(AnimationType.RUN)) {
            living.setState(new GenericRun(living, this, animations));
        }
    }

    @Override
    public void moveRight(boolean smash) {
        living.updateDirection(Direction.RIGHT);

        if (smash && animations.containsKey(AnimationType.RUN)) {
            living.setState(new GenericRun(living, this, animations));
        }
    }

    @Override
    public void moveUp(boolean smash) {
        if (animations.containsKey(AnimationType.JUMP)) {
            living.setState(new GenericJump(living, this, animations));
        }
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub
        
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
    public boolean canMoveLeft() {
        return living.getDirection() == Direction.LEFT;
    }
    
    @Override
    public boolean canMoveRight() {
        return living.getDirection() == Direction.RIGHT;
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

    @Override
    public boolean isLooping() {
        return true;
    }

}
