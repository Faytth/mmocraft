package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * A "dirt" block.  One of the most common types of blocks.  Makes up most of
 * the ground.
 * @author Faythless
 *
 */
public class DirtBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_DIRT.toString());
    private static final BlockType type = BlockType.DIRT;

    public DirtBlock() {
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
        return new DirtBlock();
    }
}
