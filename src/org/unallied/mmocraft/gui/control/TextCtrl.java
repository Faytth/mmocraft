package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public class TextCtrl extends Control {

    // Style masks
    /// The text will appear normal.
    public static final int NORMAL   = 0;
    /// The text will be replaced by asterisks.
    public static final int PASSWORD = 1 << 0; // The text will be echoed as asterisks.
    
    private String label;
    private int textOffsetX = 4;
    private int textOffsetY = 2;
    
    /**
     * The caret position.  0 is just before the first character.
     */
    private int position = 0;
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
        needsRefresh = true;
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void keyPressed(int key, char c) {
        callback(new Event(this, EventType.TEXT));
        processKeyEvent(key, c);
    }

    /**
     * Moves to the left as if Ctrl was being pressed down.  For example:
     * this  is   a  |test
     * In the above string, if | is the location of pos, then:
     * this  is   |a  test
     * the return value will be the new location of | in the above string.
     * @param str The string to parse
     * @param pos Position of the starting index
     * @return index
     */
    private int moveLeft(String str, int pos) {
    	int i = pos;
        while (i > 0 && str.charAt(i-1) == ' ') {
        	--i;
        }
        while (i > 0 && str.charAt(i-1) != ' ') {
        	--i;
        }
        return i;
    }
    
    /**
     * Moves to the right as if Ctrl was being pressed down.  For example:
     * this  is   |a  test
     * In the above string, if | is the location of pos, then:
     * this  is   a  |test
     * @param str The string to parse
     * @param pos Position of the starting index
     * @return index
     * 
     */
    private int moveRight(String str, int pos) {
    	int i = pos;
    	int len = str.length();
        while (i < len && str.charAt(i) != ' ') {
        	++i;
        }
    	while (i < len && str.charAt(i) == ' ') {
        	++i;
        }
    	return i;
    }
    
    /**
     * Processes a key event, possibly changing the text label
     * @param key
     * @param c
     */
    private void processKeyEvent(int key, char c) {
        if( GUIUtility.getInstance().isActiveElement(this) ) {
            Input input = Game.getInstance().getContainer().getInput();
            switch (key) {
            case Input.KEY_BACK:
            	if (position > 0 && label.length() > 0) {
	                /* 
	                 * If the user is holding down ctrl, then we want to delete all
	                 * characters to the left until we hit a space
	                 */
	                if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
	                	int i = moveLeft(label, position);
	                	label = label.substring(0, i) + label.substring(position);
	                    position = i;
	                } else { // act as normal
	                    label = label.substring(0, position-1) + label.substring(position);
	                    --position;
	                }
            	}
            	break;
            case Input.KEY_DELETE:
            	if (position < label.length()) {
	                // Opposite of backspace.  Delete characters in FRONT
	                
	                /* 
	                 * If the user is holding down ctrl, then we want to delete all
	                 * characters to the left until we hit a space
	                 */
	                if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
	                	int i = moveRight(label, position);
	                    label = label.substring(0, position) + label.substring(i);
	                } else { // act as normal
	                    label = label.substring(0, position) + label.substring(position+1);
	                }
            	}
            	break;
            case Input.KEY_LEFT:
            	if (position > 0) {
            		if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
            			position = moveLeft(label, position);
            		} else {
            			--position;
            		}
            	}
            	break;
            case Input.KEY_RIGHT:
            	if (position < label.length()) {
            		if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
            			position = moveRight(label, position);
            		} else {
            			++position;
            		}
            	}
            	break;
            case Input.KEY_RETURN:
                /* 
                 * TODO:  Add a boolean which says whether to process this as an event
                 *        or as a \n.
                 */
                callback(new Event(this, EventType.TEXT_ENTER));
            	break;
            default:
                if (c >= 0x20 && c <= 0x7F) {
	                // TODO:  Add a max length
	                label = label.substring(0, position) + c + label.substring(position);
	                ++position; // increase the caret position by 1.
                }
            	break;
            }
            needsRefresh = true;
        }
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void renderImage(Image image) {
        ImageHandler handler = ImageHandler.getInstance();
        Graphics g;
        
        try {
            if( image != null ) {
                g = image.getGraphics();
            
                String str = label; // The string to draw on the screen
                
                // Password mask check
                if( (this.style & PASSWORD) > 0 ) {
                    str = str.replaceAll(".", "*");
                }
                
                // Draw background
                if( GUIUtility.getInstance().isActiveElement(this) ) {
                    g.drawImage(handler.getImage(background_selected), 0, 0);
                } else if( highlighted ) {
                    g.drawImage(handler.getImage(background_highlighted), 0, 0);
                } else {
                    g.drawImage(handler.getImage(background), 0, 0);
                }
                
                // Draw text
                g.drawString(str, textOffsetX, textOffsetY);
                int xOffset = g.getFont().getWidth(label.substring(0, position));
                if (GUIUtility.getInstance().isActiveElement(this)) {
                	// "Drawing twice for a "bold" effect
	                g.drawString("|", textOffsetX+xOffset-4, textOffsetY);
	                g.drawString("|", textOffsetX+xOffset-3, textOffsetY);
                }
                g.flush();
            }
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    /**
     * Returns the current label (user input)
     * @return
     */
    public String getLabel() {
        return label;
    }
}
