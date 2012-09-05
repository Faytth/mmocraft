package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public abstract class Frame extends GUIElement {

	/**
	 * Keeps track of all frames and their position relative to one another.
	 * This is used when determining which frame to draw first as well as which
	 * frame has "focus."
	 * FIXME:  Implement this!
	 */
	public static List<Frame> zIndex = new ArrayList<Frame>();
	
    public Frame(Frame parent, EventIntf intf, GameContainer container,
            float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);
        
        /* We only draw frames that can obtain focus.  Otherwise it's part
         * of the "background" and is responsible for drawing itself
         */
        if (isAcceptingFocus()) {
        	zIndex.add(0, this);
        }
    }
    
    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
        if (isAcceptingFocus() && this.containsPoint(x, y)) {
            int i = zIndex.indexOf(this);
            if (i >= 0) {
            	zIndex.remove(i);
            	zIndex.add(0, this); // insert at the front
            }
        }
    }

    protected abstract boolean isAcceptingFocus();
    
    @Override
    public void destroy() {
    	// Remove self from the zIndex
    	zIndex.remove(this);
    	
	    // Destroy our children
	    for(GUIElement element : elements) {
	        element.destroy();
	    }
	    
	    if (isHandler) {
	        container.getInput().removeListener(this);
	    }
    }
    
    @Override
    public void keyPressed(int key, char c) {
        // Respond to tab events
        if( key == Input.KEY_TAB ) {
            /* 
             * Cycle through all children searching for active element.
             * If found, then we set the next in line to the active element
             */
            
            // Make a list of all elements that are accepting tabs
            List<GUIElement> tabElements = new ArrayList<GUIElement>();
            for (GUIElement element : elements) {
                if (element.isAcceptingTab()) {
                    tabElements.add(element);
                }
            }
            
            boolean lastElementIsActive = false;
            GUIUtility util = GUIUtility.getInstance();
            if (tabElements.size() > 0) {
                GUIElement prevElement = tabElements.get(tabElements.size()-1); // start at last
                for(GUIElement element : tabElements) {
                    if( util.isActiveElement(element) ) {
                        Input input = Game.getInstance().getContainer().getInput();
                        if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {
                            util.setActiveElement(prevElement);
                            break;
                        } else {
                            lastElementIsActive = true;
                        }
                    } else if(lastElementIsActive) {
                        util.setActiveElement(element);
                        // We're done
                        lastElementIsActive = false;
                        break;
                    }
                    prevElement = element;
                }
                // If we're not done yet, set the FIRST element to active
                if( lastElementIsActive ) {
                    for(GUIElement element : tabElements) {
                        util.setActiveElement(element);
                        break; // Done
                    }
                }
            }
        }
    }

}
