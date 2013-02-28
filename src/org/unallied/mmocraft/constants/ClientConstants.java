package org.unallied.mmocraft.constants;

import java.nio.charset.Charset;

import org.newdawn.slick.Color;

public abstract class ClientConstants {
    public static String HOST = "www.unallied.com"; //"localhost"; //
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
    public static final long SHIELD_ROLL_DELAY = 5000;
    
    /**
     * The maximum stack size of an item.
     */
    public static final long MAX_ITEM_STACK = Long.MAX_VALUE;
    
    /** The number of milliseconds that can elapse in which a smash effect can occur. */
    public static final int SMASH_DELAY = 150;
    
    /** 
     * The time in milliseconds that must pass before a roll can be started again.
     * This value is only checked in the shield animation states, which means that
     * it's possible to cancel out of the cooldown by performing any action between
     * consecutive rolls and shield states.  (A good example would be roll, idle, 
     * shield, roll).
     */
    public static final long ROLL_COOLDOWN = 250;
    
    /** The color that other player's damage is rendered in. */
    public static final Color DAMAGE_BLOCK_OTHER_PLAYER_COLOR = new Color(160, 160, 160, 150);
    
    /** The color that damage you deal is rendered in. */
    public static final Color DAMAGE_DEALT_COLOR = new Color(238, 118, 11);
    
    /** The color that damage you take is rendered in. */
    public static final Color DAMAGE_RECEIVED_COLOR = new Color(214, 78, 78);
    
    /** The color that monster damage received from other players is rendered in. */
    public static final Color DAMAGE_RECEIVED_OTHER_MONSTER_COLOR = new Color(214, 106, 106, 150);
    
    /** The color that other people's damage received is rendered in. */
    public static final Color DAMAGE_RECEIVED_OTHER_PLAYER_COLOR = new Color(214, 106, 106, 150);
    
    /** The color of the player's name if their PvP flag is enabled. */
    public static final Color PLAYER_NAME_PVP = new Color(231, 24, 14);
    
    /** The color of the player's name if their PvP flag is disabled. */
    public static final Color PLAYER_NAME_NORMAL = new Color(238, 238, 238);
    
    /** The color of a monster's name. */
    public static final Color MONSTER_NAME_NORMAL = new Color(238, 238, 238);
    
    /** The time in milliseconds that the PvP flag persists after disabling PvP. */
    public static final long PVP_FLAG_DURATION = 300000L;
    
    /** 
     * The amount in pixels to increase the collision box for when checking if
     * the player should send an attack packet to the server.
     */
    public static final float PLAYER_COLLISION_CHECK_RADIUS = 0;
    
    /** A magic number that is prepended to all Unallied Monster Pack files. */
    public static final int MAGIC_MONSTER_PACK_NUMBER = 0x4B504D55;
    
    /** A magic number that is prepended to all Unallied Item Pack files. */
    public static final int MAGIC_ITEM_PACK_NUMBER = 0x4B504955;
    
    /** The location of the Unallied Item Pack file containing all of the item data. */
    public static final String ITEM_PACK_LOCATION = "resources/items.uip";

    /** The location of the Unallied Monster Pack file containing all of the monster data. */
    public static final String MONSTER_PACK_LOCATION = "resources/monsters.ump";
    
    public static final int MINIMUM_USERNAME_LENGTH = 6;
    public static final int MAXIMUM_USERNAME_LENGTH = 30;
    
    /** 
     * The distance that the client clears an object from its cache.
     */
    public static final int OBJECT_DESPAWN_DISTANCE = 2500;
}
