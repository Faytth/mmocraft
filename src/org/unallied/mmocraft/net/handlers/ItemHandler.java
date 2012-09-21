package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.ItemQuality;
import org.unallied.mmocraft.ItemType;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class ItemHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        int count = slea.readInt(); // the number of items in this packet
        
        // For the number of items, add them to the item manager
        for (int i=0; i < count; ++i) {
            int id = slea.readInt();
            String name = slea.readPrefixedAsciiString();
            String description = slea.readPrefixedAsciiString();
            ItemQuality itemQuality = ItemQuality.fromValue(slea.readByte());
            ItemType itemType = ItemType.fromValue(slea.readByte());
            Integer buyPrice  = slea.readInt();
            Integer sellPrice = slea.readInt();
/*            ItemData itemData = new ItemData(name, description, itemQuality, 
                    itemType, buyPrice, sellPrice);
            ItemManager.add(itemData);*/
        }
    }
}
