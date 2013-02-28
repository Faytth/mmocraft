package org.unallied.mmocraft;

public enum PassiveType {
    ARMOR(0x00);
    int value = 0;
    
    PassiveType(int value) {
        this.value = value;
    }
    
    public short getValue() {
        return (short) value;
    }
    
    public static PassiveType fromValue(int value) {
        for (PassiveType pt : PassiveType.values()) {
            if (pt.value == value) {
                return pt;
            }
        }
        
        return null;
    }
}
