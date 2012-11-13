package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;
import org.unallied.mmocraft.client.SpriteSheetNode;

public class SwordFrontAir extends AnimationState {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1953254272514644551L;

	public SwordFrontAir(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(false);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(SpriteID.SWORD_FRONT_AIR.toString());
        this.collision = Collision.SWORD_FRONT_AIR;
        width = node.getWidth();
        height = node.getHeight();
        setAnimation(node.getSpriteSheet());
        animation.start();
        horizontalOffset = 19;
        verticalOffset = 29;
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
        // TODO Auto-generated method stub

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
        return 0.5f;
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
    	if (animation.isStopped()) {
    		player.setState(new SwordFall(player, this));
    	}
    }

    @Override
    public void shieldOff() {
    }

    @Override
    public AnimationType getId() {
        return AnimationType.SWORD_FRONT_AIR;
    }

    @Override
    public void die() {
        player.setState(new SwordDead(player, this));
    }
}
