package org.unallied.mmocraft.net;

import java.util.HashMap;
import java.util.Map;

import org.unallied.mmocraft.net.handlers.*;

/**
 * This class is used to perform callbacks to specific packet handlers
 * based on the received opcode.
 * Game packets are of the format:
 * [Length] [Header] [Payload]
 * Length is 4 bytes, header is 2 bytes
 * @author Faythless
 *
 */
public class PacketProcessor {
    
    private Map<Short, PacketHandler> handlers = new HashMap<Short, PacketHandler>();
    
    private PacketProcessor() {
        init();
    }
    
    private static class PacketProcessorHolder {
        public static final PacketProcessor instance = new PacketProcessor();
    }
    
    private void init() {
        reset();
    }
    
    public static PacketProcessor getInstance() {
        return PacketProcessorHolder.instance;
    }

    /**
     * Returns a handler for a given opcode
     * @param packetOpcode The opcode to return the handler for
     * @return handler for opcode if found; else null
     */
    public PacketHandler getHandler(short packetOpcode) {
        return handlers.get(packetOpcode);
    }
    
    /**
     * Registers a handler with the processor.  Opcodes must be unique
     * @param recvOpcode Unique opcode that identifies a handler
     * @param handler A handler that receives events when this opcode is received
     */
    public void registerHandler(RecvOpcode recvOpcode, PacketHandler handler) {
        handlers.put((short)recvOpcode.getValue(), handler);
    }
    
    /**
     * Resets all handlers to the default
     */
    public void reset() {
        handlers.clear();
        
        registerHandler(RecvOpcode.CHALLENGE, new ChallengeHandler());
        registerHandler(RecvOpcode.VERIFY, new VerifyHandler());
        registerHandler(RecvOpcode.LOGIN_ERROR, new LoginErrorHandler());
        registerHandler(RecvOpcode.PLAYER, new PlayerHandler());
        registerHandler(RecvOpcode.CHUNK, new ChunkHandler());
        registerHandler(RecvOpcode.MOVEMENT, new MovementHandler());
        registerHandler(RecvOpcode.PLAYER_DISCONNECT, new PlayerDisconnectHandler());
        registerHandler(RecvOpcode.REGISTRATION_ACK, new RegistrationAckHandler());
        registerHandler(RecvOpcode.CHAT_MESSAGE, new ChatMessageHandler());
        registerHandler(RecvOpcode.ITEM_DATA, new ItemDataHandler());
        registerHandler(RecvOpcode.PLAYER_INFO, new PlayerInfoHandler());
    }
}
