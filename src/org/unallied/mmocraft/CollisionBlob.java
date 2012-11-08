package org.unallied.mmocraft;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.ResourceLoader;
import org.unallied.mmocraft.client.SpriteSheetNode;

/**
 * A class that can be used for determining collision between two blobs (amorphous shapes).
 * This will work for any shape.
 * @author Alexandria
 *
 */
public class CollisionBlob {
    
    public static class CollisionPoint {
        private Point point;
        /** The damage multiplier to do at this location.  Ranges from 0 to 1. */
        private float damageMultiplier;
        
        public CollisionPoint(Point point, float damageMultiplier) {
            this.point = point;
            this.damageMultiplier = damageMultiplier;
        }
        
        public float getDamageMultiplier() {
            return damageMultiplier;
        }
        
        public Point getPoint() {
            return point;
        }
    }
    
    /** A matrix of the bounding box.  This is used to calculate the damage. */
    private float[][] collisionMatrix = null;
    
    /** The width of the bounding box. */
    private int width  = 0;
    
    /** The height of the bounding box. */
    private int height = 0;
    
    /** The absolute x offset that this blob is from the source image's top-left corner. */
    private int x = 0;
    
    /** The absolute y offset that this blob is from the source image's top-left corner. */
    private int y = 0;
    
    private CollisionBlob flippedBlob = null;
    
