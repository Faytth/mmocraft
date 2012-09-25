package org.unallied.mmocraft;

public enum EffectType {
    /** Instantly restores X, where X is the value, in HP */
    INSTANTLY_RESTORE_X_HP(0x00);
    int value = 0;
    
    EffectType(int value) {
        this.value = value;
    }
    
    public short getValue() {
        return (short) value;
    }
    
    public static EffectType fromValue(int value) {
        for (EffectType et : EffectType.values()) {
            if (et.value == value) {
                return et;
            }
        }
        
        return null;
    }
}
