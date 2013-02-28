package org.unallied.mmocraft;

import java.util.List;

import org.unallied.mmocraft.items.Item;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public interface LootIntf {
    
    /**
     * Attempts to obtain loot.
     * @param lootMultiplier The multiplier to use for loot.  Examples are as follows:<br />
     * Multiplier of 1:  Standard loot drop chance
     * Multiplier of 2 when loot nodes add up to 50% chance:  (100% - 50%) / 2 = 25% chance to not receive loot.
     * @return loot A list of all the loot that should be obtained.
     */
    public List<Item> getLoot(double lootMultiplier);
    
    /**
     * Adds an item to the loot.
     * @param slea The slea currently pointing to a loot node object.
     */
    public void addItem(SeekableLittleEndianAccessor slea);
}
