package org.unallied.mmocraft.client;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Stores all fonts in a map for easy access.  If the font requested is not
 * in the map, it will attempt to query the server to obtain the image.
 * This implements the Singleton pattern.
 * @author Faythless
 *
 */
public class FontHandler {
    private Map<String, UnicodeFont> fonts = new HashMap<String, UnicodeFont>();
    
    /**
     * Initializes the FontHandler with the default fonts.
     */
    private FontHandler() {
        init();
    }
    
    private static class FontHandlerHolder {
        public static final FontHandler instance = new FontHandler();
    }
    
    @SuppressWarnings("unchecked")
    private void init() {
        try {
            UnicodeFont uFont = new UnicodeFont("resources/fonts/oldsh.ttf", 13, false, false);
            uFont.addAsciiGlyphs();
            uFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            uFont.loadGlyphs();
            fonts.put( FontID.TOOLTIP_DEFAULT.toString(), uFont );
            
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Draws a message to the screen.  If the font is not in the map,
     * it will be queried from the server.
     * @param key The string identifier of the font to draw.
     * @param message The message to write to the screen
     * @param x The x position for drawing to the screen
     * @param y The y position for drawing to the screen
     * @param col The color of the string
     * @param width The maximum width of a line
     * @param height The maximum height of a line
     * @param wordWrap Whether to wrap the text or not
     */
    public void draw(String key, String message, float x, float y, Color col
            ,int width, int height, boolean wordWrap) {
        if( fonts.containsKey(key) && fonts.get(key) != null ) {
            fonts.get(key).drawString(x, y, message, col);
        } else {
            // FIXME:  Query from the server
        }
    }
    
    public static FontHandler getInstance() {
        return FontHandlerHolder.instance;
    }

    /**
     * Returns the maximum width needed to draw this <code>message</code>.
     * @param key The string identifier of the font to draw.
     * @param message The message to get the width of.
     * @return width
     */
    public int getMaxWidth(String key, String message) {
        if (fonts.containsKey(key) && fonts.get(key) != null) {
            String[] lines = message.split("\n");
            int maxWidth = 0;
            for (String line : lines) {
                int lineWidth = fonts.get(key).getWidth(line);
                if (lineWidth > maxWidth) {
                    maxWidth = lineWidth;
                }
            }
                
            return maxWidth;
        }
        return 0;
    }
    
    /**
     * Returns the maximum height needed to draw this <code>message</code>.
     * @param key The string identifier of the font to draw.
     * @param message The message to get the height of.
     * @return height
     */
    public int getMaxHeight(String key, String message) {
        if (fonts.containsKey(key) && fonts.get(key) != null) {
            return fonts.get(key).getHeight(message);
        }
        return 0;
    }
}
