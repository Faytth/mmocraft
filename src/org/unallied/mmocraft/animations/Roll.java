package org.unallied.mmocraft.animations;

import org.unallied.mmocraft.Living;

/**
 * An abstract class meant to be a super class to all classes that roll, such as SwordRoll.
 * @author Alexandria
 *
 */
public abstract class Roll extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 5794470932829718412L;

    public Roll(Living player, AnimationState last) {
        super(player, last);
    }
}
