package org.unallied.mmocraft.client;

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
        public long chunkId = -1; // ID of the chunk associated with this image
        
        public ImageNode() {
            try {
                image = new Image(WorldConstants.WORLD_CHUNK_WIDTH
                        * WorldConstants.WORLD_BLOCK_WIDTH,
                        WorldConstants.WORLD_CHUNK_HEIGHT
                        * WorldConstants.WORLD_BLOCK_HEIGHT);
                
                // Load ALL images right now
                //image.getGraphics().flush();
            } catch (SlickException e) {
                image = null; // How did this happen?!
            }
        }
    }
    
    // The pool of images
    private ImageNode[] images = new ImageNode[ClientConstants.IMAGE_POOL_SIZE];
    
    private ImagePool() {
        for (int i=0; i < images.length; ++i) {
            images[i] = new ImageNode();
        }
        flush();
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
     * @param chunkId A unique chunk ID.
     * @return the image associated with the chunk
     */
    public Image getImage(long chunkId) {
        // Look for the chunkId or an empty space
        for (int i=0; i < images.length; ++i) {
            if (images[i] != null && images[i].chunkId == chunkId) {
                // Update the last accessed time
                images[i].accessTime = System.currentTimeMillis();
                // Found it!
                refreshNeeded = false;
                return images[i].image;
            }
        }
        
        // Uh oh... we didn't find it.  Let's kill someone!
        int index = -1;
        long lastAccessed = 0x7FFFFFFFFFFFFFFFL;
        for (int i=0; i < images.length; ++i) {
            if (images[i] != null && images[i].accessTime < lastAccessed) {
                index = i;
                lastAccessed = images[i].accessTime;
            }
        }
        
        if (index != -1) {
            images[index].accessTime = System.currentTimeMillis();
            images[index].chunkId = chunkId;
            refreshNeeded = true;
            return images[index].image;
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
     */
    public void flush() {
        for (int i=0; i < images.length; ++i) {
            if (images[i] != null) {
                try {
                    images[i].image.getGraphics().flush();
                } catch (SlickException e) {
                }
            }
        }
    }
}
