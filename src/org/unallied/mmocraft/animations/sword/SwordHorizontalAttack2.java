package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.Rollable;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

/**
 * Animation state when a sword user is performing the second part of their
 * basic, horizontal attack.
 * @author Faythless
 *
 */
public class SwordHorizontalAttack2 extends Rollable {

    /**
     * 
     */
    private static final long serialVersionUID = -3157503491148805906L;
    
    private boolean inAir = false; // true if the player is in the air
        
    public SwordHorizontalAttack2(Living player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(false);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_HORIZONTAL_ATTACK_2.toString());
        this.collision = Collision.SWORD_HORIZONTAL_ATTACK_2;
        width = node.getWidth();
        height = node.getHeight();
        duration = 350;
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 53;
        verticalOffset = 32;
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing
    }

    @Override
    public void moveLeft(boolean smash) {
        super.moveLeft(smash);
    }

    @Override
    public void moveRight(boolean smash) {
        super.moveRight(smash);
    }

    @Override
    public void moveUp(boolean smash) {
    }

    @Override
    public void moveDown(boolean smash) {
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
    public boolean isInAir() {
        return inAir;
    }
    
    @Override
    public void attack() {
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
    public void update(long delta) {
        super.update(delta);
        
        // If we're done, then we want to return to idle.
        if (elapsedTime > duration) {
            if (living.isShielding()) {
                living.setState(new SwordShield(living, this));
            } else {
                living.setState(new SwordIdle(living, this));
            }
        }
    }
    
    @Override
    public AnimationState getRollState() {
        return new SwordRoll(living, this);
    }

    @Override
    public short getId() {
        return AnimationID.SWORD_HORIZONTAL_ATTACK_2.getValue();
    }

    @Override
    public void die() {
        living.setState(new SwordDead(living, this));
    }

}