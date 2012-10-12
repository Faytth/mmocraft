package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;

public class StaticText extends GUIElement {

    private String label = null;
    
    /**
     * The font to use when rendering the static text.
     */
    private Font font = null;
    
    /**
     * The color to use when drawing this text.
     */
    private Color color = null;
    
    /**
     * Creates a static text control, which displays a text message.
     * @param parent The parent control to this.  (Usually it's "this")
     * @param intf The event interface to use for event callbacks.
     * @param label The message to display
     * @param x The x offset for this control (from the parent GUI element)
     * @param y The y offset for this control (from the parent GUI element)
     * @param width The width for this control
     * @param height The height for this control
     * @param fontID The font ID of the font to use
     */
    public StaticText(final GUIElement parent, EventIntf intf,GameContainer container
            , String label, float x, float y, int width, int height, FontID fontID) {
        super(parent, intf, container, x, y, width, height);
        
        this.font = FontHandler.getInstance().getFont(fontID.toString());
        
        this.label = label;
    }
    
    /**
     * Creates a static text control, which displays a text message.
     * @param parent The parent control to this.  (Usually it's "this")
     * @param intf The event interface to use for event callbacks.
     * @param label The message to display
     * @param x The x offset for this control (from the parent GUI element)
     * @param y The y offset for this control (from the parent GUI element)
     * @param width The width for this control
     * @param height The height for this control
     * @param fontID The font ID of the font to use
     * @param color The color to make the text
     */
    public StaticText(final GUIElement parent, EventIntf intf,GameContainer container,
            String label, float x, float y, int width, int height, FontID fontID, 
            Color color) {
        super(parent, intf, container, x, y, width, height);
        
        this.label = label;
        this.color = color;
    }
    
    @Override
    public void update(GameContainer container) {
        // Static text does not respond to events (except tooltips)
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        int absX = getAbsoluteWidth();
        int absY = getAbsoluteHeight();
        
        Color oldColor = g.getColor();
        if (color != null) {
            g.setColor(color);
        }
        if (font != null) {
            g.setFont(font);
        }
        g.drawString( label, absX, absY);
        g.setColor(oldColor);
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void renderImage(Image image) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Returns the label's color
     * @return color the color of the label
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the color of the label
     * @param color the new color of the label
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets a new label
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
