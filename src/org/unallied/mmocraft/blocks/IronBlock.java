package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

public class IronBlock extends Block {
    private static final BlockType type = BlockType.DIRT;

    public IronBlock() {
        maximumHealth = 200;
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_IRON.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new IronBlock();
    }

    @Override
    public ItemData getItem() {
        return ItemManager.getItemData(ItemID.IRON_BLOCK);
    }
}
