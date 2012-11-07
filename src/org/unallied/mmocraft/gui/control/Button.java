package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public class Button extends Control {

    private static final String BUTTON_FONT = FontID.STATIC_TEXT_MEDIUM_BOLD.toString();
    
    /** The color to render the text in. */
    private Color textColor = new Color(220, 220, 220);
    
    private String label;
    private static final int textOffsetX = 4;
    private static final int textOffsetY = 2;
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
     * @param style Reserved
     */
    public Button(GUIElement parent, EventIntf intf, GameContainer container, 
            String label, float x, float y, int width, int height, int style) {
        this(parent, intf, container, label, x, y, 
                width <= 0 ? FontHandler.getInstance().getMaxWidth(BUTTON_FONT, label) + textOffsetX * 2 : width, 
                height <= 0 ? 21 : height, null, null, null, style);
    }
    
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
        needsRefresh = true;
        //this.style = style;
    }

    @Override
    public boolean mousePressed(int button, int x, int y) {
        if( this.containsPoint(x, y) ) {
            activated = true;
            super.mousePressed(button, x, y);
            return true;
        }
        return super.mousePressed(button, x, y);
    }

    @Override
    public boolean mouseReleased(int button, int x, int y) {
        
        if( activated ) {
            activated = false;
        }
        if( this.containsPoint(x, y) ) {
            callback(new Event(this, EventType.BUTTON));
            super.mouseReleased(button, x, y);
            return true;
        }
        return super.mouseReleased(button, x, y);
    }

    @Override
    public boolean keyPressed(int key, char c) {
        if( key == Input.KEY_RETURN && GUIUtility.getInstance().isActiveElement(this)) {
            callback(new Event(this, EventType.BUTTON));
            return true;
        }
        return super.keyPressed(key, c);
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
    public void renderImage(Graphics g) {
        ImageHandler handler = ImageHandler.getInstance();
        
        try {
            if (g != null) {
                // Draw background
                Image buttonImage = null;
                if( activated ) {
                    if (background_selected != null) {
                        g.drawImage(handler.getImage(background_selected), 0, 0);
                    } else {
                        buttonImage = handler.getImage(ImageID.BUTTON_DEFAULT_SELECTED.toString());
                    }
                } else if( highlighted ) {
                    if (background_highlighted != null) {
                        g.drawImage(handler.getImage(background_highlighted), 0, 0);
                    } else {
                        buttonImage = handler.getImage(ImageID.BUTTON_DEFAULT_HIGHLIGHTED.toString());
                    }
                } else {
                    if (background != null) {
                        g.drawImage(handler.getImage(background), 0, 0);
                    } else {
                        buttonImage = handler.getImage(ImageID.BUTTON_DEFAULT_NORMAL.toString());
                    }
                }
                if (buttonImage != null) {
                    g.drawImage(buttonImage, 0, 0, 5, 21, 0, 0, 5, 21);
                    g.drawImage(buttonImage, 5, 0, width - textOffsetX, 21, 0, 21, 5, 42);
                    g.drawImage(buttonImage, width - textOffsetX, 0, width, 21, 0, 42, 5, 63);
                }
                
                // Draw text
                FontHandler.getInstance().draw(BUTTON_FONT,  label, textOffsetX, textOffsetY, 
                        textColor, -1, -1, false);
                g.flush();
            }
        } catch( Exception e ) {
            
        }
        
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }
    
    /**
     * Returns the current label (user input)
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Assigns the color to render the text in.
     * @param textColor the color to render the text in
     */
    public void setColor(Color textColor) {
        this.textColor = textColor;
        needsRefresh = true;
    }
    
    /**
     * Retrieves the TextCtrl's text color.
     * @return textColor
     */
    public Color getColor() {
        return textColor;
    }

    /**
     * Sets the text control's label.  If label is null, nothing happens.
     * Also sets the position to the very end of the new label.
     * @param label the new label
     */
    public void setLabel(String label) {
        if (label != null) {
            this.label = label;
        }
    }
}
