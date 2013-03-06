package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.Velocity;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

public class SwordAirDodge extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 2346446800727669796L;

    /** The time (in milliseconds from the start of the animation) to start invincibility. */
    public static final int INVINCIBILITY_START = 83;
    
    /** The time (in milliseconds from the start of the animation) to end invincibility. */
    public static final int INVINCIBILITY_END = 500;
    
    /** The distance (in pixels) that the player can move when holding down a directional. */
    public static final float MOVE_DISTANCE = 100;
    
    public SwordAirDodge(Living player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(isLooping());
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_AIR_DODGE.toString());
        this.collision = node.getCollision();
        width = node.getWidth();
        height = node.getHeight();
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 19;
        verticalOffset = 14;
        duration = 817;
        
        // Move the player based on their input
        try {
            Controls controls = Game.getInstance().getControls();
            Input input = Game.getInstance().getContainer().getInput();
            float xVelocity = 0;
            float yVelocity = 0;
            if (controls.isMovingLeft(input)) {
                xVelocity = -MOVE_DISTANCE;
            }
            else if (controls.isMovingRight(input)) {
                xVelocity = MOVE_DISTANCE;
            }
            if (controls.isMovingUp(input)) {
                yVelocity = -MOVE_DISTANCE;
            } else if (controls.isMovingDown(input)) {
                yVelocity = MOVE_DISTANCE;
            }
            player.setVelocity(0, 0);
            Velocity tempVelocity = new Velocity(xVelocity, yVelocity);
            player.setFallSpeed(0);
            
            player.move(1000, tempVelocity, 0, 0); // Using 1 for milliseconds so that MOVE_DISTANCE is pixels
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void idle() {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveLeft(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveRight(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveUp(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub

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
    /**
     * Returns a multiplier for the movement speed downwards.  Default is 1.0.
     * This should be multiplied by the player's movement speed to determine
     * how fast they're moving.  A result of 0 signifies that the player is
     * unable to move down.
     * @return downMultiplier
     */
    public float moveDownMultiplier() {
        return 0.40f;
    }

    @Override
    /**
     * Returns whether or not the current state is in the air (such as jumping
     * or falling).
     * @return
     */
    public boolean isInAir() {
        return true;
    }
    
    @Override
    public void land() {
        living.setState(new SwordIdle(living, this));
    }
    
    @Override
    public void fall() {
    }

    @Override
    public void shieldOff() {
    }

    @Override
    public void update(long delta) {
        super.update(delta);
        
        // If we're done, we want to enter the helpless state.
        if (elapsedTime > duration) {
            living.setState(new SwordHelpless(living, this));
        }
    }
    
    @Override
    public short getId() {
        return AnimationID.SWORD_AIR_DODGE.getValue();
    }
    
    @Override
    public boolean isInvincible() {
        return elapsedTime >= INVINCIBILITY_START && elapsedTime <= INVINCIBILITY_END;
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
