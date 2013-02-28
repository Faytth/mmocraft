package org.unallied.mmocraft.net;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.chat.ChatMessage;

/**
 * Creates Packets to send to the client
 * @author Faythless
 *
 */
public class PacketCreator {

    private PacketCreator() {}
        
    /**
     * Ping the server (used to get latency).
     */
    public static Packet getPing() {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter(2);

        writer.write(SendOpcode.PING);
        writer.writeLong(System.currentTimeMillis());
        
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
    public static Packet getPlayerMovement(Player player) {
        // Guard
        if (player == null) {
            return null;
        }
        
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.PLAYER_MOVEMENT);
        writer.write(player.getLocation().getBytes());
        writer.writeShort(player.getState().getId());
        writer.write((byte)player.getDirection().ordinal()); // right is 0, left is 1
        writer.write(player.getVelocity().getBytes());
        writer.writeFloat(player.getFallSpeed());
        writer.writeFloat(player.getInitialVelocity());

        return writer.getPacket();
    }
    
    /**
     * Returns a direction packet to the server, which contains the player's direction.
     * @param player
     * @return packet
     */
    public static Packet getPlayerDirection(Player player) {
        // Guard
        if (player == null) {
            return null;
        }
        
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.PLAYER_DIRECTION);
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

	/**
	 * 
	 * @param startingIndex The starting index of the collision arc
	 * @param endingIndex The ending index of the collision arc
	 * @param horizontalOffset The horizontal offset to use in placement.
	 * @param verticalOffset The vertical offset to use in placement.
	 * @return
	 */
    public static Packet getBlockCollisionPacket(int startingIndex, int endingIndex, float horizontalOffset,
            float verticalOffset) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.BLOCK_COLLISION);
        writer.writeInt(startingIndex);
        writer.writeInt(endingIndex);
        writer.writeFloat(horizontalOffset);
        writer.writeFloat(verticalOffset);
        
        return writer.getPacket();
    }

    /**
     * Returns a player info packet, which asks the server to give the player name
     * and other trivial information for a player with the given id.
     * @param playerId The player id that information is being requested from.
     * @return packet
     */
    public static Packet getPlayerInfo(int playerId) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.PLAYER_INFO);
        writer.writeInt(playerId);
        
        return writer.getPacket();
    }

    /**
     * Sends an enable or disable request for PvP to the server.  When enabled,
     * the player is able to be attacked by other players in PvP.
     * @param pvpEnabled True if PvP should be enabled, else false.
     * @return packet
     */
    public static Packet getPvPToggle(boolean pvpEnabled) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.PVP_TOGGLE);
        writer.write((byte)(pvpEnabled ? 1 : 0));
        
        return writer.getPacket();
    }

    /**
     * Sends a request for item data to the server.
     * @param itemId The id of the item being requested.
     * @return packet
     */
    public static Packet getItemData(int itemId) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.ITEM_DATA);
        writer.writeInt(itemId);
        
        return writer.getPacket();
    }

    /**
     * Request monster information from the server.
     * @param monsterId The id of the monster being requested
     * @return packet
     */
    public static Packet getMonsterInfo(int monsterId) {
        PacketLittleEndianWriter writer = new PacketLittleEndianWriter();
        
        writer.write(SendOpcode.MONSTER_INFO);
        writer.writeInt(monsterId);
        
        return writer.getPacket();
    }
}
