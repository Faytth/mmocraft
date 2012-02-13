package org.unallied.mmocraft.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A generic class for all GUI elements, such as frames and controls.
 * @author Ryokko
 *
 */
public abstract class GUIElement implements InputListener {
    
    public interface EventIntf {
        public void callback(Event event);
    }
    
    /**
     * An event class used in event callbacks
     * @author Ryokko
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
    protected Image image; // The image to render for this element
    protected final GUIElement parent; // The parent element.  This is used in callbacks
    
    private EventIntf intf; // Used for event callbacks
    
    /**
     * Creates a GUIElement
     * @param parent The parent element.  This is used in callbacks
     * @param id The id of this GUIElement
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
        if( container != null ) {
            container.getInput().addListener(this);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            this.image = new Image(this.width, this.height);
        } catch (SlickException e) {
            image = null; // UH OH!
        } catch (NullPointerException e) {
            
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
        
        if( image != null ) {
            image.draw(getAbsoluteWidth(), getAbsoluteHeight());
        } else { // Fill with default image (white rectangle)
            g.fillRect(getAbsoluteWidth(), getAbsoluteHeight(), width, height);
        }
        renderToolTip(container, game, g);
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
     */
    public abstract void renderImage();
    
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
        // TODO Auto-generated method stub
        
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
}
