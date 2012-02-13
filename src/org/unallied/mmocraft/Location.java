package org.unallied.mmocraft;

/**
 * A simple class containing coordinates in the world.  Also contains
 * offsets between coordinates.  The way this works is say you have
 * some block at (x,y).  You then have a player who is almost at the
 * middle of that block, but a little to the right.  You would have a
 * slightly positive xOffset.  1 xOffset is the equivalent of 1 pixel.
 * @author Ryokko
 *
 */
public class Location {
    long x;       // The x coordinate of a specific block
    long y;       // The y coordinate of a specific block
    long xOffset; // offset from the block at (x,y)
    long yOffset; // offset from the block at (x,y)
}
