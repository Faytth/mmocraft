package org.unallied.mmocraft.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.unallied.mmocraft.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.mina.MMOCodecFactory;

/**
 * Contains the socket to the server
 */
public class PacketSocket {
    private static final int MAX_ATTEMPTS = 4;
    private static final int RETRY_DELAY = 250;
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
        if( connector == null || !connector.isActive() ) {
            connect();
            return null;
        } else {
            MMOClient client = Game.getInstance().getClient();
            if (client != null) {
                return client.getSession();
            }
        }
        
        return null;
    }
}
