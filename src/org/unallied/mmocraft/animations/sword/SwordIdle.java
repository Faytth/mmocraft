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
 * Animation state when a sword user is not in motion or attacking.
 * @author Faythless
 *
 */
public class SwordIdle extends AnimationState {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6341914772010394027L;

    public SwordIdle(Player player, AnimationState last) {
        super(player, last);
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheet ss = SpriteHandler.getInstance().get(SpriteID.SWORD_IDLE.toString());
        setAnimation(ss);
        animation.start();
        horizontalOffset = 26;
        verticalOffset = 6;
    }

    @Override
    public void idle() {
        // We're already idle, so do nothing
    }

    @Override
    public void moveLeft(boolean smash) {
        player.updateDirection( Direction.FACE_LEFT );
        
        if (smash) {
            player.setState( new SwordRun(player, this) );
        } else {
            player.setState( new SwordWalk(player, this) );
        }
    }

    @Override
    public void moveRight(boolean smash) {
        player.updateDirection( Direction.FACE_RIGHT );
        
        if (smash) {
            player.setState( new SwordRun(player, this) );
        } else {
            player.setState( new SwordWalk(player, this) );
        }
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
    /**
     * Returns whether or not this character is in a state which can move up
     * @return
     */
    public boolean canMoveUp() {
        return true;
    }
    
    @Override
    /**
     * Returns whether or not this character is in a state which can move down
     * @return
     */
    public boolean canMoveDown() {
        return true;
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
        return AnimationType.SWORD_IDLE;
    }

}
