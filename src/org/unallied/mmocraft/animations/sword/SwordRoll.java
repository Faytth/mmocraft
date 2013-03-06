package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.Roll;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

public class SwordRoll extends Roll {

    /**
     * 
     */
    private static final long serialVersionUID = 6757300044190572866L;

    /**
     * Determines the multiplier for roll speed.  Multiplier is based on normal
     * movement speed.
     */
    private static final transient float ROLL_SPEED = 1.1f;
    
    /** The start of invincibility in milliseconds. */
    private static final transient int MIN_INVINCIBLE_TIME = 30;
    
    /** The end of invincibility in milliseconds. */
    private static final transient int MAX_INVINCIBLE_TIME = 130;

    public SwordRoll(Living player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(isLooping());
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_ROLL.toString());
        this.collision = node.getCollision();
        width = node.getWidth();
        height = node.getHeight();
        duration = 200;
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 21;
        verticalOffset = 3;
        
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
            living.setVelocity(0, living.getVelocity().getY());
            living.setState(new SwordShield(living, this));
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
        living.setState(new SwordFall(living, this));
    }

    @Override
    public void shieldOff() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public short getId() {
        return AnimationID.SWORD_ROLL.getValue();
    }

    @Override
    public boolean isInvincible() {
        return elapsedTime >= MIN_INVINCIBLE_TIME && elapsedTime <= MAX_INVINCIBLE_TIME;
    }

    @Override
    public void die() {
        living.setState(new SwordDead(living, this));
    }

    @Override
    public boolean isLooping() {
        return true;
    }
}
