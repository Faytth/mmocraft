package org.unallied.mmocraft.gui.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;

/**
 * A node that is used to render items in the inventory frame and loot frame.
 * @author Alexandria
 *
 */
public class ItemNode extends Node {
    protected Item item;

    String ITEM_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
    
    /** 
     * The alpha level to render the item node at.  An alpha level of 0 is
     * fully transparent.  An alpha level of 1 is fully opaque.
     */
    float alpha = 1f;
    
    public ItemNode(GUIElement parent, EventIntf intf, Item item) {
        super(parent, intf);
        
        this.item = item;
    }
    
    @Override
    public int getWidth() {
        int result = 0;
        if (item != null) {
            ItemData itemData = ItemManager.getItemData(item.getId());
            if (itemData != null) {
                result += FontHandler.getInstance().getMaxWidth(ITEM_FONT, 
                        String.format("%s x %s", itemData.getName(), 
                                Item.getShortQuantityName(item.getQuantity())));
            }
        }
        return result;
    }
    
    @Override
    public int getHeight() {
        return FontHandler.getInstance().getLineHeight(ITEM_FONT);
    }
    
    @Override
    public void render(Graphics g, int offX, int offY, int maxHeight) {
        try {
            ItemData itemData = ItemManager.getItemData(item.getId());
            if (itemData != null) {
                Font font = FontHandler.getInstance().getFont(ITEM_FONT);
                
                // Get the width of a space
                String shortQuantityName = Item.getShortQuantityName(item.getQuantity());
                int spaceWidth = font.getWidth("| |") - font.getWidth("||");
                int nameWidth = font.getWidth(itemData.getName());
                int xWidth = font.getWidth("x");
                Color nameColor = itemData.getQuality().getColor();
                nameColor.a *= alpha;
                Color quantityColor = Item.getQuantityColor(item.getQuantity());
                quantityColor.a *= alpha;
                Color xColor = new Color(255, 255, 255);
                xColor.a *= alpha;
                
                // Render the item
                font.drawString(offX, offY, itemData.getName(), nameColor);
                font.drawString(offX + nameWidth + spaceWidth, offY, "x", xColor);
                font.drawString(offX + nameWidth + spaceWidth*2 + xWidth, offY, 
                        shortQuantityName, quantityColor);
            }
        } catch (Throwable t) {
            // It's a render error.  No one cares.
        }
    }

    @Override
    public void update(GameContainer container) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void renderImage(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAcceptingFocus() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Sets the alpha level.  This should be a value between 0 and 1.  0 is
     * fully transparent, 1 is fully opaque.
     * @param alpha
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
