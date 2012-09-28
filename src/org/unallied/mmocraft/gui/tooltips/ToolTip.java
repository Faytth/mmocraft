package org.unallied.mmocraft.gui.tooltips;

import java.util.ArrayList;
import java.util.List;

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
import org.unallied.mmocraft.gui.Node;
import org.unallied.mmocraft.gui.StringNode;

public class ToolTip {

    protected int rectOffX = 10; // The x offset of the entire tip
    protected int rectOffY = 10; // The y offset of the entire tip
    protected int tipOffsetX = 4; // X offset of text from top-left corner of tip
    protected int tipOffsetY = 1; // Y offset of text from top-left corner of tip
    protected int maxWidth = 300; // Max width of the tooltip
    protected int toolWidth = maxWidth; // Width of the tooltip
    protected int toolHeight = 80; // Height of the tooltip
    
    /**
     * A container for nodes.  This lets us have multiple nodes on the same line.
     * @author Alexandria
     *
     */
    protected class NodeContainer {
        private Node node = null;
        private NodeContainer next = null;
        
        public NodeContainer(Node node, NodeContainer next) {
            this.node = node;
            this.next = next;
        }
    }
    
    private List<NodeContainer> nodes = new ArrayList<NodeContainer>();
    
    public ToolTip(String tip) {
        nodes.add(new NodeContainer(new StringNode(tip, new Color(200, 248, 220), 
                FontHandler.getInstance().getFont(FontID.TOOLTIP_DEFAULT.toString()), maxWidth - tipOffsetX*2), null));
        toolWidth  = nodes.get(0).node.getWidth() + tipOffsetX*2;
        toolHeight = nodes.get(0).node.getHeight() + tipOffsetY*2;
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        // Determine which corner to render at based on mouse location
        int offX = mouseX > container.getWidth()/2 ? mouseX - toolWidth - rectOffX : rectOffX + mouseX;
        int offY = mouseY > container.getHeight()/2 ? mouseY - toolHeight - rectOffY : rectOffY + mouseY;
        
        g.setColor(new Color(0, 178, 200));
        g.drawRect(offX,  offY, toolWidth, toolHeight);
        g.setColor(new Color(255, 255, 255, 255));
        g.fill(new Rectangle(offX, offY, toolWidth, toolHeight), 
        		new GradientFill(0, 0, new Color(0, 10, 76, 106), 
        				toolWidth/2, toolHeight/2, new Color(0, 10, 76, 106), true));
        
        offX += tipOffsetX;
        offY += tipOffsetY;
        
        // Now render the nodes
        int heightNodeOffset = 0;
        for (NodeContainer node : nodes) {
            do {
                node.node.render(g,  offX, offY + heightNodeOffset, toolHeight - heightNodeOffset - tipOffsetY*2);
                heightNodeOffset += node.node.getHeight();
            } while ((node = node.next) != null); 
        }
    }
}
