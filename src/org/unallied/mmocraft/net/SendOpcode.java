package org.unallied.mmocraft.net;

/**
 * Opcodes used when sending packets to the server
 * @author Faythless
 *
 */
public enum SendOpcode {
    PONG(0x00),
    LOGON(0x01),
    CREDS(0x02),
    CHUNK(0x03), 
    MOVEMENT(0x04), 
    REGISTER(0x05);
    private int code = 0;
    
    private SendOpcode(int code) {
        this.code = code;
    }
    
    public int getValue() {
        return code;
    }
}
