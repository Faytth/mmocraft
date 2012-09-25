package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the server.
 * When adding an opcode, it should ALWAYS come after the ones before it, and
 * the value should be ONE higher.  For example, 0x00, 0x01, 0x02, etc.
 * @author Faythless
 *
 */
public enum SendOpcode {
    PONG(0x00),
    LOGON(0x01),
    CREDS(0x02),
    CHUNK(0x03), 
    MOVEMENT(0x04), 
    REGISTER(0x05), 
    CHAT_MESSAGE(0x06),
    ITEM_DATA(0x07);
    private int code = 0;
    
    private SendOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
