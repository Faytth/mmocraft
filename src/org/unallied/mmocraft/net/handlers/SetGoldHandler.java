package org.unallied.mmocraft.net.handlers;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.items.Inventory;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class SetGoldHandler extends AbstractPacketHandler {
    
    /** A list of gold drops that have been looted. */
    private static final List<Long> goldDrops = new ArrayList<Long>();
    
    @Override
    /**
     * This is sent by the server when a change to the player's gold has been made.
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            synchronized (goldDrops) {
                long gold = slea.readLong();
                Inventory inventory = client.getPlayer().getInventory();
                long curGold = inventory.getGold();
                
                // Check to see if our gold increased.
                if (gold > curGold) {
                    goldDrops.add(gold - curGold);
                }
                inventory.setGold(gold);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Retrieves the next gold drop or null if no gold drops are found.  These gold drops are
     * gold drops that need to be shown in the loot frame.
     * @return gold drop if one exists, else null.
     */
    public static Long getNextGold() {
        Long result = null;
        
        synchronized (goldDrops) {
            if (!goldDrops.isEmpty()) {
                result = goldDrops.get(0);
                goldDrops.remove(0);
            }
        }
        
        return result;
    }
}
