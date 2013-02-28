package org.unallied.mmocraft.animations;

import java.util.Map;

import org.unallied.mmocraft.animations.generics.*;
import org.unallied.mmocraft.monsters.ClientMonsterManager;
import org.unallied.mmocraft.monsters.Monster;
import org.unallied.mmocraft.monsters.MonsterData;

/**
 * The animation type, such as idle, walk, jump, or run.
 * @author Alexandria
 *
 */
public enum AnimationType {
    IDLE(0x00),
    WALK(0x01),
    JUMP(0x02),
    ATTACK(0x03),
    RUN(0x04), 
    SHIELD(0x05),
    FALL(0x06),
    DEAD(0x07);
    
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
        
        return null;
    }

    public static AnimationState getState(Monster monster,
            AnimationState prev, short value) {
        return getState(monster, prev, fromValue(value));
    }
    
    public static AnimationState getState(Monster monster,
            AnimationState prev, AnimationType type) {
        try {
            AnimationState result = null;
            MonsterData data = ClientMonsterManager.getInstance().getMonsterData(monster.getDataId());
            
            Map<AnimationType, String> animations = data.getAnimations();
            
            String key = animations.get(type);
            if (key != null) {
                switch (type) {
                case IDLE:
                    result = new GenericIdle(monster, prev, animations);
                    break;
                case WALK:
                    result = new GenericWalk(monster, prev, animations);
                    break;
                case JUMP:
                    result = new GenericJump(monster, prev, animations);
                    break;
                case ATTACK:
                    result = new GenericAttack(monster, prev, animations);
                    break;
                case RUN:
                    result = new GenericRun(monster, prev, animations);
                    break;
                case SHIELD:
                    result = new GenericShield(monster, prev, animations);
                    break;
                case FALL:
                    result = new GenericFall(monster, prev, animations);
                    break;
                case DEAD:
                    result = new GenericDead(monster, prev, animations);
                    break;
                }
            }
            
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
