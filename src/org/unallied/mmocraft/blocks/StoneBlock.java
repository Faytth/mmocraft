package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * A "dirt" block.  One of the most common types of blocks.  Makes up most of
 * the ground.
 * @author Faythless
 *
 */
public class StoneBlock extends Block {
    private static final BlockType type = BlockType.STONE;

    public StoneBlock() {
        maximumHealth = 95;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_STONE.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new StoneBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.STONE_BLOCK);
    }
}
