package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

/**
 * This state is caused right after SwordRoll.
 * Its purpose is to serve as a small delay after rolling to prevent
 * abusive game mechanics, etc.
 * @author Alexandria
 *
 */
public class SwordRollCooldown extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = -7103373482776820849L;
    /**
     * The length of time (in milliseconds) that a player remains in "cooldown"
     * after a roll.
     */
    private static final transient long MAX_COOLDOWN_TIME = 200;
        
    public SwordRollCooldown(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_IDLE.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 26;
        verticalOffset = 6;
        cooldownTime = last.getCooldownTime();
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
        // Fix the amount of distance to roll
        cooldownTime += delta;
        
        // The player has exceeded their rolling time, so reset their state
        if (cooldownTime > MAX_COOLDOWN_TIME) {
            player.setState(new SwordIdle(player, this));
        }
    }
    
    @Override
    public void moveLeft(boolean smash) {
        // Can't move to the left when in cooldown
    }

    @Override
    public void moveRight(boolean smash) {
        // Can't move to the right when in cooldown
    }

    @Override
    public void moveUp(boolean smash) {
        // Can't jump when in cooldown
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
     * Returns whether or not this character is in a state which can move down
     * @return
     */
    public boolean canMoveDown() {
        return false;
    }
    
    @Override
    public void attack() {
        // Can't attack when in cooldown
    }

    @Override
    public void shield() {
        player.setState(new SwordShieldCooldown(player, this));
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
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_ROLL_COOLDOWN;
    }

}
