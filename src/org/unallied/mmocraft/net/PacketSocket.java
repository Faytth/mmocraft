package org.unallied.mmocraft.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.mina.MMOCodecFactory;

/**
 * Contains the socket to the server
 */
public class PacketSocket {
    private IoConnector connector;
    
    
    private PacketSocket() {
        init();
    }
    
    private static class PacketSocketHolder {
        public static final PacketSocket instance = new PacketSocket();
    }
    
    private void init() {
        connect();
    }
    
    /**
     * Closes the socket
     */
    public void close() {
        connector.dispose();
    }
    
    /**
     * Attempts to connect to the server
     */
    public void connect() {
        final int RETRY_TIME = 100; //
        final int MAX_RETRY_ATTEMPTS = 50; // number of times to keep retrying connection
        try {
            connector = new NioSocketConnector();
            connector.getFilterChain().addLast("codec", 
                    (IoFilter) new ProtocolCodecFilter(new MMOCodecFactory()));
            connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 
                    ClientConstants.PACKET_TIMEOUT);
            connector.setHandler(new MMOClientHandler(PacketProcessor.getInstance()));
            connector.setDefaultRemoteAddress(
                    new InetSocketAddress(ClientConstants.HOST, 
                            ClientConstants.SERVER_PORT));
            connector.connect();
            
            // We need to wait for it to become active
            for (int i=0; i < MAX_RETRY_ATTEMPTS && !connector.isActive(); ++i) {
                Thread.sleep(RETRY_TIME);
            }
            
        } catch (Throwable t) {
            System.out.println("Badness");
            // We failed to connect, or something went wrong.
            if (connector != null) {
                connector.dispose();
            }
        }
    }
    
    public static PacketSocket getInstance() {
        return PacketSocketHolder.instance;
    }
    
    /**
     * Gets the current session.  There is only ever one session at a time.
     * @return
     */
    public IoSession getSession() {
        // If there's an error, try to fix it first
        if( connector == null ) {
            connect();
        } else if (!connector.isActive()) {
            connector.dispose();
            connect();
        } 
        
        if( connector != null && connector.isActive() ) {
            MMOClient client = Game.getInstance().getClient();
            if (client != null) {
                return client.getSession();
            }
        }
        
        return null;
    }
}
