package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.monsters.ClientMonster;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class MonsterDirectionHandler extends AbstractPacketHandler {
    
    @Override
    /**
     * A message containing a monster's direction
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int monsterId = slea.readInt();
            ClientMonster monster = client.monsterPoolSession.getMonster(monsterId);
            
            // If this is a new monster, we have to add it to our pool
            if (monster != null) {
                // Not a new monster, so update it.
                monster.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
            }
        } catch (Throwable t) {
            
        }
    }
}
