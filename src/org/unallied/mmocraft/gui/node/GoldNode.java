package org.unallied.mmocraft.gui.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.items.Item;

public class GoldNode extends Node {
    
    /** The amount of gold that will be rendered. */
    protected long quantity;

    String GOLD_FONT = FontID.STATIC_TEXT_MEDIUM.toString();
    
    Color GOLD_COLOR = new Color(186, 147, 18);
    
    /** 
     * The alpha level to render the gold node at.  An alpha level of 0 is
     * fully transparent.  An alpha level of 1 is fully opaque.
     */
    float alpha = 1f;
    
    public GoldNode(GUIElement parent, EventIntf intf, long quantity) {
        super(parent, intf);
        
        this.quantity = quantity;
    }
    
    @Override
    public int getWidth() {
        return FontHandler.getInstance().getMaxWidth(GOLD_FONT, 
                String.format("%s x %s", StringConstants.GOLD, 
                        Item.getShortQuantityName(quantity)));
    }
    
    @Override
    public int getHeight() {
        return FontHandler.getInstance().getLineHeight(GOLD_FONT);
    }
    
    @Override
    public void render(Graphics g, int offX, int offY, int maxHeight) {
        try {
            Font font = FontHandler.getInstance().getFont(GOLD_FONT);
            
            // Get the width of a space
            String shortQuantityName = Item.getShortQuantityName(quantity);
            int spaceWidth = font.getWidth("| |") - font.getWidth("||");
            int nameWidth = font.getWidth(StringConstants.GOLD);
            int xWidth = font.getWidth("x");
            Color nameColor = new Color(GOLD_COLOR);
            nameColor.a *= alpha;
            Color quantityColor = Item.getQuantityColor(quantity);
            quantityColor.a *= alpha;
            Color xColor = new Color(255, 255, 255);
            xColor.a *= alpha;
            
            // Render the item
            font.drawString(offX, offY, StringConstants.GOLD, nameColor);
            font.drawString(offX + nameWidth + spaceWidth, offY, "x", xColor);
            font.drawString(offX + nameWidth + spaceWidth*2 + xWidth, offY, 
                    shortQuantityName, quantityColor);
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
