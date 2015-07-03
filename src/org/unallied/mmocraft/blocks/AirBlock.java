package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.items.ItemData;

/**
 * An "air" block.  The most common type of block.  It is essentially the absence
 * of some other type of block.
 * @author Faythless
 *
 */
public class AirBlock extends Block {
    private static final BlockType type = BlockType.AIR;

    public AirBlock() {
    }

    @Override
    public Image getImage() {
        return ImageHandler.getInstance().getImage(ImageID.BLOCK_AIR.toString());
    }

    @Override
    public BlockType getType() {
        return type;
    }
    
    @Override
    public boolean isCollidable() {
        return false; // You can't collide with air!
    }
    
    @Override
    public long getMaximumHealth() {
    	return 0;
    }

    @Override
    public Block getCopy() {
        return new AirBlock();
    }

    @Override
    public ItemData getItem() {
        return null;
    }
}