    /**
     * Creates a new collision blob from the image.  The image should be a heat map
     * in which the color red determines the amount of damage dealt.  The more red
     * a pixel is, the more damage it will do.
     * 
     * If this is a heat map of an attack, more red means higher attack damage.
     * 
     * If this is a heat map of a body (player or NPC), then more red means the
     * body will receive higher damage from the attack.
     * @param image
     */
    public CollisionBlob(Image image) {
        
        int lowestX = image.getWidth();
        int lowestY = image.getHeight();
        int highestX = 0;
        int highestY = 0;
        List<CollisionPoint> collisionPoints = new ArrayList<CollisionPoint>();
        
        // Go through all of the pixels adding any non-blank ones to the blob.
        for (int x=0; x < image.getWidth(); ++x) {
            for (int y=0; y < image.getHeight(); ++y) {
                Color pixel = image.getColor(x, y);
                
                /*
                 *  If this pixel is a damage pixel.  Note that we ignore alpha
                 *  in the calculations, but we still want to prevent "invisible"
                 *  pixels.
                 */
                if ((pixel.r != 0 || pixel.g != 0 || pixel.b != 0) && pixel.a > 0.5f) {
                    collisionPoints.add(
                            new CollisionPoint(new Point(x, y), pixel.r));
                    lowestX  = lowestX  > x ? x : lowestX;
                    lowestY  = lowestY  > y ? y : lowestY;
                    highestX = highestX < x ? x : highestX;
                    highestY = highestY < y ? y : highestY;
                }
            }
        }
        
        // Sanity check
        if (highestX - lowestX >= 0 && highestY - lowestY >= 0) {
            width = highestX - lowestX + 1;
            height = highestY - lowestY + 1;
        }
        
        // Update our absolute offset from the image top-left corner.
        this.x = lowestX;
        this.y = lowestY;
        
        collisionMatrix = new float[width][height];
        
        // Now go through again and, using our new bounding box, correct offsets
        try {
            for (CollisionPoint cp : collisionPoints) {
                cp.point.x -= lowestX;
                cp.point.y -= lowestY;
                collisionMatrix[cp.point.x][cp.point.y] = cp.damageMultiplier;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        flippedBlob = new CollisionBlob(this, image.getWidth(), image.getHeight());
    }
    
    /**
     * Creates a flipped version of the blob.  This is not a copy constructor.
     * This blob is flipped along the y-axis only.  It is not flipped on the x-axis.
     * @param flippedBlob The flipped version of this blob to create.
     */
    protected CollisionBlob(CollisionBlob flippedBlob, int width, int height) {
        this.flippedBlob = flippedBlob;
        this.width = flippedBlob.width;
        this.height = flippedBlob.height;
        this.x = width - flippedBlob.x - flippedBlob.width;
        this.y = flippedBlob.y;
        collisionMatrix = new float[this.width][this.height];
        for (int i=0; i < this.width; ++i) {
            for (int j=0; j < this.height; ++j) {
                collisionMatrix[this.width-i-1][j] = flippedBlob.collisionMatrix[i][j];
            }
        }
    }
    
    /**
     * 
     * @param image
     * @param width
     * @param height
     * @param index The 0-based index 
     */
    public CollisionBlob(BufferedImage image, int width, int height, int index) {
        int lowestX = width;
        int lowestY = height;
        int highestX = 0;
        int highestY = 0;
        List<CollisionPoint> collisionPoints = new ArrayList<CollisionPoint>();
        
        // Go through all of the pixels adding any non-blank ones to the blob.
        for (int x=0; x < width; ++x) {
            for (int y=0; y < height; ++y) {
                Color pixel = new Color(image.getRGB(x + index * width, y));
                
                /*
                 *  If this pixel is a damage pixel.  Note that we ignore alpha
                 *  in the calculations, but we still want to prevent "invisible"
                 *  pixels.
                 */
                if ((pixel.r != 0 || pixel.g != 0 || pixel.b != 0) && pixel.a > 0.5f) {
                    collisionPoints.add(
                            new CollisionPoint(new Point(x, y), pixel.r));
                    lowestX  = lowestX  > x ? x : lowestX;
                    lowestY  = lowestY  > y ? y : lowestY;
                    highestX = highestX < x ? x : highestX;
                    highestY = highestY < y ? y : highestY;
                }
            }
        }
        
        // Sanity check
        if (highestX - lowestX >= 0 && highestY - lowestY >= 0) {
            this.width = highestX - lowestX + 1;
            this.height = highestY - lowestY + 1;
        }
        
        // Update our absolute offset from the image top-left corner.
        this.x = lowestX;
        this.y = lowestY;
        
        collisionMatrix = new float[this.width][this.height];
        
        // Now go through again and, using our new bounding box, correct offsets
        try {
            for (CollisionPoint cp : collisionPoints) {
                cp.point.x -= lowestX;
                cp.point.y -= lowestY;
                collisionMatrix[cp.point.x][cp.point.y] = cp.damageMultiplier;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        flippedBlob = new CollisionBlob(this, width, height);
    }

    public Rectangle getIntersection(Rectangle other) {
        Rectangle intersection = new Rectangle(0, 0, width, height).intersection(new Rectangle(other.x, other.y, other.width, other.height));
        
        return intersection;
    }
    
    /**
     * Retrieves the damage between two collision blobs.  Damage is calculated by taking
     * the average of all collisions.
     * 
     * @param other The other blob to test against
     * @param offsetX The x offset that the second blob is from the first
     * @param offsetY The y offset that the second blob is from the first
     * @return pixelDamage.  Pixel damage can be between roughly 0 and 6.5.
     *                       Damage is normalized around 1.  This should be
     *                       used as a damage multiplier.
     */
    public float getDamage(CollisionBlob other, int offsetX, int offsetY) {
        float averageDamage = 0;
        int damageCount = 0;
        
        // Check the bounding box
        Rectangle collision = getIntersection(new Rectangle(offsetX + other.x, offsetY + other.y, other.width, other.height));
        
        for (int i=collision.x; i < collision.x + collision.width; ++i) {
            for (int j=collision.y; j < collision.y + collision.height; ++j) {
                float pixelDamage = other.collisionMatrix[i + offsetX][j + offsetY];
                pixelDamage *= collisionMatrix[i][j];
                if (pixelDamage > 0) {
                    averageDamage += pixelDamage;
                    ++damageCount;
                }
            }
        }
        
        // The division by 10000f is for damage normalization.
        return damageCount == 0 ? 0 : averageDamage / damageCount / 10000f;
    }
    
    /**
     * Retrieves the damage between a collision blob and a rectangle.  Use this
     * for getting the damage dealt to blocks and other objects that don't
     * have a collision blob.
     * 
     * Damage is calculated by taking the average of all collisions.
     * 
     * @param other The other rectangle to test against
     * @param offsetX The x offset that the second blob is from the first
     * @param offsetY The y offset that the second blob is from the first
     * @return pixelDamage.  Pixel damage can be between roughly 0 and 2.5.
     *                       Damage is normalized around 1.  This should be
     *                       used as a damage multiplier.
     */
    public float getDamage(Rectangle other, int offsetX, int offsetY) {
        float averageDamage = 0;
        int damageCount = 0;

        // Check the bounding box
        Rectangle collision = getIntersection(new Rectangle(offsetX + other.x, offsetY + other.y, other.width, other.height));
        
        for (int i=collision.x; i < collision.x + collision.width; ++i) {
            for (int j=collision.y; j < collision.y + collision.height; ++j) {
                float pixelDamage = collisionMatrix[i][j];
                if (pixelDamage > 0) {
                    averageDamage += pixelDamage;
                    ++damageCount;
                }
            }
        }

        // The division by 100f is for damage normalization.
        return damageCount == 0 ? 0 : averageDamage / damageCount / 100f;
    }
    
    /**
     * Retrieves the absolute x offset from the collision blob's source image.
     * @return xOffset
     */
    public int getXOffset() {
        return x;
    }
    
    /**
     * Retrieves the absolute y offset from the collision blob's source image.
     * @return yOffset
     */
    public int getYOffset() {
        return y;
    }
    
    /**
     * Retrieves the width of the collision blob's bounding box.
     * @return width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Retrieves the height of the collision blob's bounding box.
     * @return height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Retrieves the flipped version of this blob.
     * @return flippedBlob
     */
    public CollisionBlob getFlipped() {
        return flippedBlob;
    }
    
    /**
     * Generates an array of collisions from a sprite sheet.  This should be
     * used when you need all of the collision blobs for an animation (for
     * example, SwordHorizontalAttack).
     * 
     * Note:  The spritesheet must be horizontal ONLY.
     * @param ss The spritesheet to generate the collision blobs from
     * @return collisionArc.  If SpriteSheet is null, an empty array is returned.
     */
    public static CollisionBlob[] generateCollisionArc(SpriteSheet ss) {
        // Guard
        if (ss == null) {
            return new CollisionBlob[0];
        }
        
        CollisionBlob[] collisionArc = new CollisionBlob[ss.getHorizontalCount()];
        for (int x=0; x < ss.getHorizontalCount(); ++x) {
            collisionArc[x] = new CollisionBlob(ss.getSprite(x, 0));
        }
        
        return collisionArc;
    }

    /**
     * Generates an array of collisions from a sprite sheet node.  This should be
     * used when you need all of the collision blobs for an animation (for
     * example, SwordHorizontalAttack).
     * 
     * Note:  The spritesheet must be horizontal ONLY.
     * @param node The spritesheet node containing the width, height, and resource
     *             name needed to generate the collision arc.
     * @return collisionArc.  If SpriteSheet is null, an empty array is returned.
     */
    public static CollisionBlob[] generateCollisionArc(SpriteSheetNode node) {
        if (node == null) { // Guard
            return new CollisionBlob[0];
        }
        CollisionBlob[] collisionArc;
        try {
            BufferedImage image = null;
            image = ImageIO.read(ResourceLoader.getResourceAsStream(node.getResourceName()));
            int horizontalCount = image.getWidth() / node.getWidth();
            collisionArc = new CollisionBlob[horizontalCount];
            for (int x=0; x < horizontalCount; ++x) {
                collisionArc[x] = new CollisionBlob(image, node.getWidth(), node.getHeight(), x);
            }
        } catch (Throwable t) {
            return new CollisionBlob[0];
        }
        return collisionArc;
    }
}
