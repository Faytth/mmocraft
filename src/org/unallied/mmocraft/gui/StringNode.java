package org.unallied.mmocraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;

/**
 * A node that is used to render strings.
 * @author Alexandria
 *
 */
public class StringNode implements Node {
   
    /** The color of the string to display. */
    private Color color = new Color(255, 255, 255, 255);
    
    /** The font to display the string in. */
    private Font font = FontHandler.getInstance().getFont(FontID.STATIC_TEXT_MEDIUM.toString());
    
    /** The maximum width before the string wraps on itself. */
    private int maxWidth;
    
    /** The actual lines that will be rendered. */
    private List<String> lines = new ArrayList<String>();
    
    /**
     * Creates a string node from the given parameters.
     * @param str The string to display.
     * @param color The color to render the string in.
     * @param font The font to render the string in.
     * @param maxWidth The maximum width before this string has to wrap.
     */
    public StringNode(String str, Color color, Font font, int maxWidth) {
        if (color != null) {
            this.color = color;
        }
        if (font != null) {
        	this.font = font;
        }
        this.maxWidth = maxWidth;
        splitString(str);
        
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
        return lines.size() > 1  ? maxWidth : 
               lines.size() == 1 ? font.getWidth(lines.get(0)) :
                                   0;
    }

    @Override
    public int getHeight() {
        return lines.size() * font.getLineHeight();
    }

    @Override
    public void render(Graphics g, int offX, int offY, int maxHeight) {
        int curHeight = 0;
        final int lineHeight = font.getLineHeight();
        for (int i=0; (i+1) * lineHeight <= maxHeight && i < lines.size(); ++i) {
            font.drawString(offX, offY + curHeight, lines.get(i), color);
            curHeight += lineHeight;
        }
    }

}
