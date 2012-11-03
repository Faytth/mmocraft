package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.Roll;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.constants.ClientConstants;

public class SwordShield extends AnimationState {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7802962161525790877L;

    public SwordShield(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_SHIELD.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 21;
        verticalOffset = 0;
        player.setVelocity(0, player.getVelocity().getY());
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing
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
        if (animation.isStopped()) {
            player.setState(new SwordIdle(player, this));
        }
    }
    
    @Override
    public void moveLeft(boolean smash) {
        boolean canRoll = true;
        long lastRollExit = entryTime; // The time that the last roll exited
        AnimationState index = last;
        for (int i=0; i < 6 && canRoll; ++i) {
            if (index != null) {
                if (index instanceof Roll) {
                    canRoll = false;
                } else {
                    lastRollExit = index.getEntryTime();
                }
                index = index.getPreviousState();
            }
        }
        if (canRoll || (System.currentTimeMillis() - lastRollExit) > ClientConstants.ROLL_COOLDOWN) {
            player.updateDirection(Direction.LEFT);
            player.setState(new SwordRoll(player, this));
        }
    }

    @Override
    public void moveRight(boolean smash) {
        boolean canRoll = true;
        long lastRollExit = entryTime; // The time that the last roll exited
        AnimationState index = last;
        for (int i=0; i < 6 && canRoll; ++i) {
            if (index != null) {                
                if (index instanceof Roll) {
                    canRoll = false;
                } else {
                    lastRollExit = index.getEntryTime();
                }
                index = index.getPreviousState();
            }
        }
        if (canRoll || (System.currentTimeMillis() - lastRollExit) > ClientConstants.ROLL_COOLDOWN) {
            player.updateDirection(Direction.RIGHT);
            player.setState(new SwordRoll(player, this));
        }
    }

    @Override
    public void moveUp(boolean smash) {
        player.setState(new SwordJump(player, this));
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
    public void attack() {
        player.setState(new SwordHorizontalAttack(player, this));
    }

    @Override
    public void shield() {
    }

    @Override
    public void land() {
    }

    @Override
    public void fall() {
        player.setState(new SwordFall(player, this));
    }

    @Override
    public void shieldOff() {
        player.setState(new SwordIdle(player, this));
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_SHIELD;
    }

}
