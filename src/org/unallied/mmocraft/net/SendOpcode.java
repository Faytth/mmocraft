package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the server.
 * When adding an opcode, it should ALWAYS come after the ones before it, and
 * the value should be ONE higher.  For example, 0x00, 0x01, 0x02, etc.
 * @author Faythless
 *
 */
public enum SendOpcode {
    /** Sent in response to a server ping request. */
    PING(0x00),
    /** Sent to initialize the login process. */
    LOGON(0x01),
    /** Sent when the player is logging in after being challenged by the server. */
    CREDS(0x02),
    /** Sent when the player needs to load a chunk. */
    CHUNK(0x03),
    /** Sent when the player changes velocity or animation state. */
    PLAYER_MOVEMENT(0x04),
    /** Sent when the player is trying to register for an account. */
    REGISTER(0x05),
    /** Sent when the player is sending a chat message. */
    CHAT_MESSAGE(0x06),
    /** Sent when the client is requesting information about an item. */
    ITEM_DATA(0x07),
    /** Sent when the client's attack hits a block. */
    BLOCK_COLLISION(0x08),
    /** Sent when requesting player information about someone. */
    PLAYER_INFO(0x09),
    /** Sent when enabling or disabling PvP. */
    PVP_TOGGLE(0x0A),
    /** Sent when the client is requesting information about an instance of a monster. */
    MONSTER_INFO(0x0B), 
    /** Sent when the player changes direction. */
    PLAYER_DIRECTION(0x0C), 
    /** Sent when the player is requesting to revive. */
    REVIVE(0x0D);
    private int code = 0;
    
    private SendOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
