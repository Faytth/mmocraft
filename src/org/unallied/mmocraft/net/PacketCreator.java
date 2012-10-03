package org.unallied.mmocraft.net;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.gui.ChatMessage;

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
     * Returns a CREDS packet to the server, which tells the server whether
     * the client knows the correct password.
     * Protocol:  [clientNonce] [Hash(clientNonce.serverNonce.password)]
     * @param clientNonce
     * @param clientHash
     * @return packet
     */
    public static Packet getCreds(int clientNonce, byte[] clientHash) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.CREDS);
        writer.writeInt(clientNonce);
        writer.write(clientHash);
        return writer.getPacket();
    }

    /**
     * Returns a chunk request from the server.
     * @return packet
     */
    public static Packet getChunk(long chunkId) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
                
        writer.write(SendOpcode.CHUNK);
        writer.writeLong(chunkId);
        return writer.getPacket();
    }

    /**
     * Returns a movement packet to the server, which contains player's x,y coords,
     * direction, current animation, and other movement related status.
     * @param player
     * @return packet
     */
    public static Packet getMovement(Player player) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.MOVEMENT);
        /* TODO:  Reduce the size of the location serialization.
         *        We only need to know the x, y, and offsets. 
         */
        writer.write(player.getLocation().getBytes());
        /* TODO:  Reduce size of the AnimationState serialization.
         *        We only need to know the ID.
         */
        writer.writeShort(player.getState().getId().getValue());
        writer.write((byte)player.getDirection().ordinal()); // right is 0, left is 1

        return writer.getPacket();
    }

    /**
     * Returns a register packet to the server, which contains player's
     * username, password, and email address.
     * The server responds to this packet and tells the client whether
     * the player was registered or it failed.
     * @param username
     * @param password
     * @param email
     * @return packet
     */
    public static Packet getRegister(String username, String password,
            String email) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.REGISTER);
        writer.writePrefixedAsciiString(username);
        writer.writePrefixedAsciiString(password);
        writer.writePrefixedAsciiString(email);
        
        return writer.getPacket();
    }

    /**
     * Returns a chat message packet, which contains the type and body of the
     * chat message.  The server knows who sent it, and as such, is responsible
     * for filling in the author on its own.
     * @param message the message to send
     * @return packet
     */
	public static Packet getChatMessage(ChatMessage message) {
		PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
		
		writer.write(SendOpcode.CHAT_MESSAGE);
		writer.write((byte) message.getType().ordinal());
		writer.writePrefixedAsciiString(message.getBody());
		
		return writer.getPacket();
	}

    public static Packet getBlockCollisionPacket(AnimationType id,
            int startingIndex, int endingIndex, float horizontalOffset,
            float verticalOffset) {
        // TODO Auto-generated method stub
        return null;
    }
}
