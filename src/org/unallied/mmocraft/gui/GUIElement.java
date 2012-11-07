package org.unallied.mmocraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.tooltips.ToolTip;

/**
 * A generic class for all GUI elements, such as frames and controls.
 * @author Faythless
 *
 */
public abstract class GUIElement {
    
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
            //container.getInput().addListener(this);
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
        try {
            // Destroy our children
            for(GUIElement element : elements) {
                element.destroy();
            }
            if (isHandler) {
                //container.getInput().removeListener(this);
            }
        } catch (Throwable t) {
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
	            int offX = getAbsoluteWidth();
	            int offY = getAbsoluteHeight();
                g.translate(offX, offY);
                renderImage(g);
                g.flush();
                g.resetTransform();
	        }
	        
	        // Iterate over all GUI controls and inform them of input
			if (elements != null) {
				for(int i = elements.size() - 1; i >= 0; i--) {
					elements.get(i).render(container, game, g);
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
        // Guard
        if (container == null || g == null) {
            return;
        }
        
        if (!hidden) {
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
            if (elements != null) {
                for( GUIElement element : elements ) {
                    element.renderToolTip(container, game, g);
                }
            }
        }
    }
    
    /**
     * Updates the image to reflect the current state of the element
     * @param g The image to update
     */
    public abstract void renderImage(Graphics g);
    
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

        return (x >= realX && x < realX + getWidth() && y >= realY && y < realY + getHeight());
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
     * @return acceptingTab True if the control can be focused by tab, else false.
     */
    public abstract boolean isAcceptingTab();
    
    /**
     * True if this control should accept focus (keyboard input).
     * @return acceptingFocus True if the control accepts focus, else false.
     */
    public abstract boolean isAcceptingFocus();
    
    public boolean mouseClicked(int button, int x, int y, int clickCount) {
    	for (GUIElement element : elements) {
			if (element.mouseClicked(button, x, y, clickCount)) {
				return true;
			}
		}
    	return false;
    }

    public boolean mouseDragged(int oldx, int oldy, int newx, int newy) {
    	for (GUIElement element : elements) {
			if (element.mouseDragged(oldx, oldy, newx, newy)) {
				return true;
			}
		}
    	return false;
    }

    public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
    	for (GUIElement element : elements) {
			if (element.mouseMoved(oldx, oldy, newx, newy)) {
				return true;
			}
		}
    	return false;
    }

    public boolean mousePressed(int button, int x, int y) {
    	for (GUIElement element : elements) {
			if (element.mousePressed(button, x, y)) {
				return true;
			}
		}
    	return false;
    }

    public boolean mouseReleased(int button, int x, int y) {
    	for (GUIElement element : elements) {
			if (element.mouseReleased(button, x, y)) {
				return true;
			}
		}
    	return false;
    }
    
    public boolean mouseWheelMoved(int change) {
    	for (GUIElement element : elements) {
			if (element.mouseWheelMoved(change)) {
				return true;
			}
		}
    	return false;
    }

    public boolean inputEnded() {
    	for (GUIElement element : elements) {
			if (element.inputEnded()) {
				return true;
			}
		}
    	return false;
    }
    
    public boolean inputStarted() {
    	for (GUIElement element : elements) {
			if (element.inputStarted()) {
				return true;
			}
		}
    	return false;
    }

    public boolean setInput(Input input) {
    	return false;
    }

    public boolean keyPressed(int key, char c) {
    	for (GUIElement element : elements) {
			if (element.keyPressed(key, c)) {
				return true;
			}
		}
    	return false;
    }
    
    public boolean keyReleased(int key, char c) {
    	for (GUIElement element : elements) {
			if (element.keyReleased(key, c)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerButtonPressed(int controller, int button) {
    	for (GUIElement element : elements) {
			if (element.controllerButtonPressed(controller, button)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerButtonReleased(int controller, int button) {
    	for (GUIElement element : elements) {
			if (element.controllerButtonReleased(controller, button)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerDownPressed(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerDownPressed(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerDownReleased(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerDownReleased(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerLeftPressed(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerLeftPressed(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerLeftReleased(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerLeftReleased(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerRightPressed(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerRightPressed(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerRightReleased(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerRightReleased(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerUpPressed(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerUpPressed(controller)) {
				return true;
			}
		}
    	return false;
    }

    public boolean controllerUpReleased(int controller) {
    	for (GUIElement element : elements) {
			if (element.controllerUpReleased(controller)) {
				return true;
			}
		}
    	return false;
    }
    
    public abstract boolean isAcceptingInput();
    
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
    	show(true);
    }
    
    /**
     * Shows or hides the element.  When an element is showing, it will be rendered.
     * @param show True if the element should be shown, else false.
     */
    public void show(boolean show) {
        hidden = !show;
    }
    
    /**
     * Hides the element.  When an element is hidden, it will not be rendered.
     */
    public void hide() {
    	show(false);
    }
    
    /**
     * Returns whether the GUI element is being shown (rendered).
     * This also checks its parent.
     * @return shown: true if being rendered, else false
     */
    public boolean isShown() {
        if (parent != null) {
            return !hidden && parent.isShown();
        }
        
    	return !hidden;
    }
    
    /**
     * Retrieves the x coordinate of this frame's position.
     * @return x
     */
    public int getX() {
        return (int) x;
    }
    
    /**
     * Retrieves the y coordinate of this frame's position.
     * @return y
     */
    public int getY() {
        return (int) y;
    }
    
    /**
     * Sets this GUI element's x position.
     * @param x The new x position.
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Sets this GUI element's y position.
     * @param y The new y position.
     */
    public void setY(int y) {
        this.y = y;
    }
}
