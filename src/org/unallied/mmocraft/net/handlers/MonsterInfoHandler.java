package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.monsters.ClientMonster;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class MonsterInfoHandler extends AbstractPacketHandler {

    @Override
    /**
     * A message containing [monsterData]
     * This is sent by the server after a monster spawns.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            ClientMonster monster = ClientMonster.fromBytes(slea);
            client.monsterPoolSession.addMonster(monster);
        } catch (Throwable t) {
            
        }
    }
}
