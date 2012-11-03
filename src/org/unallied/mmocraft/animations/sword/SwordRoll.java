package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.animations.Roll;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

public class SwordRoll extends Roll {

    /**
     * 
     */
    private static final long serialVersionUID = 6757300044190572866L;

    /**
     * Determines the multiplier for roll speed.  Multiplier is based on normal
     * movement speed.
     */
    private static final transient float ROLL_SPEED = 1.0f;
    
    /** The start of invincibility in milliseconds. */
    private static final transient int MIN_INVINCIBLE_TIME = 30;
    
    /** The end of invincibility in milliseconds. */
    private static final transient int MAX_INVINCIBLE_TIME = 130;

    public SwordRoll(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_ROLL.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 21;
        verticalOffset = 3;
        duration = 200;
        
        switch (player.getDirection()) {
        case LEFT:
            player.setVelocity(-ROLL_SPEED * player.getMovementSpeed(), 0);
            break;
        case RIGHT:
            player.setVelocity(ROLL_SPEED * player.getMovementSpeed(), 0);
            break;
        }
    }
    
    @Override
    public void idle() {
    }

    /**
     * Updates the animation.
     * @param delta time since last update in milliseconds
     */
    @Override
    public void update(long delta) {
        // Fix the amount of distance to roll
        elapsedTime += delta;
        if (elapsedTime > duration) {
            delta -= elapsedTime - duration;
        }
        
        if (animation != null) {
            animation.update(delta);
        }
        
        // The player has exceeded their rolling time, so reset their state
        if (elapsedTime > duration) {
            player.setVelocity(0, player.getVelocity().getY());
            if (player.isShielding()) {
                player.setState(new SwordShield(player, this));
            } else {
                player.setState(new SwordIdle(player, this));
            }
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean canChangeVelocity() {
        return false;
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
        return false;
    }
    
    @Override
    public void attack() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void land() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fall() {
        player.setState(new SwordFall(player, this));
    }

    @Override
    public void shieldOff() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_ROLL;
    }

    @Override
    public boolean isInvincible() {
        return elapsedTime >= MIN_INVINCIBLE_TIME && elapsedTime <= MAX_INVINCIBLE_TIME;
    }
    
    @Override
    /**
     * Renders the animation to the screen
     * @param x The x location from the left side of the screen to draw at
     * @param y The y location from the top of the screen to draw at
     */
    public void render(float x, float y, boolean flipped) {
        if (animation != null) {
            animation.getCurrentFrame().getFlippedCopy(flipped, false).draw(
                    flipped ? x - (animation.getWidth()-horizontalOffset-player.getWidth()) : x - horizontalOffset,
                    y - verticalOffset);
        }
    }
}
