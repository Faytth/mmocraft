package org.unallied.mmocraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.ImagePool;

/**
 * A generic class for all GUI elements, such as frames and controls.
 * @author Faythless
 *
 */
public abstract class GUIElement implements InputListener {
    
    protected List<GUIElement> elements = new ArrayList<GUIElement>();
    
    /**
     * When set to true, this forces a refresh of the image on the next render.
     */
    protected boolean needsRefresh = true;
    
    /** True if the element should be "hidden" (not rendered), else false. */
    protected boolean hidden = false;
    
    public interface EventIntf {
        public void callback(Event event);
    }
    
    /**
     * An event class used in event callbacks
     * @author Faythless
     *
     */
    public class Event {
        private GUIElement element = null; // The element associated with the event
        private EventType id; // An event ID specifying what type of event this is
        
        public Event(GUIElement element, EventType id) {
            this.element = element;
            this.id = id;
        }
        
        /**
         * Returns the GUI element associated with this event
         * @return the GUI element or null if no element
         */
        public GUIElement getElement() {
            return element;
        }
        
        public EventType getId() {
            return id;
        }
    }

    protected float x; // The x offset of this GUI element
    protected float y; // The y offset of this GUI element
    protected int width; // The width of the element
    protected int height; // The height of the element
    protected int id; // The id of this GUIElement.  Not guaranteed to be unique
    protected ToolTip toolTip = null; // The tooltip to display for this element
    protected final GUIElement parent; // The parent element.  This is used in callbacks
    
    protected GameContainer container = null; // Stores a reference to the game container
    protected boolean isHandler = false; // Stores whether this control handles events
    private EventIntf intf; // Used for event callbacks
    
    /**
     * Creates a GUIElement
     * @param parent The parent element.  This is used in callbacks.  Can be null.
     * @param intf The event interface (callback) for this element.  Can be null.
     * @param container The input container which we will listen on
     * @param x The x offset of this GUI element
     * @param y The y offset of this GUI element
     * @param width The width of this GUI element
     * @param height The height of this GUI element
     */
    public GUIElement(final GUIElement parent, EventIntf intf, GameContainer container, float x
            , float y, int width, int height) {
        this.parent = parent;
        this.intf = intf;
        this.container = container;
        if( container != null ) {
            isHandler = true;
            container.getInput().addListener(this);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * This should be called when this element is being destroyed.  By calling
     * this, the GUI element is able to properly remove itself from the container
     * as a listener.
     */
    public void destroy() {
        if (isHandler) {
            container.getInput().removeListener(this);
        }
    }
    
    /**
     * Returns the absolute x displacement from the top-left of the screen for
     * this element.
     * @return x displacement
     */
    public int getAbsoluteWidth() {
        int result = (int)x;
        
        // While we have a parent, add the parent's offset
        if( parent != null ) {
            result += parent.getAbsoluteWidth();
        }
        
        return result;
    }
    
    /**
     * Returns the absolute y displacement from the top-left of the screen for
     * this element.
     * @return y displacement
     */
    public int getAbsoluteHeight() {
        int result = (int)y;
        
        // While we have a parent, add the parent's offset
        if( parent != null ) {
            result += parent.getAbsoluteHeight();
        }
        
        return result;      
    }
    
    /**
     * Attaches a tooltip to the element
     * @param tip
     */
    public void setToolTip(ToolTip tip) {
        this.toolTip = tip;
    }
    
    public abstract void update(GameContainer container);
    
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
    	if (!hidden) {
	        if (width > 0 && height > 0) {
	            Image image = ImagePool.getInstance().getImage(this, width, height);
	            if( image != null) {
	                if (ImagePool.getInstance().needsRefresh() || needsRefresh) {
	                    renderImage(image);
	                    needsRefresh = false;
	                }
	                image.draw(getAbsoluteWidth(), getAbsoluteHeight());
	            }
	        }
	        
	        if (elements != null) {
	            // Iterate over all GUI controls and inform them of input
	            for( GUIElement element : elements ) {
	                element.render(container, game, g);
	            }
	            
	            for( GUIElement element : elements ) {
	                element.renderToolTip(container, game, g);
	            }
	        }
    	}
    }
    
    /**
     * Renders a tooltip if necessary.  A lot of elements use this, even if
     * they override the render function.
     */
    public void renderToolTip(GameContainer container, StateBasedGame game
            , Graphics g) {
        Input input = container.getInput();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        // Update tooltip if necessary
        if( toolTip != null ) {
            if( containsPoint(mouseX, mouseY) ) {
                try {
                    toolTip.render(container, game, g);
                } catch (SlickException e) {
                    // Failed to render tooltip.  Oh well.
                }
            }
        }
    }
    
    /**
     * Updates the image to reflect the current state of the element
     * @param image The image to update
     */
    public abstract void renderImage(Image image);
    
    /**
     * True if this element contains the given point on the screen.  Else false
     * @param x The absolute x coordinate of the point to compare to
     * @param y The absolute y coordinate of the point to compare to
     * @param parentX The offset from the left
     * @param parentY The offset from the top
     * @return True if the point is in the element; else false.
     */
    public boolean containsPoint(int x, int y) {
        float realX = getAbsoluteWidth();
        float realY = getAbsoluteHeight();

        return (x >= realX && x <= realX + width && y >= realY && y <= realY + height);
    }
    
    /**
     * Should be called by itself if a callback is needed
     * @param event
     */
    public void callback(Event event) {
        if( intf != null ) {
            intf.callback(event);
        }
    }
    
    /**
     * True if this control should accept tab events
     * @return
     */
    public abstract boolean isAcceptingTab();
    
    public void mouseClicked(int button, int x, int y, int clickCount) {
        // TODO Auto-generated method stub
        
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    public void mousePressed(int button, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void mouseReleased(int button, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public void mouseWheelMoved(int change) {
        // TODO Auto-generated method stub
        
    }

    public void inputEnded() {
        // TODO Auto-generated method stub
        
    }

    public void inputStarted() {
        // TODO Auto-generated method stub
        
    }

    public void setInput(Input input) {
        // TODO Auto-generated method stub
        
    }

    public void keyPressed(int key, char c) {
    }

    public void keyReleased(int key, char c) {
        // TODO Auto-generated method stub
        
    }

    public void controllerButtonPressed(int controller, int button) {
        // TODO Auto-generated method stub
        
    }

    public void controllerButtonReleased(int controller, int button) {
        // TODO Auto-generated method stub
        
    }

    public void controllerDownPressed(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerDownReleased(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerLeftPressed(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerLeftReleased(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerRightPressed(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerRightReleased(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerUpPressed(int controller) {
        // TODO Auto-generated method stub
        
    }

    public void controllerUpReleased(int controller) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Returns the parent to this element.
     * @return parent
     */
    public GUIElement getParent() {
        return parent;
    }
    
    /**
     * Gets the width of this control.
     * @return width
     */
    public int getWidth() {
    	return width;
    }
    
    /**
     * Gets the height of this control.
     * @return height
     */
    public int getHeight() {
    	return height;
    }
    
    /**
     * Shows the element.  When an element is showing, it will be rendered.
     */
    public void show() {
    	hidden = false;
    }
    
    /**
     * Hides the element.  When an element is hidden, it will not be rendered.
     */
    public void hide() {
    	hidden = true;
    }
    
    /**
     * Returns whether the GUI element is being shown (rendered).
     * @return shown: true if being rendered, else false
     */
    public boolean isShown() {
    	return !hidden;
    }
}
