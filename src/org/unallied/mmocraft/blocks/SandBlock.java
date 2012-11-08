package org.unallied.mmocraft.blocks;

import org.newdawn.slick.Image;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;

/**
 * Sand.  Typically found in the desert and near beaches / oceans.
 * @author Faythless
 *
 */
public class SandBlock extends Block {
    private static final Image image = ImageHandler.getInstance().getImage(
            ImageID.BLOCK_SAND.toString());
    private static final BlockType type = BlockType.SAND;

    public SandBlock() {
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
        return new SandBlock();
    }
}
