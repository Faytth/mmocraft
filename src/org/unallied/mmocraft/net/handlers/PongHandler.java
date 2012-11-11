package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.Heartbeat;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PongHandler extends AbstractPacketHandler {

    @Override
    /**
     * Handles a ping-pong packet response from the server.  This packet tells
     * the client the time that the client initiated the request as well as the
     * time that the server answered the request.
     * @param slea
     * @param client
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        Heartbeat.getInstance().updateLatency(slea.readLong(), slea.readLong());
    }
}
