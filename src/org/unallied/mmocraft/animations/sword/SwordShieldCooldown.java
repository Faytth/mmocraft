package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

/**
 * 
 * @author Alexandria
 *
 */
public class SwordShieldCooldown extends AnimationState {
    
    /**
     * 
     */
    private static final long serialVersionUID = -606357898636817393L;

    /**
     * The length of time (in milliseconds) that a player remains in "cooldown"
     * after a roll.
     */
    private static final transient long MAX_COOLDOWN_TIME = 200;
        
    public SwordShieldCooldown(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_SHIELD.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 21;
        verticalOffset = 0;
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
        cooldownTime += delta;
        
        if (cooldownTime > MAX_COOLDOWN_TIME) {
            player.setState(new SwordShield(player, this));
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
     * Returns whether or not this character is in a state which can move up
     * @return
     */
    public boolean canMoveUp() {
        return false;
    }
    
    @Override
    public void attack() {
    }

    @Override
    public void shield() {
    }

    @Override
    public void land() {
    }

    @Override
    public void fall() {
    }

    @Override
    public void shieldOff() {
        player.setState(new SwordRollCooldown(player, this));
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_SHIELD_COOLDOWN;
    }

}
