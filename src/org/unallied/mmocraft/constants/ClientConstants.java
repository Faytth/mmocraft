package org.unallied.mmocraft.constants;

import java.nio.charset.Charset;

public abstract class ClientConstants {
    public static final String HOST = "localhost";
    public static final int PACKET_TIMEOUT = 30; // 30 seconds timeout for packets
    public static final int SERVER_PORT = 27300; // the port to listen for connections on
    
    public static final int HASH_SIZE = 32; // Hash size of the password hash
    
    public static final Charset CHARSET = Charset.forName("US-ASCII"); // Default charset
    
    /**
     *  The maximum number of images to cycle through for the image pool.
     *  Each image is huge, so keep this number reasonably small.
     *  Note, however, that this number shouldn't be TOO small, or the CPU
     *  will need to do a lot more work.  If the number is smaller than the
     *  number of images drawn of the same size at any given time, then there 
     *  will be MASSIVE slow down.
     */
    public static final int IMAGE_POOL_SIZE = 5;
    
    /**
     * The rate of acceleration in pixels / second^2 of gravity.  The value
     * "feels" low, so we're increasing it to make up for it.
     */
    public static final float FALL_ACCELERATION = 241.1f * 4;
    
    /**
     * The terminal velocity in pixels / second of air.  1540 is the physically
     * correct value where 45 pixels = 1.8288 meters, but it "feels" low.
     */
    public static final float FALL_TERMINAL_VELOCITY = 1540 * 4;
    
    /**
     * The maximum delay between the time the shield key is released and a
     * movement key is pressed (in milliseconds) in which a roll should be 
     * performed.
     */
    public static final long SHIELD_ROLL_DELAY = 50;
    
    /**
     * The maximum stack size of an item.
     */
    public static final long MAX_ITEM_STACK = Long.MAX_VALUE;
    
    /** The delay in milliseconds between movement and when the player moves.  Used for smash effects. */
    public static final int MOVEMENT_DELAY = 40;
}
