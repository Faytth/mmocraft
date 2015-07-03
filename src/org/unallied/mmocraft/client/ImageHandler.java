package org.unallied.mmocraft.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Stores all images in a map for easy access.  If the image requested is not
 * in the map, it will attempt to query the server to obtain the image.
 * This implements the Singleton pattern.
 * TODO:  Change the the Image Handler loads images.  It should use a resource
 * manager.
 * @author Alexandria
 *
 */
public class ImageHandler {
    
    /**
     * Provides lazy loading functionality for the image handler.
     * @author Alexandria
     *
     */
    private class ImageHandlerNode {
        protected String url;
        private Image image;
        
        public ImageHandlerNode(String url) {
            if (url == null) {
                throw new NullPointerException("ImageHandlerNode cannot have a null resource location.");
            }
            this.url = url;
            this.image = null;
        }
        
        public Image getImage() {
            if (image == null) {
                try {
                    image = new Image(url);
                } catch (SlickException e) {
                    // This probably shouldn't be happening!
                    e.printStackTrace();
                } catch (Throwable t) {
                    // Probably the server.  Just ignore the warning.
                    t.printStackTrace();
                }
            }
            
            return image;
        }
    }
    
    /* A map of all images currently loaded in the game.  These images should
     * never be freed from memory.
     */
    private Map<String, ImageHandlerNode> images = new ConcurrentHashMap<String, ImageHandlerNode>(8, 0.9f, 1);
    
    /**
     * Initializes the ImageHandler with the default images.
     */
    private ImageHandler() {
        init();
    }
    
    /**
     * This is a singleton holder.  It is loaded on the first execution of
     * ImageHandler.getInstance() or the first access to the holder, not before
     * @author Faythless
     *
     */
    private static class ImageHandlerHolder {
        private static final ImageHandler instance = new ImageHandler();
    }
    
