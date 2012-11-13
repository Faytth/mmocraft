package org.unallied.mmocraft.gui.node;

import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.gui.GUIElement;

/**
 * A node which knows its width and height.  Nodes are used for drawing elements
 * of a control, such as a list control or tooltip.  This way the control only
 * needs to know the width and height of the node to place it.  The node
 * takes care of rendering itself.
 * 
 * @author Alexandria
 *
 */
public abstract class Node extends GUIElement {
    public Node(GUIElement parent, EventIntf intf) {
		super(parent, intf, null, 0, 0, -1, -1);
	}

	/**
     * Renders this node to the given graphics context.
     * @param g The graphic context to render to
     * @param offX The starting x position of the screen in pixels to begin rendering.
     * @param offY The starting y position of the screen in pixels to begin rendering.
     * @param maxHeight The maximum height that this node should take up.
     */
    public abstract void render(Graphics g, int offX, int offY, int maxHeight);
}
