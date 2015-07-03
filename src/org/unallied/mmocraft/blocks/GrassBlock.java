package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * A "grass" block.  One of the most common types of blocks.  Makes up the top layer of dirt.
 * @author Faythless
 *
 */
public class GrassBlock extends Block {
    private static final BlockType type = BlockType.GRASS;

    public GrassBlock() {
        maximumHealth = 35;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_GRASS.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new GrassBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.DIRT_BLOCK);
    }
}