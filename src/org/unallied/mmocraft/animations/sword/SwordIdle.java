package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

/**
 * Animation state when a sword user is not in motion or attacking.
 * @author Faythless
 *
 */
public class SwordIdle extends AnimationState {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6341914772010394027L;

    public SwordIdle(Living player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_IDLE.toString());
        width = node.getWidth();
        height = node.getHeight();
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 26;
        verticalOffset = 6;
        canDoubleJump = true; // reset double jump
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing
    }

    @Override
    public void moveLeft(boolean smash) {
        living.updateDirection( Direction.LEFT );
        
        if (smash) {
            living.setState( new SwordRun(living, this) );
        } else {
            living.setState( new SwordWalk(living, this) );
        }
    }

    @Override
    public void moveRight(boolean smash) {
        living.updateDirection( Direction.RIGHT );
        
        if (smash) {
            living.setState( new SwordRun(living, this) );
        } else {
            living.setState( new SwordWalk(living, this) );
        }
    }

    @Override
    public void moveUp(boolean smash) {
        living.setState(new SwordJump(living, this));
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
        living.setState(new SwordHorizontalAttack(living, this));
    }

    @Override
    public void shield() {
        living.setState(new SwordShield(living, this));
    }

    @Override
    public void land() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fall() {
        living.setState(new SwordFall(living, this));
    }

    @Override
    public void shieldOff() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public short getId() {
        return AnimationID.SWORD_IDLE.getValue();
    }

    @Override
    public void die() {
        living.setState(new SwordDead(living, this));
    }

}
