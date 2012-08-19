package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * An "air" block.  The most common type of block.  It is essentially the absence
 * of some other type of block.
 * @author Faythless
 *
 */
public class AirBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_AIR.toString());
    private static final BlockType type = BlockType.AIR;

    public AirBlock() {
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
    public boolean isCollidable() {
        return false; // You can't collide with air!
    }
}
