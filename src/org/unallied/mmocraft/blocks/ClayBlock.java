package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * A "clay" block.  Typically found underneath water or in the desert(?).
 * @author Faythless
 *
 */
public class ClayBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_CLAY.toString());
    private static final BlockType type = BlockType.CLAY;

    public ClayBlock() {
    }

    @Override
    protected Image getImage() {
        return image;
    }

    @Override
    protected BlockType getType() {
        return type;
    }

    @Override
    public Block getCopy() {
        return new ClayBlock();
    }
}
