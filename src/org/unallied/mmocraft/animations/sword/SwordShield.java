package org.unallied.mmocraft.animations.sword;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.Roll;
import org.unallied.mmocraft.animations.generics.GenericShield;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;
import org.unallied.mmocraft.constants.ClientConstants;

public class SwordShield extends GenericShield {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7802962161525790877L;

    // TODO:  Update this to our new method (using the resource editor) once it's done.
    private static final Map<AnimationType, String> animations = new HashMap<AnimationType, String>();
    static {
        animations.put(AnimationType.IDLE, SpriteID.SWORD_IDLE.toString());
        animations.put(AnimationType.ATTACK, SpriteID.SWORD_HORIZONTAL_ATTACK.toString());
        animations.put(AnimationType.DEAD, SpriteID.SWORD_HELPLESS.toString());
        animations.put(AnimationType.FALL, SpriteID.SWORD_FALL.toString());
        animations.put(AnimationType.JUMP, SpriteID.SWORD_JUMP.toString());
        animations.put(AnimationType.RUN, SpriteID.SWORD_RUN.toString());
        animations.put(AnimationType.SHIELD, SpriteID.SWORD_SHIELD.toString());
        animations.put(AnimationType.WALK, SpriteID.SWORD_WALK.toString());
    }
    
    public SwordShield(Living player, AnimationState last) {
        super(player, last, animations);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(isLooping());
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_SHIELD.toString());
        this.collision = node.getCollision();
        width = node.getWidth();
        height = node.getHeight();
        setAnimation(node.getSpriteSheet());
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
        super.update(delta);
        
        if (animation.isStopped() || !living.isShielding()) {
            living.setState(new SwordIdle(living, this));
        }
    }
    
    /**
     * Returns true if the player is in control of their actions and is able to
     * move.  This is used as a "cooldown" after a roll.  The player must recover
     * before being able to move again.
     * @return true if this player is able to move, else false.
     */
    public boolean canMove() {
        boolean result = true;
        long lastRollExit = entryTime; // The time that the last roll exited
        AnimationState index = last;
        for (int i=0; i < 6 && result; ++i) {
            if (index != null) {
                if (index instanceof Roll) {
                    result = false;
                } else {
                    lastRollExit = index.getEntryTime();
                }
                index = index.getPreviousState();
            }
        }
        
        return result || (System.currentTimeMillis() - lastRollExit) > ClientConstants.ROLL_COOLDOWN;
    }
    
    @Override
    public void moveLeft(boolean smash) {
        if (canMove() && living.isShielding()) {
            living.updateDirection(Direction.LEFT);
            living.setState(new SwordRoll(living, this));
        }
    }

    @Override
    public void moveRight(boolean smash) {
        if (canMove() && living.isShielding()) {
            living.updateDirection(Direction.RIGHT);
            living.setState(new SwordRoll(living, this));
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
    public void attack() {
        living.setState(new SwordHorizontalAttack(living, this));
    }

    @Override
    public void shield() {
    }

    @Override
    public void land() {
    }

    @Override
    public void fall() {
        living.setState(new SwordFall(living, this));
    }

    @Override
    public void shieldOff() {
        living.setState(new SwordIdle(living, this));
    }

    @Override
    public short getId() {
        return AnimationID.SWORD_SHIELD.getValue();
    }

    @Override
    public void die() {
        living.setState(new SwordDead(living, this));
    }

    @Override
    public boolean isLooping() {
        return true;
    }
    
    @Override
    public boolean isInvincible() {
        return elapsedTime < 50; // Invincible for the first 50 ms
    }
    
    @Override
    public boolean isShielding() {
        return true;
    }
}
