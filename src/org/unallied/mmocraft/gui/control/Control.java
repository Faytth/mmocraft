package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImagePool;
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
    protected boolean mouseDown = false; // true if the mouse was down for the last update
    protected boolean activeState = false; // true if the control is active
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
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
    	if (!hidden) {
	        boolean newActiveState;
	        
	        GUIUtility util = GUIUtility.getInstance();
	        newActiveState = util.isActiveElement(this);
	        
	        if( activeState != newActiveState ) {
	            activeState = newActiveState;
	            needsRefresh = true;
	        }
	        
	        // Perform normal render stuff
	        Image image = ImagePool.getInstance().getImage(this, width, height);
	        if( image != null) {
	            if (ImagePool.getInstance().needsRefresh() || needsRefresh) {
	                renderImage(image);
	                needsRefresh = false;
	            }
	            image.draw(getAbsoluteWidth(), getAbsoluteHeight());
	        }
	        renderToolTip(container, game, g);
    	}
    }
    
    @Override
    /**
     * Provides a means for mouse selection / highlighting
     */
    public void update(GameContainer container) {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean newMouseDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        boolean newHighlighted;
        boolean newActiveState = false;
        
        // Check if selected / deselected
        if( mouseDown && !newMouseDown && this.isAcceptingInput() ) {
            GUIUtility util = GUIUtility.getInstance();
            if( containsPoint(mouseX, mouseY) && isShown() ) {
                util.setActiveElement(this);
                newActiveState = true;
            } else if( util.isActiveElement(this)) {
                util.setActiveElement(null);
                newActiveState = false;
            }
        }
        
        newHighlighted = containsPoint(mouseX, mouseY);
        
        needsRefresh |= ( highlighted != newHighlighted || newActiveState != activeState);
        highlighted = newHighlighted;
        activeState = newActiveState;
        
        mouseDown = newMouseDown;
    }
}
