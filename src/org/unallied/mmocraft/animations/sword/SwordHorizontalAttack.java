package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.Rollable;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

/**
 * Animation state when a sword user is performing a basic, horizontal sword
 * attack.
 * @author Faythless
 *
 */
public class SwordHorizontalAttack extends Rollable {

    /**
     * 
     */
    private static final long serialVersionUID = 5216310073752517557L;

    private boolean inAir = false; // true if the player is in the air
    
    /**
     * This value determines whether or not this state returns to idle or
     * proceeds to the next attack once the animation ends.
     */
    private transient boolean performNextAttack = false;
    
    public SwordHorizontalAttack(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(false);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_HORIZONTAL_ATTACK.toString());
        this.collision = Collision.SWORD_HORIZONTAL_ATTACK;
        setAnimation(ss);
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
        if (animation.getFrame() > 2) {
            performNextAttack = true;
        }
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
        if (animation.isStopped()) {
            if (player.isShielding()) {
                player.setState(new SwordShield(player, this));
            } else if (performNextAttack) {
                player.setState(new SwordHorizontalAttack2(player, this));
            } else {
                player.setState(new SwordIdle(player, this));
            }
        }
    }
    
    @Override
    public AnimationState getRollState() {
        return new SwordRoll(player, this);
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_HORIZONTAL_ATTACK;
    }

}