package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * Gravel.  This is used for roads and such.
 * @author Faythless
 *
 */
public class GravelBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_GRAVEL.toString());
    private static final BlockType type = BlockType.GRAVEL;

    public GravelBlock() {
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
        return new GravelBlock();
    }
}
