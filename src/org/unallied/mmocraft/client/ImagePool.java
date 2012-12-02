package org.unallied.mmocraft.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.constants.WorldConstants;


/**
 * The image pool is used for very large images that would otherwise cause a
 * noticeable "pause" in the game if they were to be allocated at runtime.
 * Instead, we have a fixed number IMAGE_POOL_SIZE of images that we can
 * allocate.  When we run out of images, we recycle the least recently used
 * image.
 * 
 * @author Faythless
 *
 */
public class ImagePool {
    
    private boolean refreshNeeded = false;
    
    private class ImageNode {
        public Image image = null;
        public long accessTime = 0; // Time this was last accessed
        public Object object = null; // The object associated with this image
        
        /**
         * Creates a new image of the specified width and height.
         * @param w The width of the image
         * @param h The height of the image
         */
        public ImageNode(int w, int h) {
            try {
                image = new Image(w, h);
            } catch (SlickException e) {
                image = null; // How did this happen?!
            }
        }
    }
    
    // The pool of images
    private Map<Integer, ImageNode[]> images = new ConcurrentHashMap<Integer, ImageNode[]>(8, 0.9f, 1);
    
    /** An offscreen buffer of 1024 x 1024 pixels. */
    private Image offscreenBuffer;
    
    /**
     * Initializes all image nodes of a given width and height if they are
     * not already initialized.
     * @param width The width of the images to initialize
     * @param height The height of the images to initialize
     */
    private void initImageNodes(int width, int height) {
        int key = getImagesKey(width, height);
        if (!images.containsKey(key)) {
        	int chunkWidth = WorldConstants.WORLD_BLOCK_WIDTH * WorldConstants.WORLD_CHUNK_WIDTH;
        	int chunkHeight = WorldConstants.WORLD_BLOCK_HEIGHT * WorldConstants.WORLD_CHUNK_HEIGHT;
        	
        	// This is a kludge for chunks.  Chunks require extra special image size to prevent some MAJOR rendering problems.
        	// TODO: Remove this kludge
        	if (width == chunkWidth && height == chunkHeight && chunkWidth != 0 && chunkHeight != 0) {
        	    // Because the game is now resizable, we need these to be sufficiently large.
        	    int chunksAcross = 4;
        	    int chunksHigh   = 2;
//        		int chunksAcross = (Game.getInstance().getContainer().getWidth() / chunkWidth) + 1;
//        		int chunksHigh   = (Game.getInstance().getContainer().getHeight() / chunkHeight) + 1;
        		chunksAcross = chunksAcross * 2 + 1;
        		chunksHigh   = chunksHigh   * 2 + 1;
        		// The +1 below is to stop "lag" between two adjacent coordinates when walking back and forth
        		images.put(key, new ImageNode[(chunksAcross+1) * (chunksHigh+1)]);
        	} else {
        		images.put(key, new ImageNode[ClientConstants.IMAGE_POOL_SIZE]);
        	}

            ImageNode[] node = images.get(key);
            
            for (int i=0; i < node.length; ++i) {
                node[i] = new ImageNode(width, height);
            }
            flush(key);
        }
    }
    
    private ImagePool() {
        
        /*
         *  Special case.  We want to create these images beforehand so that loading
         *  new chunks in-game doesn't slow down.
         */
        int width = WorldConstants.WORLD_CHUNK_WIDTH * WorldConstants.WORLD_BLOCK_WIDTH;
        int height = WorldConstants.WORLD_CHUNK_HEIGHT * WorldConstants.WORLD_BLOCK_HEIGHT;
        initImageNodes(width, height);
        try {
            offscreenBuffer = new Image(1024, 1024);
        } catch (SlickException e) { // Unable to use offscreen buffer
            offscreenBuffer = null;
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the key for the images map given a width and height.
     * @param width The width of the image's key
     * @param height The height of the image's key
     * @return (height << 32) | width
     */
    private Integer getImagesKey(int width, int height) {
        return  (height << 32) | width;
    }

    private static class ImagePoolHandler {
        public static final ImagePool instance = new ImagePool();
    }
    
    public static ImagePool getInstance() {
        return ImagePoolHandler.instance;
    }
    
    /**
     * Given a chunkId that uniquely identifies a chunk, retrieve its
     * associated image.  If this chunk does not have an image associated
     * with it yet, it will be assigned an image.  This should only be
     * called for chunks that need to render their image.
     * @param object The object requesting the image.  Each object may only
     * request a single image at a time.
     * @param width The width of the image to retrieve
     * @param height The height of the image to retrieve
     * @return the image associated with the chunk
     */
    public Image getImage(Object object, int width, int height) {

        // Create the images if they're not already made
        if (!images.containsKey(getImagesKey(width, height))) {
            initImageNodes(width, height);
        }
        
        ImageNode[] nodes = images.get(getImagesKey(width, height));
        
        if (nodes != null) {
            // Look for the chunkId or an empty space
            for (int i=0; i < nodes.length; ++i) {
                if (nodes[i] != null && nodes[i].object == object) {
                    // Update the last accessed time
                    nodes[i].accessTime = System.currentTimeMillis();
                    // Found it!
                    refreshNeeded = false;
                    return nodes[i].image;
                }
            }
            
            // Uh oh... we didn't find it.  Let's kill someone!
            int index = -1;
            long lastAccessed = 0x7FFFFFFFFFFFFFFFL;
            for (int i=0; i < nodes.length; ++i) {
                if (nodes[i] != null && nodes[i].accessTime < lastAccessed) {
                    index = i;
                    lastAccessed = nodes[i].accessTime;
                }
            }
            
            if (index != -1) {
                nodes[index].accessTime = System.currentTimeMillis();
                nodes[index].object = object;
                refreshNeeded = true;

                return nodes[index].image;
            }
        }
        
        refreshNeeded = false;
        return null;
    }
    
    /**
     * Returns whether the last call to getImage() requires a refresh
     * of the image.  DOES NOT WORK WITH MULTITHREADING.
     * @return
     */
    public boolean needsRefresh() {
        return refreshNeeded;
    }
    
    /**
     * Flushes all images.  Use this on initialization
     * @param key The key to the images that we need to flush
     */
    public void flush(int key) {
        ImageNode[] nodes = images.get(key);
        
        if (nodes != null) {
            for (int i=0; i < nodes.length; ++i) {
                if (nodes[i] != null) {
                    try {
                        nodes[i].image.getGraphics().flush();
                    } catch (SlickException e) {
                    }
                }
            }
        }
    }
    
    public Image getOffscreenBuffer() {
        if (offscreenBuffer != null) {
            try {
                Graphics g = offscreenBuffer.getGraphics();
                g.clear();
                g.flush();
            } catch (SlickException e) {
            }
            
        }
        return offscreenBuffer;
    }
}
