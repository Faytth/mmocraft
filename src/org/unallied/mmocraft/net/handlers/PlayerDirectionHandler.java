package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PlayerDirectionHandler extends AbstractPacketHandler {
    
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int playerId = slea.readInt();
            Player p = client.playerPoolSession.getPlayer(playerId);
            
            if (p != null) { // not a new player, so just update it
                p.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
