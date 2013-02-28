package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.monsters.ClientMonster;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class MonsterMovementHandler extends AbstractPacketHandler {

    @Override
    /**
     * A message containing a monster's movement and current animation state.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int monsterId = slea.readInt();
            ClientMonster monster = client.monsterPoolSession.getMonster(monsterId);
            
            // If this is a new monster, we have to add it to our pool
            if (monster == null) {
                // Request monster info from server
                client.announce(PacketCreator.getMonsterInfo(monsterId));
            } else { // New monster, so update it.
                monster.fromMovement(slea);
            }
        } catch (Throwable t) {
            
        }
    }
}
