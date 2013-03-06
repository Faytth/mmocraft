package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;

public class GenericFall extends GenericAnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = -1162482924528358159L;
    
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
    public GenericFall(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.FALL, animations.get(AnimationType.FALL));
        animation.setAutoUpdate(false);
        animation.setLooping(isLooping());
        
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
        // TODO:  Implement this if you need to.
        /*if (canMoveUp() && animations.containsKey(AnimationType.DOUBLE_JUMP)) {
            living.setState(new GenericDoubleJump(living, this, animations));
        }*/
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attack() {
        // TODO: If we need aerial attacks, go ahead and add them here.
    }

    @Override
    public void shield() {
        // TODO:  Add if necessary
        /*if (animations.containsKey(AnimationType.AIR_DODGE)) {
            living.setState(new GenericAirDodge(living, this, animations));
        }*/
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
