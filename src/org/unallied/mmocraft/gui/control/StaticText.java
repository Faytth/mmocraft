package org.unallied.mmocraft.gui.control;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.gui.GUIElement;

public class StaticText extends GUIElement {

    /** The maximum width before the string wraps on itself. */
    private int maxWidth;
    
    /** The maximum height before the text is truncated. */
    private int maxHeight;
    
    /** The actual lines that will be rendered. */
    private List<String> lines = new ArrayList<String>();
    
    /**
     * The font to use when rendering the static text.
     */
    private Font font = null;
    
    /**
     * The color to use when drawing this text.
     */
    private Color color = null;
    
    /**
     * Creates a static text control, which displays a text message.
     * @param parent The parent control to this.  (Usually it's "this")
     * @param intf The event interface to use for event callbacks.
     * @param label The message to display
     * @param x The x offset for this control (from the parent GUI element)
     * @param y The y offset for this control (from the parent GUI element)
     * @param width The width for this control
     * @param height The height for this control
     * @param fontID The font ID of the font to use
     */
    public StaticText(final GUIElement parent, EventIntf intf,GameContainer container
            , String label, float x, float y, int width, int height, FontID fontID) {
        this(parent, intf, container, label, x, y, width, height, fontID, new Color(255, 255, 255));
    }
    
    /**
     * Creates a static text control, which displays a text message.
     * @param parent The parent control to this.  (Usually it's "this")
     * @param intf The event interface to use for event callbacks.
     * @param label The message to display
     * @param x The x offset for this control (from the parent GUI element)
     * @param y The y offset for this control (from the parent GUI element)
     * @param width The width for this control
     * @param height The height for this control
     * @param fontID The font ID of the font to use
     * @param color The color to make the text
     */
    public StaticText(final GUIElement parent, EventIntf intf,GameContainer container,
            String label, float x, float y, int width, int height, FontID fontID, 
            Color color) {
        super(parent, intf, container, x, y, width, height);
        
        this.font = FontHandler.getInstance().getFont(fontID.toString());
        this.color = color;
        
        this.maxWidth = width;
        // This is a kludge
        this.maxWidth = this.maxWidth < 0 ? 1000 : this.maxWidth;
        
        this.maxHeight = height;
        // This is a kludge
        this.maxHeight = this.maxHeight < 0 ? 1000 : this.maxHeight;
        
        splitString(label);
        
        // This is a silly check to make sure the max width will still work even for super small max widths.
        if (maxWidth < this.font.getWidth("M")) {
            maxWidth = this.font.getWidth("M") + 1; // +1 for safe measure.
        }
    }
    
    /**
     * Splits the string if necessary to fit on multiple lines.
     * The split string will occupy the lines member variable.
     * @param str The string to split.
     */
    private void splitString(String str) {
        if (str == null) { // Guard
            return;
        }
        lines.clear();
        if (font.getWidth(str) > maxWidth || str.contains("\n")) {
            String[] stringLines = str.split("\n");
            for (String stringLine : stringLines) {
                String[] words = stringLine.split(" ");
                String line = "";
                
                for (String word : words) {
                    if (font.getWidth(line + word) < maxWidth) {
                        line += word + " ";
                    } else if (line.length() == 0) {
                        // The word was too long for the entire line!  Split it!
                        String partOfAWord = "";
                        for (int i=0; i < word.length(); ++i) {
                            if (font.getWidth(partOfAWord + str.charAt(i)) < maxWidth) {
                                partOfAWord += str.charAt(i);
                            } else {
                                lines.add(partOfAWord);
                                partOfAWord = "";
                            }
                        }
                    } else {
                        lines.add(line);
                        line = word + " ";
                    }
                }
                if (line.length() > 0) {
                    lines.add(line);
                }
            }
        } else {
            lines.add(str);
        }
    }
    
    @Override
    public int getWidth() {
        return font == null || lines.isEmpty() ? 0 : 
               lines.size() > 1 ? maxWidth : font.getWidth(lines.get(0));
    }
    
    @Override
    public int getHeight() {
        return font == null ? 0 : font.getLineHeight();
    }
    
    @Override
    public void update(GameContainer container) {
        // Static text does not respond to events (except tooltips)
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        int absX = getAbsoluteX();
        int absY = getAbsoluteY();
        
        Color oldColor = g.getColor();
        if (color != null) {
            g.setColor(color);
        }
        if (font != null) {
            g.setFont(font);
        }
        if (font != null) {
            int curHeight = 0;
            final int lineHeight = font.getLineHeight();
            for (int i=0; (i+1) * lineHeight <= maxHeight && i < lines.size(); ++i) {
                font.drawString(absX, absY + curHeight, lines.get(i), color);
                curHeight += lineHeight;
            }
            g.setColor(oldColor);
            g.flush();
        }
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void renderImage(Graphics image) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Returns the label's color
     * @return color the color of the label
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the color of the label
     * @param color the new color of the label
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets a new label
     * @param label the new label
     */
    public void setLabel(String label) {
        if (lines.size() > 1 || (lines.size() == 0 && !label.isEmpty()) || 
                (lines.get(0).compareTo(label) != 0)) {
            splitString(label);
        }
    }

    @Override
    public boolean isAcceptingFocus() {
        return false;
    }

    /**
     * Retrieves the current label that's being shown, with each line being
     * separated by a newline.
     * @return label
     */
    public String getLabel() {
        String result = "";
        for (int i=0; i < lines.size(); ++i) {
            result += lines.get(i);
            if (i + 1 < lines.size()) {
                result += "\n";
            }
        }
        return result;
    }
}
