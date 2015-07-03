package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * Gravel.  This is used for roads and such.
 * @author Faythless
 *
 */
public class GravelBlock extends Block {
    private static final BlockType type = BlockType.GRAVEL;

    public GravelBlock() {
        maximumHealth = 80;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_GRAVEL.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new GravelBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.GRAVEL_BLOCK);
    }
}
