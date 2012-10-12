package org.unallied.mmocraft;

import org.unallied.mmocraft.blocks.*;

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
    AIR(0x00, new AirBlock()),
    DIRT(0x01, new DirtBlock()),
    //WOOD(0x02, new WoodBlock()),
    STONE(0x03, new StoneBlock()),
    IRON(0x04, new IronBlock()), 
    CLAY(0x05, new ClayBlock()),
    GRAVEL(0x06, new GravelBlock()),
    SANDSTONE(0x07, new SandstoneBlock()),
    SAND(0x08, new SandBlock()),
    GRASS(0x09, new GrassBlock());
    int value = 0;
    Block block;
    
    BlockType(int value, Block block) {
        this.value = value;
        this.block = block;
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
    
    /**
     * Retrieves a copy of a block that goes with this block type.
     * 
     * This can be used when you only have a block type but want the Block
     * class that it refers to.
     * @return block
     */
    public Block getBlock() {
        return block.getCopy();
    }
}
