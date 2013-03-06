package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PlayerDamagedHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int victimId          = slea.readInt();
            int damageDealt       = slea.readInt();
            int victimHpRemaining = slea.readInt();
            Player victim = client.playerPoolSession.getPlayer(victimId);
            if (victimId == client.getPlayer().getId()) { // Player is the victim
                client.getPlayer().setHpCurrent(victimHpRemaining);
                client.damageSession.addDamage(client.getPlayer().getLocation(), damageDealt, ClientConstants.DAMAGE_RECEIVED_COLOR);
            } else if (victim != null) {
                victim.setHpCurrent(victimHpRemaining);
                client.damageSession.addDamage(victim.getLocation(), damageDealt, ClientConstants.DAMAGE_RECEIVED_OTHER_PLAYER_COLOR);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
