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
	AIR(0x00, AirBlock.class),
	DIRT(0x01, DirtBlock.class),
	//WOOD(0x02, WoodBlock.class),
	STONE(0x03, StoneBlock.class),
	IRON(0x04, IronBlock.class), 
	CLAY(0x05, ClayBlock.class),
	GRAVEL(0x06, GravelBlock.class),
	SANDSTONE(0x07, SandstoneBlock.class),
	SAND(0x08, SandBlock.class),
	GRASS(0x09, GrassBlock.class);
	int value = 0;
	@SuppressWarnings("rawtypes")
    Class block;

	@SuppressWarnings("rawtypes")
    BlockType(int value, Class block) {
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
		try {
			return (Block)block.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
