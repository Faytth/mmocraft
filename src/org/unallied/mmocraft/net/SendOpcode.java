package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the server.
 * When adding an opcode, it should ALWAYS come after the ones before it, and
 * the value should be ONE higher.  For example, 0x00, 0x01, 0x02, etc.
 * @author Faythless
 *
 */
public enum SendOpcode {
    PING(0x00), /** Sent in response to a server ping request. */
    LOGON(0x01), /** Sent to initialize the login process. */
    CREDS(0x02), /** Sent when the player is logging in after being challenged by the server. */
    CHUNK(0x03), /** Sent when the player needs to load a chunk. */
    MOVEMENT(0x04), /** Sent when the player changes velocity or animation state. */
    REGISTER(0x05), /** Sent when the player is trying to register for an account. */
    CHAT_MESSAGE(0x06),/** Sent when the player is sending a chat message. */
    ITEM_DATA(0x07), /** Sent when the client is requesting information about an item. */
    BLOCK_COLLISION(0x08), /** Sent when the client's attack hits a block. */
    PLAYER_INFO(0x09), /** Sent when requesting player information about someone. */
    PVP_TOGGLE(0x0A); /** Sent when enabling or disabling PvP. */
    private int code = 0;
    
    private SendOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
