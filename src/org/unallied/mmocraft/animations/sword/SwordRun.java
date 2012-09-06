package org.unallied.mmocraft.animations.sword;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationState;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteID;

/**
 * Animation state when a player is running
 * @author Faythless
 *
 */
public class SwordRun extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = -6030014058203122053L;

    public SwordRun(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_RUN.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 24;
        verticalOffset = 1;
    }

    @Override
    public void idle() {
        player.setState(new SwordIdle(player, this));
    }

    @Override
    public void moveLeft(boolean smash) {
        player.updateDirection(Direction.FACE_LEFT);
    }

    @Override
    public void moveRight(boolean smash) {
        player.updateDirection(Direction.FACE_RIGHT);
    }

    @Override
    public void moveUp(boolean smash) {
        player.setState(new SwordJump(player, this));
    }

    @Override
    public void moveDown(boolean smash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attack() {
        player.setState(new SwordHorizontalAttack(player, this));
    }

    @Override
    public void shield() {
        player.setState(new SwordShield(player, this));
    }
    
    @Override
    public boolean canMoveLeft() {
        return player.getDirection() == Direction.FACE_LEFT;
    }
    
    @Override
    public boolean canMoveRight() {
        return player.getDirection() == Direction.FACE_RIGHT;
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
        return AnimationType.SWORD_RUN;
    }
}