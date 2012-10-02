package org.unallied.mmocraft.gui.tooltips;

import java.util.ArrayList;
import java.util.Collection;
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
    protected int toolWidth = 0; // Width of the tooltip
    protected int toolHeight = tipOffsetY * 2; // Height of the tooltip
    
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
    
    public ToolTip() {
    	
    }
    
    public ToolTip(String tip) {
        nodes.add(new NodeContainer(new StringNode(tip, new Color(200, 248, 220), 
                FontHandler.getInstance().getFont(FontID.TOOLTIP_DEFAULT.toString()), maxWidth - tipOffsetX*2), null));
        toolWidth  = nodes.get(0).node.getWidth() + tipOffsetX*2;
        toolHeight = nodes.get(0).node.getHeight() + tipOffsetY*2;
    }
    
    /**
     * Adds a single node to the list of nodes.  This node will appear on its
     * own line.
     * @param node The node to add to its own line.
     */
    public void addNode(Node node) {
        if (node != null) {
            nodes.add(new NodeContainer(node, null));
            toolHeight += node.getHeight();
            toolWidth = toolWidth < node.getWidth() + tipOffsetX*2 ? node.getWidth() + tipOffsetX*2 : toolWidth;
        }
    }
    
    /**
     * Adds a collection of nodes to the list of nodes.  All of these nodes will appear
     * on the same line.
     * @param nodeCollection The collection of nodes to add to a single line.
     */
    public void addNodes(Collection<Node> nodeCollection) {
        addNodes((Node[])nodeCollection.toArray());
    }
    
    /**
     * Adds an array of nodes to the list of nodes.  All of these nodes will appear
     * on the same line.
     * @param nodeCollection The collection of nodes to add to a single line.
     */
    public void addNodes(Node[] nodeCollection) {
    	NodeContainer first = null;
        NodeContainer cur = null;
        int lineOffset = 0;
        int lineWidth = tipOffsetX * 2;
        for (Node node : nodeCollection) {
            if (node != null) {
            	lineOffset = node.getHeight() > lineOffset ? node.getHeight() : lineOffset;
            	lineWidth += node.getWidth();
                if (cur == null || first == null) {
                    first = new NodeContainer(node, null);
                    cur = first;
                } else {
                    cur.next = new NodeContainer(node, null);
                    cur = cur.next;
                }
            }
        }
        toolHeight += lineOffset;
        toolWidth = toolWidth < lineWidth ? lineWidth : toolWidth;
        if (first != null) {
            nodes.add(first);
        }
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        // Determine which corner to render at based on mouse location
        int offX = mouseX > container.getWidth()/2 ? mouseX - toolWidth - rectOffX : rectOffX + mouseX;
        int offY = mouseY > container.getHeight()/2 ? mouseY - toolHeight - rectOffY : rectOffY + mouseY;
        
        // Render background
        g.fill(new Rectangle(offX, offY, toolWidth, toolHeight), 
        		new GradientFill(0, 0, new Color(0, 10, 46, 206), 
        				toolWidth/2, toolHeight/2, new Color(0, 10, 46, 206), true));
        // Render border
        g.setColor(new Color(0, 178, 200));
        g.drawRect(offX,  offY, toolWidth, toolHeight);
        g.setColor(new Color(255, 255, 255, 255));
        
        offX += tipOffsetX;
        offY += tipOffsetY;
        
        // Now render the nodes
        int heightNodeOffset = 0;
        for (NodeContainer node : nodes) {
        	int lineOffset = 0;
        	int widthOffset = 0;
            do {
                node.node.render(g,  offX + widthOffset, offY + heightNodeOffset, toolHeight - heightNodeOffset - tipOffsetY*2);
                lineOffset = node.node.getHeight() > lineOffset ? node.node.getHeight() : lineOffset;
                widthOffset += node.node.getWidth();
            } while ((node = node.next) != null); 
            heightNodeOffset += lineOffset;
        }
    }
}
