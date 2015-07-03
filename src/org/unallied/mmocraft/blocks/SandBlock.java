package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * Sand.  Typically found in the desert and near beaches / oceans.
 * @author Faythless
 *
 */
public class SandBlock extends Block {
    private static final BlockType type = BlockType.SAND;

    public SandBlock() {
        maximumHealth = 30;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_SAND.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new SandBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.SAND_BLOCK);
    }
}
