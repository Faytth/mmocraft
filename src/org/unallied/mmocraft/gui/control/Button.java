package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public class Button extends Control {

    private String label;
    private int textOffsetX = 4;
    private int textOffsetY = 2;
    //private int style = 0;
    private boolean activated = false; // true if the mouse is being held down over the element
    
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
     * @param style Reserved
     */
    public Button(GUIElement parent, EventIntf intf, GameContainer container
            , String label, float x, float y, int width
            , int height, String background, String background_highlighted
            , String background_selected, int style) {
        super(parent, intf, container, x, y, width, height
                , background, background_highlighted, background_selected);
        this.label = label;
        renderImage();
        //this.style = style;
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if( this.containsPoint(x, y) ) {
            activated = true;
            renderImage();
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if( this.containsPoint(x, y) ) {
            callback(new Event(this, EventType.BUTTON));
        }
        if( activated ) {
            activated = false;
            renderImage();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if( key == Input.KEY_RETURN && GUIUtility.getInstance().isActiveElement(this)) {
            callback(new Event(this, EventType.BUTTON));
        }
    }

    
    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }

    @Override
    public void renderImage() {
        ImageHandler handler = ImageHandler.getInstance();
        
        try {
            if( image == null ) {
                image = new Image(width, height);
            }
            Graphics g = image.getGraphics();
                        
            // Draw background
            if( activated ) {
                g.drawImage(handler.getImage(background_selected), 0, 0);
            } else if( highlighted ) {
                g.drawImage(handler.getImage(background_highlighted), 0, 0);
            } else {
                g.drawImage(handler.getImage(background), 0, 0);
            }
            
            // Draw text
            g.drawString(label, textOffsetX, textOffsetY);
            g.flush();
        } catch( SlickException e ) {
            
        }
        
    }

}
