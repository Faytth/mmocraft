package org.unallied.mmocraft.gui.control;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.ImagePool;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;

public class TextCtrl extends Control {

    // Style masks
    /// The text will appear normal.
    public static final int NORMAL   = 0;
    /// The text will be replaced by asterisks.
    public static final int PASSWORD = 1 << 0; // The text will be echoed as asterisks.
    
    private String fontKey = FontID.STATIC_TEXT_MEDIUM.toString();
    private String label;
    private int textOffsetX = 4;
    private int textOffsetY = 2;
    
    /**
     * The color to render the text in.
     */
    private Color textColor;
    
    /**
     * The caret position.  0 is just before the first character.
     */
    private int position = 0;
    private int style = 0;
    /** The maximum number of characters allowed by the text control.  -1 means infinite. */
    private int maxLength = -1;
    
    /**
     * 
     * @param container The GameContainer that's passed from a state's init()
     * @param label The TextCtrl label.  This is what the user can change.
     * @param x The x offset for this control.
     * @param y The y offset for this control.
     * @param width The width of this control.  If -1, then it's calculated from background
     * @param height The height of this control.  If -1, then it's calculated from background
     * @param style The style masks for this text control
     */
    public TextCtrl(GUIElement parent, EventIntf intf, GameContainer container, 
            String label, float x, float y, int width, int height, int style) {
        this(parent, intf, container, label, x, y, width, height, null, null, null, style);
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
        this.textColor = new Color(255, 255, 255);
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
                	if (maxLength == -1 || label.length() < maxLength) {
		                label = label.substring(0, position) + c + label.substring(position);
		                ++position; // increase the caret position by 1.
                	}
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
    public void renderImage(Graphics g) {
        ImageHandler handler = ImageHandler.getInstance();
        
        try {
            if( g != null ) {
                String str = label; // The string to draw on the screen
                
                // Password mask check
                if( (this.style & PASSWORD) > 0 ) {
                    str = str.replaceAll(".", "*");
                }
                
                // Draw background
                Image textCtrlImage = null;
                if( GUIUtility.getInstance().isActiveElement(this) ) {
                    if (background_selected != null) {
                        g.drawImage(handler.getImage(background_selected), 0, 0);
                    } else {
                        textCtrlImage = handler.getImage(ImageID.TEXTCTRL_DEFAULT_SELECTED.toString());
                    }
                } else if( highlighted ) {
                    if (background_highlighted != null) {
                        g.drawImage(handler.getImage(background_highlighted), 0, 0);
                    } else {
                        textCtrlImage = handler.getImage(ImageID.TEXTCTRL_DEFAULT_HIGHLIGHTED.toString());
                    }
                } else {
                    if (background != null) {
                        g.drawImage(handler.getImage(background), 0, 0);
                    } else {
                        textCtrlImage = handler.getImage(ImageID.TEXTCTRL_DEFAULT_NORMAL.toString());
                    }
                }
                if (textCtrlImage != null) {
                    g.drawImage(textCtrlImage, 0, 0, 6, 21, 0, 0, 6, 21);
                    g.drawImage(textCtrlImage, 6, 0, width - textOffsetX, 21, 0, 21, 6, 42);
                    g.drawImage(textCtrlImage, width - textOffsetX, 0, width, 21, 0, 42, 6, 63);
                }
                
                // Draw text
                Font font = FontHandler.getInstance().getFont(fontKey);
                if (font != null) {
	                int xOffset = font.getWidth(str.substring(0, position));
	                /*
	                 *  When drawing text, we need to only show the text that "fits" into the text ctrl.
	                 *  To do this, we use:  widthOfText - widthOfTextCtrl.  If this value is > 0, then
	                 *  we know that we need to render only the "displayFrame" part of the text ctrl.
	                 *  For now this will just be based on the end of the text.  Later on, we need to
	                 *  make a new variable that keeps track of what we're looking at.
	                 */
	                int xDelta = font.getWidth(str+"|") - width + textOffsetX;
	                xDelta = xDelta > 0 ? xDelta : 0;
/*	                FontHandler.getInstance().draw(fontKey, str, textOffsetX-xDelta, 
	                		textOffsetY, textColor, width, height, false);*/
	                Image offscreenImage = ImagePool.getInstance().getOffscreenBuffer();
	                try {
	                    Graphics offscreenGraphics = offscreenImage.getGraphics();
	                    offscreenGraphics.setFont(FontHandler.getInstance().getFont(fontKey));
	                    offscreenGraphics.drawString(str, textOffsetX - xDelta, textOffsetY);
	                    offscreenGraphics.flush();
	                    g.drawImage(offscreenImage, textOffsetX, textOffsetY, width - textOffsetX, height, 
	                            textOffsetX, textOffsetY, width - textOffsetX, height, textColor);
	                } catch (SlickException e) {
    	                g.drawString(str, textOffsetX - xDelta, textOffsetY);
	                }
                    if (GUIUtility.getInstance().isActiveElement(this)) {
                        // "Drawing twice for a "bold" effect
                        g.drawString("|", xOffset-xDelta, textOffsetY);
                        g.drawString("|", xOffset+1-xDelta, textOffsetY);
                    }
                }
                g.flush();
            }
        } catch (Exception e) {
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
			this.position = this.label.length();
		}
	}
	
	/**
	 * Returns the maximum length for this text control.  This is the number of
	 * characters that are allowed to be entered by the text control.
	 * @return maximumLength.  A value of -1 indicates infinite.
	 */
	public int getMaxLength() {
		return maxLength;
	}
	
	/**
	 * Sets the maximum length of the text control.  This is the number of
	 * characters that are allowed to be entered by the text control.
	 * @param maxLength the number of characters allowed to be entered by this
	 *                  text control.  A value of -1 indicates infinite.
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

    @Override
    public boolean isAcceptingFocus() {
        return true;
    }
    
    /**
     * Sets the key to use for displaying the text's font.  If the font doesn't
     * exist or is null, then the font isn't changed.
     * @param fontKey The font key.
     */
    public void setFontKey(String fontKey) {
        if (fontKey == null || FontHandler.getInstance().getFont(fontKey) == null) {
            return;
        }
        this.fontKey = fontKey;
    }
    
    /**
     * Retrieves the font key used for rendering the text's font.
     * @return The font key.
     */
    public String getFontKey() {
        return fontKey;
    }
}
