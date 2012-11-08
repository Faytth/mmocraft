package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.items.ItemData;
import org.unallied.mmocraft.items.ItemManager;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * Handler for item data.  Item data is information about an item.  It is not
 * an item that is being added to the inventory.
 * @author Alexandria
 *
 */
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
