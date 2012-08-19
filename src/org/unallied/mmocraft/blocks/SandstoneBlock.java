package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

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
    }

    @Override
    protected Image getImage() {
        return image;
    }

    @Override
    protected BlockType getType() {
        return type;
    }
}
