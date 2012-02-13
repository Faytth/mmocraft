package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.ImageHandler;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public class TextCtrl extends Control {

    // Style masks
    public static final int PASSWORD = 1 << 0; // The text will be echoed as asterisks.
    
    private String label;
    private int textOffsetX = 4;
    private int textOffsetY = 2;
    private int style = 0;
    
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
     * @param style The style masks for this text control
     */
    public TextCtrl(GUIElement parent, EventIntf intf, GameContainer container
            , String label, float x, float y, int width
            , int height, String background, String background_highlighted
            , String background_selected, int style) {
        super(parent, intf, container, x, y, width, height, background
                , background_highlighted, background_selected);
        
        this.label = "";
        this.style = style;
        renderImage();
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void keyPressed(int key, char c) {
        
        processKeyEvent(key, c);
    }

    /**
     * Processes a key event, possibly changing the text label
     * @param key
     * @param c
     */
    private void processKeyEvent(int key, char c) {
        if( GUIUtility.getInstance().isActiveElement(this) ) {
            if( key == Input.KEY_BACK && label.length() > 0 ) {
                label = label.substring(0, label.length()-1);
            } else if( c >= 0x20 && c <= 0x7F ){
                label += c;
            }
            renderImage();
        }
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void renderImage() {
        ImageHandler handler = ImageHandler.getInstance();
        Graphics g;
        
        try {
            if( image == null ) {
                image = new Image(width, height);
            }
            g = image.getGraphics();
        
            String str = label; // The string to draw on the screen
            
            // Password mask check
            if( (this.style & PASSWORD) > 0 ) {
                str = str.replaceAll(".", "*");
            }
            
            // Draw background
            if( GUIUtility.getInstance().isActiveElement(this) ) {
                g.drawImage(handler.getImage(background_selected), 0, 0);
                str += "|"; // The pipe is for showing the "active" cursor location
            } else if( highlighted ) {
                g.drawImage(handler.getImage(background_highlighted), 0, 0);
            } else {
                g.drawImage(handler.getImage(background), 0, 0);
            }
            
            // Draw text
            g.drawString(str, textOffsetX, textOffsetY);
            g.flush();
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
