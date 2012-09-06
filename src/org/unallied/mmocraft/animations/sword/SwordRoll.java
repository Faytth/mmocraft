package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

public class SwordRoll extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 6757300044190572866L;

    /**
     * Determines the multiplier for roll speed.  Multiplier is based on normal
     * movement speed.
     */
    private static final transient double ROLL_SPEED = 1.5;
    
    /**
     * The amount of time (in milliseconds) that the player rolls for.
     */
    private static final transient int MAX_ROLL_TIME = 180;
    
    /**
     * The amount of time (in milliseconds) that the player has been rolling.
     */
    private transient int rollTime = 0;

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
        rollTime += delta;
        if (rollTime > MAX_ROLL_TIME) {
            delta -= rollTime - MAX_ROLL_TIME;
        }
        
        if (animation != null) {
            animation.update(delta);
        }
        switch (player.getDirection()) {
        case FACE_LEFT:
            player.moveLeft((int) (delta*ROLL_SPEED));
            break;
        case FACE_RIGHT:
            player.moveRight((int) (delta*ROLL_SPEED));
            break;
        }
        
        // The player has exceeded their rolling time, so reset their state
        if (rollTime > MAX_ROLL_TIME) {
            if (player.isShielding()) {
                player.setState(new SwordShieldCooldown(player, this));
            } else {
                player.setState(new SwordRollCooldown(player, this));
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

}