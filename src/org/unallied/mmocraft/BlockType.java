package org.unallied.mmocraft;

/**
 * An enumeration of all types of blocks.  Each type is linked to a specific
 * texture, sound, toughness, and so on.
 * 
 * NOTE:  You should only append to this enumeration.  Do not place new values
 * between already-established values.  Failure to do so can result in the 
 * corruption of the entire game world.
 * 
 * @author Faythless
 *
 */
public enum BlockType {
    AIR(0x00),
    DIRT(0x01),
    WOOD(0x02),
    STONE(0x03),
    IRON(0x04), 
    CLAY(0x05),
    GRAVEL(0x06),
    SANDSTONE(0x07),
    SAND(0x08);
    int value = 0;
    
    BlockType(int value) {
        this.value = value;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static BlockType fromValue(int value) {
        for (BlockType bt : BlockType.values()) {
            if (bt.value == value) {
                return bt;
            }
        }
        
        return null; // not found
    }
}
