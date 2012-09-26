package org.unallied.mmocraft.gui.tooltips;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;

public class ToolTip {

    protected String tip; // The tip to display
    protected int rectOffX = 15; // The x offset of the entire tip
    protected int rectOffY = 15; // The y offset of the entire tip
    protected int tipOffsetX = 4; // X offset of text from top-left corner of tip
    protected int tipOffsetY = 1; // Y offset of text from top-left corner of tip
    protected int toolWidth = 160; // Width of the tooltip
    protected int toolHeight = 80; // Height of the tooltip
    
    public ToolTip(String tip) {
        this.tip = tip;
        toolWidth = FontHandler.getInstance().getMaxWidth(FontID.TOOLTIP_DEFAULT.toString(), this.tip) + tipOffsetX*2;
        toolHeight = FontHandler.getInstance().getMaxHeight(FontID.TOOLTIP_DEFAULT.toString(), this.tip) + tipOffsetY*2;
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        g.fill(new Rectangle(rectOffX + mouseX, rectOffY + mouseY, toolWidth, toolHeight), 
        		new GradientFill(0, 0, new Color(47, 102, 176, 166), 
        				toolWidth/2, toolHeight/2, new Color(99, 47, 176, 166), true));

        FontHandler.getInstance().draw(FontID.TOOLTIP_DEFAULT.toString(), tip, 
        		rectOffX + mouseX + tipOffsetX, rectOffY + mouseY + tipOffsetY, 
        		new Color(200, 248, 220), toolWidth-tipOffsetX*2, toolHeight-tipOffsetY*2, true);
    }
}
