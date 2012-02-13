package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.GUIElement;

public class StaticText extends GUIElement {

    private String label = null;
    
    /**
     * Creates a static text control, which displays a text message.
     * @param x The x offset for this control (from the parent GUI element)
     * @param y The y offset for this control (from the parent GUI element)
     * @param width The width for this control
     * @param height The height for this control
     * @param label The message to display
     */
    public StaticText(final GUIElement parent, EventIntf intf,GameContainer container
            , String label, float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        this.label = label;
    }
    
    @Override
    public void update(GameContainer container) {
        // Static text does not respond to events (except tooltips)
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        int absX = getAbsoluteWidth();
        int absY = getAbsoluteHeight();
        
        g.drawString( label, absX, absY);
        
        // Render tooltip
        renderToolTip(container, game, g);
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
    public void renderImage() {
        // TODO Auto-generated method stub
        
    }

}
