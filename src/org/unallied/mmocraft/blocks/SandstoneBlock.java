package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * Sandstone.  This is "compressed" sand, and can be found under about 3 or 4
 * blocks of sand.
 * @author Faythless
 *
 */
public class SandstoneBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_SANDSTONE.toString());
    private static final BlockType type = BlockType.SANDSTONE;

    public SandstoneBlock() {
        maximumHealth = 60;
    }

    @Override
    protected Image getImage() {
        return image;
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new SandstoneBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.SANDSTONE_BLOCK);
    }
}
