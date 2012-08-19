package org.unallied.mmocraft.net;

import java.io.IOException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;

import org.unallied.mmocraft.net.PacketHandler;
import org.unallied.mmocraft.net.PacketProcessor;
import org.unallied.mmocraft.tools.PrintError;
import org.unallied.mmocraft.tools.input.ByteArrayByteStream;
import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class MMOClientHandler extends IoHandlerAdapter {

    private PacketProcessor processor;
    
    public MMOClientHandler(PacketProcessor processor) {
        this.processor = processor;
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        
        // If server forcibly closed the connection
        if (cause instanceof IOException) {
            MMOClient client = (MMOClient) session.getAttribute(MMOClient.CLIENT_KEY);
            client.disconnect();
            session.close(true);
        } else {
            MMOClient client = (MMOClient) session.getAttribute(MMOClient.CLIENT_KEY);
            client.disconnect();
            session.close(true);
            PrintError.print(PrintError.EXCEPTION_CAUGHT, cause);
        }
    }

    @Override
    public void sessionOpened(IoSession session) {
        
        /*
         * There is only ever one client.  Set the new connection without 
         * a reset.  This is because the client is reset if a logout occurs.
         * Otherwise we just want to reconnect.
         */
        MMOClient client = Game.getInstance().getClient();
        client.setSession(session);
        session.setAttribute(MMOClient.CLIENT_KEY, client);
    }
    
    @Override
    public void sessionClosed(IoSession session) {
        synchronized (session) {
            MMOClient client = (MMOClient) session.getAttribute(MMOClient.CLIENT_KEY);
            if (client != null) {
                try {
                    client.disconnect();
                } finally {
                    session.removeAttribute(MMOClient.CLIENT_KEY);
                    client.empty();
                    
                    // Return to login menu
                    Game.getInstance().enterState(org.unallied.mmocraft.client.GameState.LOGIN);
                }
            }
        }
        try {
            super.sessionClosed(session);
        } catch (Exception e) {
            PrintError.print(PrintError.EXCEPTION_CAUGHT, e);
        }
    }
    
    @Override
    /**
     * Handle a message from the client
     */
    public void messageReceived(IoSession session, Object message) {
        byte[] content = (byte[]) message;
        SeekableLittleEndianAccessor slea = 
                new GenericSeekableLittleEndianAccessor(
                        new ByteArrayByteStream(content));
        short packetOpcode = slea.readShort();
        MMOClient client = (MMOClient) session.getAttribute(MMOClient.CLIENT_KEY);
        
        // Get the handler for this opcode. (e.g. LOGIN, REGISTER, LOGOUT, ATTACK, ...)
        PacketHandler packetHandler = processor.getHandler(packetOpcode);
        
        if (packetHandler != null && packetHandler.validState(client)) {
            try {
                packetHandler.handlePacket(slea, client);
            } catch (Throwable t) {
                // Uh oh!  We failed to handle the packet!
            }
        }
    }
    
    @Override
    /**
     * Occurs when the session becomes idle.  The server will initiate the ping-pong
     */
    public void sessionIdle(final IoSession session, final IdleStatus status) {
    }
}
