package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the client.
 * NOTE:  This is the OPPOSITE of the client's SendOpcode class.
 * @author Faythless
 *
 */
public enum RecvOpcode {
    /** Sent in response to a PING request. */
    PONG(0x00),
    WELCOME(0x01),
    CHALLENGE(0x02),
    VERIFY(0x03), 
    LOGIN_ERROR(0x04),
    PLAYER(0x05),
    CHUNK(0x06),
    PLAYER_MOVEMENT(0x07),
    PLAYER_DISCONNECT(0x08), 
    REGISTRATION_ACK(0x09), 
    CHAT_MESSAGE(0x0A), 
    ITEM_DATA(0x0B),
    PLAYER_INFO(0x0C), 
    /** Updates a player's experience for a certain skill. */
    SKILL_EXPERIENCE(0x0D),
    /** Informs a player that a block's type has changed. */
    BLOCK_CHANGED(0x0E),
    /** Returns the status of the player's PvP flag. */
    PVP_TOGGLE_RESPONSE(0x0F),
    /** Sent when a player was damaged by another player. */
    PVP_PLAYER_DAMAGED(0x10), 
    /** Sets the item quantity for an item in the player's inventory. */
    SET_ITEM(0x11),
    /** Informs the client of a nearby monster. */
    MONSTER_INFO(0x12),
    /** Updates the movement and position of a monster. */
    MONSTER_MOVEMENT(0x13), 
    /** Sent when a player damages a monster. */
    MONSTER_DAMAGED(0x14), 
    /** Informs the player of the gold that they have. */
    SET_GOLD(0x15), 
    /** Informs the client of a player's direction change. */
    PLAYER_DIRECTION(0x16),
    /** Informs the client of a monster's direction change. */
    MONSTER_DIRECTION(0x17)
    ;
    private int code = 0;
    
    private RecvOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
