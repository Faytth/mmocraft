package org.unallied.mmocraft.client;

import org.newdawn.slick.Image;

/**
 * Container used by ImagePool to send back an Image and whether that image
 * needs to be refreshed or not.
 * @author Faythless
 *
 */
public class ImageContainer {
    public Image image;
    public boolean needsRefresh;
    
    ImageContainer(Image image, boolean needsRefresh) {
        this.image = image;
        this.needsRefresh = needsRefresh;
    }
}
