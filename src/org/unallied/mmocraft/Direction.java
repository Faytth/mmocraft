package org.unallied.mmocraft;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public enum Direction {
    RIGHT(0x00), 
    LEFT(0x01), 
    UP(0x02), 
    DOWN(0x03);
    int value = 0;
    
    Direction(int value) {
        this.value = value;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static Direction fromValue(SeekableLittleEndianAccessor slea) {
        return fromValue(slea.readByte());
    }
    
    public static Direction fromValue(int value) {
        for (Direction d : Direction.values()) {
            if (d.value == value) {
                return d;
            }
        }
        
        return RIGHT; // Default
    }
}
