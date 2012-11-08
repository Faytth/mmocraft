package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * A "grass" block.  One of the most common types of blocks.  Makes up the top layer of dirt.
 * @author Faythless
 *
 */
public class GrassBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_GRASS.toString());
    private static final BlockType type = BlockType.GRASS;

    public GrassBlock() {
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
        return new GrassBlock();
    }
}