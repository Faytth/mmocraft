package org.unallied.mmocraft;

import java.util.HashMap;
import java.util.Map;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/** Inventory for the player.  Contains all items and inventory categories. */
public class Inventory {
    private volatile Map<Integer, Item> items = new HashMap<Integer, Item>();

    public Inventory() {
        init();
    }
    
    private void init() {
        items.clear();
    }
    
    public Map<Integer, Item> getItems() {
        return items;
    }
    
    /**
     * Adds an item to the inventory.
     * @param item
     * @param quantity The number of items to add.  Negative values do nothing.
     */
    public void addItem(Item item, long quantity) {
        if (items.containsKey(item.getId())) {
            Item categoryItem = items.get(item.getId());
            categoryItem.addQuantity(quantity);
        } else {
            item.addQuantity(quantity);
            items.put(item.getId(), item);
        }
    }
    
    /**
     * Removes an item from the inventory.
     * @param item
     * @param quantity The number of items to remove.  Negative values do nothing.
     */
    public void removeItem(Item item, long quantity) {
        if (items.containsKey(item.getId())) {
            Item categoryItem = items.get(item.getId());
            categoryItem.removeQuantity(quantity);
            if (categoryItem.getQuantity() == 0) {
                items.remove(categoryItem.getId());
            }
        }
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * This class serializes as follows:
     * [itemCount(4)] [item] [item]...
     * 
     * @return byteArray
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(items.size());
        for (final Item item : items.values()) {
            writer.write(item.getBytes());
        }
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an Inventory.
     * @return inventory
     */
    public static Inventory fromBytes(SeekableLittleEndianAccessor slea) {
        Inventory result = new Inventory();
        
        int count = slea.readInt();
        
        for (int i=0; i < count; ++i) {
        	Item item = Item.fromBytes(slea);
        	result.addItem(item, item.getQuantity());
        }
        
        return result;
    }
}
