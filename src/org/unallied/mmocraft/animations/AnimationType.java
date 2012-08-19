package org.unallied.mmocraft.animations;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.sword.*;

/**
 * An enumeration of all animation types.  Each type is linked to a specific
 * animation state.
 * 
 * NOTE:  When you add a new state, you MUST add it here as well.
 * @author Alexandria
 *
 */
public enum AnimationType {
    SWORD_FALL(0x00),
    SWORD_HORIZONTAL_ATTACK(0x01),
    SWORD_HORIZONTAL_ATTACK_2(0x02),
    SWORD_IDLE(0x03),
    SWORD_JUMP(0x04),
    SWORD_ROLL(0x05),
    SWORD_ROLL_COOLDOWN(0x06),
    SWORD_RUN(0x07),
    SWORD_SHIELD(0x08),
    SWORD_SHIELD_COOLDOWN(0x09),
    SWORD_WALK(0x0A);
    int value = 0;
    
    AnimationType(int value) {
        this.value = value;
    }
    
    public short getValue() {
        return (short) value;
    }
    
    public static AnimationType fromValue(int value) {
        for (AnimationType at : AnimationType.values()) {
            if (at.value == value) {
                return at;
            }
        }
        
        return null; // not found
    }
    
    public static AnimationState getState(Player player, AnimationState prev, int value) {
        return getState(player, prev, fromValue(value));
    }
    
    public static AnimationState getState(Player player, AnimationState prev, AnimationType type) {
        AnimationState result = null;
        switch (type) {
        case SWORD_FALL:
            result = new SwordFall(player, prev);
            break;
        case SWORD_HORIZONTAL_ATTACK:
            result = new SwordHorizontalAttack(player, prev);
            break;
        case SWORD_HORIZONTAL_ATTACK_2:
            result = new SwordHorizontalAttack2(player, prev);
            break;
        case SWORD_IDLE:
            result = new SwordIdle(player, prev);
            break;
        case SWORD_JUMP:
            result = new SwordJump(player, prev);
            break;
        case SWORD_ROLL:
            result = new SwordRoll(player, prev);
            break;
        case SWORD_ROLL_COOLDOWN:
            result = new SwordRollCooldown(player, prev);
            break;
        case SWORD_RUN:
            result = new SwordRun(player, prev);
            break;
        case SWORD_SHIELD:
            result = new SwordShield(player, prev);
            break;
        case SWORD_SHIELD_COOLDOWN:
            result = new SwordShieldCooldown(player, prev);
            break;
        case SWORD_WALK:
            result = new SwordWalk(player, prev);
            break;
        }
        
        return result;
    }
}
