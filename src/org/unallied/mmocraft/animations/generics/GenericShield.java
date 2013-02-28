package org.unallied.mmocraft.animations.generics;

import java.util.Map;

import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.GenericAnimationState;
import org.unallied.mmocraft.animations.sword.SwordIdle;

public class GenericShield extends GenericAnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 885647679019404123L;
    
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
    public GenericShield(Living living, AnimationState last,
            Map<AnimationType, String> animations) {
        super(living, last, AnimationType.SHIELD, animations.get(AnimationType.SHIELD));
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        
        this.animations = animations;
    }
    
    @Override
    public void idle() {
        // TODO Auto-generated method stub

    }

    /**
     * Updates the animation.
     * @param delta time since last update in milliseconds
     */
    @Override
    public void update(long delta) {
        if (animation != null) {
            animation.update(delta);
        }
        if ((animation.isStopped() || !living.isShielding()) && 
                animations.containsKey(AnimationType.IDLE)) {
            living.setState(new GenericIdle(living, this, animations));
        }
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
        return true;
    }
    
    @Override
    public void attack() {
        if (animations.containsKey(AnimationType.ATTACK)) {
            living.setState(new GenericAttack(living, this, animations));
        }
    }

    @Override
    public void shield() {
    }

    @Override
    public void shieldOff() {
        if (animations.containsKey(AnimationType.IDLE)){
            living.setState(new GenericIdle(living, this, animations));
        }
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
        if (animations.containsKey(AnimationType.DEAD)){
            living.setState(new GenericDead(living, this, animations));
        }
    }
}
