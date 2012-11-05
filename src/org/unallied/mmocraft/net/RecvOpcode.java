package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the client.
 * NOTE:  This is the OPPOSITE of the client's SendOpcode class.
 * @author Faythless
 *
 */
public enum RecvOpcode {
    PING(0x00), 
    WELCOME(0x01),
    CHALLENGE(0x02),
    VERIFY(0x03), 
    LOGIN_ERROR(0x04),
    PLAYER(0x05),
    CHUNK(0x06),
    MOVEMENT(0x07),
    PLAYER_DISCONNECT(0x08), 
    REGISTRATION_ACK(0x09), 
    CHAT_MESSAGE(0x0A), 
    ITEM_DATA(0x0B),
    PLAYER_INFO(0x0C), 
    SKILL_EXPERIENCE(0x0D);
    private int code = 0;
    
    private RecvOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
