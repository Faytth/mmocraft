package org.unallied.mmocraft.net.handlers;

import org.newdawn.slick.Color;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.monsters.ClientMonster;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class MonsterDamagedHandler extends AbstractPacketHandler {
    
    @Override
    /**
     * This is sent by the server whenever a monster nearby the player is damaged.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int sourceId = slea.readInt();
            int monsterId = slea.readInt();
            int damageDealt = slea.readInt();
            int monsterHpRemaining = slea.readInt();
            ClientMonster monster = client.monsterPoolSession.getMonster(monsterId);
            if (monster != null) {
                Color damageColor = sourceId == client.getPlayer().getId() ? 
                        ClientConstants.DAMAGE_DEALT_COLOR : ClientConstants.DAMAGE_RECEIVED_OTHER_MONSTER_COLOR;
                client.damageSession.addDamage(monster.getLocation(), damageDealt, damageColor);
                monster.setHpCurrent(monsterHpRemaining);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
