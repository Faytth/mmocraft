package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.Heartbeat;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PlayerInfoHandler extends AbstractPacketHandler {
    @Override
    /**
     * A message containing [playerId][playerName]
     * This is sent by the server after the client requests player information about a player.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int playerId = slea.readInt();
            Player p = client.playerPoolSession.getPlayer(playerId);
            
            // We only need the name if this is not a new player.
            if (p != null) {
                p.setHpMax(slea.readInt());
                p.setHpCurrent(slea.readInt());
                p.setPvPTime(Heartbeat.getInstance().getLocalTimestamp(slea.readLong()));
                p.setName(slea.readPrefixedAsciiString());
            }
        } catch (Throwable t) {
        }
    }
}
