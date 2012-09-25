package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.ItemData;
import org.unallied.mmocraft.ItemManager;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class ItemDataHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        int count = slea.readInt(); // the number of items in this packet
        
        // For the number of items, add them to the item manager
        for (int i=0; i < count; ++i) {
            ItemManager.add(ItemData.fromBytes(slea));
        }
    }
}
