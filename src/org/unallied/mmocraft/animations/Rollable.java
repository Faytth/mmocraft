package org.unallied.mmocraft.animations;

import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.constants.ClientConstants;

/**
 * All animation states that can roll should extend this class.
 * @author Alexandria
 *
 */
public abstract class Rollable extends AnimationState {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1444818840737565262L;

    public Rollable(Player player, AnimationState last) {
        super(player, last);
    }

    protected long shieldTime = 0;
    protected long shieldOffTime = 0;
    
    /**
     * Gets the roll state associated with this rollable class.
     * @return roll state
     */
    public abstract AnimationState getRollState();
    
    @Override
    public void moveLeft(boolean smash) {
        if (shieldTime > shieldOffTime || 
                shieldOffTime + ClientConstants.SHIELD_ROLL_DELAY > System.currentTimeMillis()) {
            player.updateDirection(Direction.FACE_LEFT);
            player.setState(getRollState());
        }
    }
    
    @Override
    public void moveRight(boolean smash) {
        if (shieldTime > shieldOffTime || 
                shieldOffTime + ClientConstants.SHIELD_ROLL_DELAY > System.currentTimeMillis()) {
            player.updateDirection(Direction.FACE_RIGHT);
            player.setState(getRollState());
        }
    }
    
    @Override
    public void shield() {
        // We want to be able to "roll cancel" attacks
        shieldTime = System.currentTimeMillis();
    }

    @Override
    public void shieldOff() {
        shieldOffTime = System.currentTimeMillis();
    }
}
