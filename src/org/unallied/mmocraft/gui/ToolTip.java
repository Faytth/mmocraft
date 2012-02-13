package org.unallied.mmocraft.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.FontHandler;
import org.unallied.mmocraft.FontID;

public class ToolTip {

    private String tip; // The tip to display
    private Image panelBackground = null; // The panel to use for drawing the background (no text)
    private int rectOffX = 15; // The x offset of the entire tip
    private int rectOffY = 15; // The y offset of the entire tip
    private int tipOffsetX = 4; // X offset of text from top-left corner of tip
    private int tipOffsetY = 1; // Y offset of text from top-left corner of tip
    private int toolWidth = 160; // Width of the tooltip
    private int toolHeight = 80; // Height of the tooltip
    
    public ToolTip(String tip) {
        this.tip = tip;
        renderImage();
    }
    
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) throws SlickException {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        panelBackground.draw(rectOffX + mouseX, rectOffY + mouseY);
        FontHandler.getInstance().draw(FontID.TOOLTIP_DEFAULT.toString()
                , tip
                , rectOffX + mouseX + tipOffsetX, rectOffY + mouseY + tipOffsetY
                , new Color(200, 248, 220), toolWidth-tipOffsetX*2, toolHeight-tipOffsetY*2, true);        
    }
    
    public void renderImage() {
        try {
            if( panelBackground != null ) {
                panelBackground.destroy();
            }
            
            panelBackground = new Image(toolWidth, toolHeight);
            
            if( panelBackground == null ) {
                panelBackground = new Image(toolWidth, toolHeight);
            }
            
            // Do background
            Graphics g = panelBackground.getGraphics();
            
            g.fill(new Rectangle(0, 0, toolWidth, toolHeight)
                    , new GradientFill(0, 0, new Color(47, 102, 176)
                    , toolWidth, toolHeight, new Color(99, 47, 176), true));
            panelBackground.setAlpha(0.65f);
            g.flush();
            
        } catch (SlickException e) {
            
        }
    }
    
}
