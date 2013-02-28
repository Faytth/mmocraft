package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

public class SwordJump extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 5694192053146518272L;

    public SwordJump(Living player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_JUMP.toString());
        width = node.getWidth();
        height = node.getHeight();
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 19;
        verticalOffset = 14;
        player.setFallSpeed(-300f - player.getMovementSpeed() * 0.251f);
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
        living.setState(new SwordDoubleJump(living, this));
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attack() {
    	Input input = Game.getInstance().getContainer().getInput();
    	Controls controls = Game.getInstance().getControls();
    	// TODO:  Make this capable of using a switch statement?
    	if (controls.isMovingDown(input)) {
    		living.setState(new SwordNeutralAir(living, this));
    	} else if (controls.isMovingUp(input)) {
    		living.setState(new SwordUpAir(living, this));
    	} else if (controls.isMovingRight(input)) {
    		if (living.getDirection() == Direction.LEFT) {
    			living.setState(new SwordBackAir(living, this));
    		} else {
    			living.setState(new SwordFrontAir(living, this));
    		}
    	} else if (controls.isMovingLeft(input)) {
    		if (living.getDirection() == Direction.LEFT) {
    			living.setState(new SwordFrontAir(living, this));
    		} else {
    			living.setState(new SwordBackAir(living, this));
    		}
    	} else {
    		living.setState(new SwordNeutralAir(living, this));
    	}
    }

    @Override
    public void shield() {
        living.setState(new SwordAirDodge(living, this));
    }

    /**
     * Returns whether or not this character is in a state which can move left
     * @return
     */
    public boolean canMoveLeft() {
        return true;
    }
    
    /**
     * Returns whether or not this character is in a state which can move right
     * @return
     */
    public boolean canMoveRight() {
        return true;
    }
    
    @Override
    /**
     * Returns whether or not this character is in a state which can move up
     * @return
     */
    public boolean canMoveUp() {
        return canDoubleJump;
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
        living.setState(new SwordFall(living, this));
    }

    @Override
    public void shieldOff() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public short getId() {
        return AnimationID.SWORD_JUMP.getValue();
    }

    @Override
    public void die() {
        living.setState(new SwordDead(living, this));
    }
}
