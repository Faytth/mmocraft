package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * A "clay" block.  Typically found underneath water or in the desert(?).
 * @author Faythless
 *
 */
public class ClayBlock extends Block {
    private static final BlockType type = BlockType.CLAY;

    public ClayBlock() {
        maximumHealth = 20;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_CLAY.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new ClayBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.CLAY_BLOCK);
    }
}
