package org.unallied.mmocraft.gui.frame;

import org.newdawn.slick.GameContainer;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.tooltips.ToolTip;

/**
 * A mining frame which shows the active block for placement as well as a
 * toggle to turn mining mode on / off.  When in mining mode, the player can
 * place and destroy blocks.  There is also a toggle for weapons to deal damage
 * to blocks.
 * 
 * @author Alexandria
 *
 */
public class MiningFrame extends Frame {
    
    private Button toggleMiningButton;
    
    private Panel blockImage;
    
    public MiningFrame(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        toggleMiningButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case BUTTON:
                    
                    break;
                }
            }
        }, container, "", 0, 0, -1, -1, ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_NORMAL.toString(),
                ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_HIGHLIGHTED.toString(),
                ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_SELECTED.toString(), 0);
        toggleMiningButton.setToolTip(new ToolTip(StringConstants.TOOLTIP_MINING_BUTTON));
        
        blockImage = new Panel(this, null, container, toggleMiningButton.getWidth() + 1, 0, 
                toggleMiningButton.getWidth(), toggleMiningButton.getHeight());
        blockImage.setBackground(BlockType.DIRT.getBlock().getImage());
        
        elements.add(toggleMiningButton);
        elements.add(blockImage);
        
        this.width = blockImage.getWidth() + blockImage.getX();
        this.height = toggleMiningButton.getHeight() > blockImage.getHeight() ? 
                toggleMiningButton.getHeight() : blockImage.getHeight();
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
    
}
