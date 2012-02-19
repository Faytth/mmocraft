package org.unallied.mmocraft.net;

/**
 * Creates Packets to send to the client
 * @author Faythless
 *
 */
public class PacketCreator {

    private PacketCreator() {}
    
    /**
     * Pong the server (used to prevent connection timeouts)
     */
    public static Packet getPong() {
        // Create a new packet writer with 2 bytes (opcode only)
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter(2);
        writer.write(SendOpcode.PONG);
        return writer.getPacket();
    }
    
    /**
     * Returns a logon packet, which initiates the logon process.
     * Protocol:  [LOGON][username]
     * @param username
     * @return logon packet
     */
    public static Packet getLogon(String username) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.LOGON);
        writer.writePrefixedAsciiString(username);
        return writer.getPacket();
    }
    
    /**
     * Create a credentials packet, which is sent at the response of a
     * challenge packet during the login process.
     * Protocol:  [CREDENTIALS][nonce(4)][]
     * @param password user's password
     * @return credentials packet
     */
/*    public static Packet getCredentials(String password) {
        
    }*/
}
