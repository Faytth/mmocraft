package org.unallied.mmocraft.gui.control;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.Node;

public class ListCtrl extends Control {
    
    /**
     * The top node to render.
     */
//    private int position = 0;
    
    /** All of the nodes in this list control. */
    private List<Node> nodes = new ArrayList<Node>();

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
            nodes.add(node);
        }
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        int offX = getAbsoluteWidth();
        int offY = getAbsoluteHeight();
                
        // Now render the nodes
        int heightNodeOffset = 0;
        for (Node node : nodes) {
            int lineOffset = 0;
            node.render(g,  offX, heightNodeOffset + offY, height - heightNodeOffset);
            lineOffset = node.getHeight() > lineOffset ? node.getHeight() : lineOffset;
            heightNodeOffset += lineOffset;
        }
    }

    @Override
    public boolean isAcceptingFocus() {
        return true;
    }

    @Override
    public void renderImage(Graphics image) {
    }
    
    /**
     * Clears all of the nodes in the ListCtrl.
     */
    public void clear() {
        nodes.clear();
    }
}