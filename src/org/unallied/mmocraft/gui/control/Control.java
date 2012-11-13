package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

/**
 * A styleable control that all control objects should be derived from.
 * This class provides the means for setting the background, width, and height.
 * It also provides a means for activating the event by mouse click.
 * @author Faythless
 *
 */
public abstract class Control extends GUIElement {
    // Style masks
    public static final int PASSWORD = 1 << 0; // The text will be echoed as asterisks.

    protected String background;
    protected String background_highlighted;
    protected String background_selected;
    protected boolean highlighted = false; // true if the control is highlighted
    //protected boolean mouseDown = false; // true if the mouse was down for the last update
    protected int textOffsetX = 4;
    protected int textOffsetY = 2;
    
    /**
     * 
     * @param container The GameContainer that's passed from a state's init()
     * @param label The TextCtrl label.  This is what the user can change.
     * @param x The x offset for this control.
     * @param y The y offset for this control.
     * @param width The width of this control.  If -1, then it's calculated from background
     * @param height The height of this control.  If -1, then it's calculated from background
     * @param background The normal background to display when the control is not highlighted / active
     * @param background_highlighted The background to display when the mouse cursor is over the control.
     * @param background_selected The background for the active element.
     */
    public Control(GUIElement parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height, String background
            , String background_highlighted, String background_selected) {
        super(parent, intf, container, x, y, width, height);
        
        ImageHandler handler = ImageHandler.getInstance();
        
        // Default size, so calculate the default
        if( width == -1 ) {
            this.width = handler.getWidth(background);
        }
        if( height == -1 ) {
            this.height = handler.getHeight(background);
        }
        this.background = background;
        this.background_highlighted = background_highlighted;
        this.background_selected = background_selected;
    }
    
    @Override
    /**
     * We need to check each render to make sure we didn't change states
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
    	if (!hidden) {
	        // Perform normal render stuff
            g.translate(getAbsoluteWidth(), getAbsoluteHeight());
            renderImage(g);
            g.flush();
            g.translate(-getAbsoluteWidth(), -getAbsoluteHeight());
//	        renderToolTip(container, game, g);
    	}
    }
   
    
    @Override
    public boolean mousePressed(int button, int x, int y) {
        if( this.containsPoint(x, y) ) {
            return true;
        }
        return super.mousePressed(button, x, y);
    }
    
    @Override
    public boolean mouseReleased(int button, int x, int y) {
    	if (this.isAcceptingInput()) {
    		GUIUtility util = GUIUtility.getInstance();

    		highlighted = containsPoint(x, y);
    		
    		if (containsPoint(x, y) && isShown() && isAcceptingFocus()) {
                util.setActiveElement(this);
                return true;
            } else if (util.isActiveElement(this)) {
                util.setActiveElement(null);
            }
    	}
    	
    	return super.mouseReleased(button, x, y);
    }
    
    @Override
    public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
    	if (this.isAcceptingInput()) {
    		boolean newHighlighted = containsPoint(newx, newy);
    		if (highlighted != newHighlighted) {
    			highlighted = newHighlighted;
    			return true;
    		}
    	}

    	return super.mouseMoved(oldx, oldy, newx, newy);
    }
    
    
    @Override
    public void update(GameContainer container) {

    }
}