    /**
     * Loads the default images into the image handler.  These are images
     * that are not queried from the server, and are always available.
     */
    private void init() {
        try {
            images.put( ImageID.LOGIN_SCREEN.toString(), new ImageHandlerNode("resources/images/backgrounds/sky.png") );
            images.put( ImageID.LOGIN_TITLE.toString(), new ImageHandlerNode("resources/images/title.png"));
            images.put( ImageID.TEXTCTRL_DEFAULT_NORMAL.toString(), new ImageHandlerNode("resources/images/textctrl_default_normal.png"));
            images.put( ImageID.TEXTCTRL_DEFAULT_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/textctrl_default_highlighted.png"));
            images.put( ImageID.TEXTCTRL_DEFAULT_SELECTED.toString(), new ImageHandlerNode("resources/images/textctrl_default_selected.png"));
            images.put( ImageID.BUTTON_BACK_NORMAL.toString(), new ImageHandlerNode("resources/images/button_back_normal.png"));
            images.put( ImageID.BUTTON_BACK_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/button_back_highlighted.png"));
            images.put( ImageID.BUTTON_BACK_SELECTED.toString(), new ImageHandlerNode("resources/images/button_back_selected.png"));
            images.put( ImageID.BUTTON_CLOSE_NORMAL.toString(), new ImageHandlerNode("resources/images/button_close_normal.png"));
            images.put( ImageID.BUTTON_CLOSE_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/button_close_highlighted.png"));
            images.put( ImageID.BUTTON_CLOSE_SELECTED.toString(), new ImageHandlerNode("resources/images/button_close_selected.png"));
            images.put( ImageID.BUTTON_DEFAULT_NORMAL.toString(), new ImageHandlerNode("resources/images/button_default_normal.png"));
            images.put( ImageID.BUTTON_DEFAULT_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/button_default_highlighted.png"));
            images.put( ImageID.BUTTON_DEFAULT_SELECTED.toString(), new ImageHandlerNode("resources/images/button_default_selected.png"));
            images.put( ImageID.BUTTON_LOGIN_NORMAL.toString(), new ImageHandlerNode("resources/images/button_login_normal.png"));
            images.put( ImageID.BUTTON_LOGIN_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/button_login_highlighted.png"));
            images.put( ImageID.BUTTON_LOGIN_SELECTED.toString(), new ImageHandlerNode("resources/images/button_login_selected.png"));
            images.put( ImageID.BUTTON_REGISTER_NORMAL.toString(), new ImageHandlerNode("resources/images/button_register_normal.png"));
            images.put( ImageID.BUTTON_REGISTER_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/button_register_highlighted.png"));
            images.put( ImageID.BUTTON_REGISTER_SELECTED.toString(), new ImageHandlerNode("resources/images/button_register_selected.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_NORMAL.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarcharacter_normal.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarcharacter_highlighted.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARCHARACTER_SELECTED.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarcharacter_selected.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_NORMAL.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarinventory_normal.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_HIGHLIGHTED.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarinventory_highlighted.png"));
            images.put( ImageID.TOGGLEBUTTON_TOOLBARINVENTORY_SELECTED.toString(), new ImageHandlerNode("resources/images/togglebutton_toolbarinventory_selected.png"));

            // Backgrounds
            images.put( ImageID.BACKGROUND_SKY.toString(), new ImageHandlerNode("resources/images/backgrounds/sky.png") );
            images.put( ImageID.BACKGROUND_SKY_DIRT_INTERSECT.toString(), new ImageHandlerNode("resources/images/backgrounds/sky_dirt_intersect.png") );
            images.put( ImageID.BACKGROUND_DIRT.toString(), new ImageHandlerNode("resources/images/backgrounds/dirt.png") );
            
            // Blocks
            images.put( ImageID.BLOCK_AIR.toString(),       new ImageHandlerNode("resources/images/blocks/air.png"));
            images.put( ImageID.BLOCK_DIRT.toString(),      new ImageHandlerNode("resources/images/blocks/dirt.png"));
            images.put( ImageID.BLOCK_GRASS.toString(),     new ImageHandlerNode("resources/images/blocks/grass.png"));
            images.put( ImageID.BLOCK_STONE.toString(),     new ImageHandlerNode("resources/images/blocks/stone.png"));
            images.put( ImageID.BLOCK_IRON.toString(),      new ImageHandlerNode("resources/images/blocks/iron.png"));
            images.put( ImageID.BLOCK_CLAY.toString(),      new ImageHandlerNode("resources/images/blocks/clay.png"));
            images.put( ImageID.BLOCK_GRAVEL.toString(),    new ImageHandlerNode("resources/images/blocks/gravel.png"));
            images.put( ImageID.BLOCK_SANDSTONE.toString(), new ImageHandlerNode("resources/images/blocks/sandstone.png"));
            images.put( ImageID.BLOCK_SAND.toString(),      new ImageHandlerNode("resources/images/blocks/sand.png"));
            
            // Skills
            images.put( ImageID.ICON_SKILL_CONSTITUTION.toString(), new ImageHandlerNode("resources/images/icons/skill/constitution.png"));
            images.put( ImageID.ICON_SKILL_COOKING.toString(), new ImageHandlerNode("resources/images/icons/skill/cooking.png"));
            images.put( ImageID.ICON_SKILL_DEFENSE.toString(), new ImageHandlerNode("resources/images/icons/skill/defense.png"));
            images.put( ImageID.ICON_SKILL_FISHING.toString(), new ImageHandlerNode("resources/images/icons/skill/fishing.png"));
            images.put( ImageID.ICON_SKILL_MINING.toString(), new ImageHandlerNode("resources/images/icons/skill/mining.png"));
            images.put( ImageID.ICON_SKILL_SMITHING.toString(), new ImageHandlerNode("resources/images/icons/skill/smithing.png"));
            images.put( ImageID.ICON_SKILL_STRENGTH.toString(), new ImageHandlerNode("resources/images/icons/skill/strength.png"));
        } catch (RuntimeException e) {
            /*
             *  Do nothing.  This is a server only thrown exception, because 
             *  the server doesn't have OpenGL.
             */
        } catch (Throwable t) {
            t.printStackTrace();
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
            images.get(key).getImage().draw(x, y);
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
            result = images.get(key).getImage().getWidth();
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
            result = images.get(key).getImage().getHeight();
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
            return images.get(key).getImage();
        } else {
            return null;
        }
    }
}
