package org.unallied.mmocraft;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Stores all images in a map for easy access.  If the image requested is not
 * in the map, it will attempt to query the server to obtain the image.
 * This implements the Singleton pattern.
 * @author Ryokko
 *
 */
public class ImageHandler {
    
    /* A map of all images currently loaded in the game.  These images should
     * never be freed from memory.
     */
    private Map<String, Image> images = new HashMap<String, Image>();
    
    /**
     * Initializes the ImageHandler with the default images.
     */
    private ImageHandler() {
        init();
    }
    
    /**
     * This is a singleton holder.  It is loaded on the first execution of
     * ImageHandler.getInstance() or the first access to the holder, not before
     * @author Ryokko
     *
     */
    private static class ImageHandlerHolder {
        public static final ImageHandler instance = new ImageHandler();
    }
    
    /**
     * Loads the default images into the image handler.  These are images
     * that are not queried from the server, and are always available.
     */
    private void init() {
        try {
            images.put( ImageID.LOGIN_SCREEN.toString(), new Image("resources/images/login_screen.png") );
            images.put( ImageID.TEXTCTRL_LOGIN_NORMAL.toString(), new Image("resources/images/textctrl_login_normal.png"));
            images.put( ImageID.TEXTCTRL_LOGIN_HIGHLIGHTED.toString(), new Image("resources/images/textctrl_login_highlighted.png"));
            images.put( ImageID.TEXTCTRL_LOGIN_SELECTED.toString(), new Image("resources/images/textctrl_login_selected.png"));
            images.put( ImageID.BUTTON_LOGIN_NORMAL.toString(), new Image("resources/images/button_login_normal.png"));
            images.put( ImageID.BUTTON_LOGIN_HIGHLIGHTED.toString(), new Image("resources/images/button_login_highlighted.png"));
            images.put( ImageID.BUTTON_LOGIN_SELECTED.toString(), new Image("resources/images/button_login_selected.png"));
            
        } catch (SlickException e) {
            // There was an error loading the default images
            //e.printStackTrace();
        } catch (RuntimeException e) {
            // Do nothing
        }
    }
    
    /**
     * Draws an image from the images map to the screen.  If the image is
     * not in the map, it will be queried from the server.
     * @param key The string identifier (or enum) of the image to draw.  If
     *            this image is not in the map, it will be queried from the
     *            server.
     * @param x The x coordinate of the screen to draw
     * @param y The y coordinate of the screen to draw
     */
    public void draw(String key, float x, float y) {
        if( images.containsKey(key) && images.get(key) != null) {
            images.get(key).draw(x, y);
        } else {
            // FIXME:  Query from the server
        }
    }
    
    /**
     * Returns the width of the image indexed by key
     * @param key The string that references the image
     * @return Returns the width of the image or -1 if error
     */
    public int getWidth(String key) {
        int result = -1;
        
        if( images.containsKey(key) ) {
            result = images.get(key).getWidth();
        }
        
        return result;
    }
    
    /**
     * Returns the height of the image indexed by key
     * @param key The string that references the image
     * @return Returns the height of the image or -1 if error
     */
    public int getHeight(String key) {
        int result = -1;
        
        if( images.containsKey(key) ) {
            result = images.get(key).getHeight();
        }
        
        return result;
    }
    
    /**
     * Returns the image handler singleton.
     * @return the singleton image handler
     */
    public static ImageHandler getInstance() {
        return ImageHandlerHolder.instance;
    }

    public Image getImage(String key) {
        if( images.containsKey(key) ) {
            return images.get(key);
        } else {
            return null;
        }
    }
}
