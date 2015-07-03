package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.node.Node;

public class ListCtrl extends Control {
    
    /**
     * The top node to render.
     */
//    private int position = 0;

    /**
     * 
     * @param container The GameContainer that's passed from a state's init()
     * @param label The TextCtrl label.  This is what the user can change.
     * @param x The x offset for this control.
     * @param y The y offset for this control.
     * @param width The width of this control.  If -1, then it's calculated from background
     * @param height The height of this control.  If -1, then it's calculated from background
     */
    public ListCtrl(GUIElement parent, EventIntf intf, GameContainer container, 
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height, null, null, null);
        
        needsRefresh = true;
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Adds a single node to the list of nodes.  This node will appear on its
     * own line.
     * @param node The node to add to its own line.
     */
    public void addNode(Node node) {
        if (node != null) {
            elements.add(node);
        }
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        int offX = getAbsoluteX();
        int offY = getAbsoluteY();
                
        // Now render the nodes
        int heightNodeOffset = 0;
        for (GUIElement element : elements) {
        	if (element instanceof Node) {
        		Node node = (Node)element;
	            int lineOffset = 0;
	            if (heightNodeOffset + node.getHeight() > height) {
	                break;
	            }
	            node.render(g,  offX, heightNodeOffset + offY, height - heightNodeOffset);
	            lineOffset = node.getHeight() > lineOffset ? node.getHeight() : lineOffset;
	            heightNodeOffset += lineOffset;
	            node.render(container, game, g);
        	}
        }
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }

    @Override
    public void renderImage(Graphics image) {
    }
    
    /**
     * Clears all of the nodes in the ListCtrl.
     */
    public void clear() {
        elements.clear();
    }
}