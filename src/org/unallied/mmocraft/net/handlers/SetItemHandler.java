package org.unallied.mmocraft.net.handlers;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle Set Item packets from the server which set the
 * quantity for a specified item ID.  A quantity of 0 indicates that the item
 * should be removed from the player's inventory.  These packets have the 
 * following format:<br />
 * [itemID] [quantity]
 * @author Alexandria
 *
 */
public class SetItemHandler extends AbstractPacketHandler {
    
    private static final List<Item> items = new ArrayList<Item>();
    
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            synchronized(items) {
                int itemId = slea.readInt();
                long quantity = slea.readLong();
                Inventory inventory = client.getPlayer().getInventory();
                long curQuantity = inventory.getQuantity(itemId);
                
                // Check to see if our quantity increased.
                if (quantity > curQuantity) {
                    items.add(new Item(itemId, quantity - curQuantity));
                }
                client.getPlayer().getInventory().setQuantity(itemId, quantity);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Retrieves the next item or null if no items are found.  These items are
     * items that need to be shown in the loot frame.  They consist of an item
     * id and the quantity that was added to the player's inventory.
     * @return item if one exists, else null.
     */
    public static Item getNextItem() {
        Item result = null;
        
        synchronized (items) {
            if (!items.isEmpty()) {
                result = items.get(0);
                items.remove(0);
            }
        }
        
        return result;
    }
    
}
