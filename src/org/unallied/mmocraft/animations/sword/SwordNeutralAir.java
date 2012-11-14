package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

public class SwordNeutralAir extends AnimationState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2112837603135218836L;

	public SwordNeutralAir(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(false);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_NEUTRAL_AIR.toString());
        this.collision = Collision.SWORD_NEUTRAL_AIR;
        width = node.getWidth();
        height = node.getHeight();
        duration = 600;
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 13;
        verticalOffset = 30;
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
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attack() {
        if (elapsedTime > duration) {
            Input input = Game.getInstance().getContainer().getInput();
            Controls controls = Game.getInstance().getControls();
            // TODO:  Make this capable of using a switch statement?
            if (controls.isMovingDown(input)) {
                player.setState(new SwordNeutralAir(player, this));
            } else if (controls.isMovingUp(input)) {
                player.setState(new SwordUpAir(player, this));
            } else if (controls.isMovingRight(input)) {
                if (player.getDirection() == Direction.LEFT) {
                    player.setState(new SwordBackAir(player, this));
                } else {
                    player.setState(new SwordFrontAir(player, this));
                }
            } else if (controls.isMovingLeft(input)) {
                if (player.getDirection() == Direction.LEFT) {
                    player.setState(new SwordFrontAir(player, this));
                } else {
                    player.setState(new SwordBackAir(player, this));
                }
            } else {
                player.setState(new SwordNeutralAir(player, this));
            }
        }
    }

    @Override
    public void shield() {
    }

    /**
     * Returns whether or not this character is in a state which can move left
     * @return
     */
    public boolean canMoveLeft() {
        return false;
    }
    
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
       /*
        *  Neutral Air is the second easiest aerial for jump distance, so it 
        *  should have a higher 
        */
        return 0.6f;
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
        player.setState(new SwordIdle(player, this));
    }
    
    @Override
    public void fall() {
    	if (elapsedTime > duration) {
    		player.setState(new SwordFall(player, this));
    	}
    }

    @Override
    public void shieldOff() {
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_NEUTRAL_AIR;
    }

    @Override
    public void die() {
        player.setState(new SwordDead(player, this));
    }
}
