package org.unallied.mmocraft;

import java.util.HashMap;
import java.util.Map;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/** Inventory for the player.  Contains all items and inventory categories. */
public class Inventory {
    
    public class ItemCategory {
        protected Map<Integer, Item> items = new HashMap<Integer, Item>();
    }
    
    private Map<ItemType, ItemCategory> categories = new HashMap<ItemType, ItemCategory>();

    public Inventory() {
        init();
    }
    
    private void init() {
        categories.clear();
        categories.put(ItemType.EQUIPMENT, new ItemCategory());
        categories.put(ItemType.CONSUMABLES, new ItemCategory());
        categories.put(ItemType.TRADE_GOODS, new ItemCategory());
        categories.put(ItemType.BLOCKS, new ItemCategory());
        categories.put(ItemType.MISCELLANEOUS, new ItemCategory());
    }
    
    public Map<ItemType, ItemCategory> getItems() {
        return categories;
    }
    
    /**
     * Adds an item to the inventory.
     * @param item
     * @param quantity The number of items to add.  Negative values do nothing.
     */
    public void addItem(Item item, long quantity) {
        if (categories.containsKey(item.getType())) {
            ItemCategory category = categories.get(item.getType());
            if (category != null) {
                if (category.items.containsKey(item.getId())) {
                    Item categoryItem = category.items.get(item.getId());
                    categoryItem.addQuantity(quantity);
                } else {
                    item.addQuantity(quantity);
                    category.items.put(item.getId(), item);
                }
            }
        }
    }
    
    /**
     * Removes an item from the inventory.
     * @param item
     * @param quantity The number of items to remove.  Negative values do nothing.
     */
    public void removeItem(Item item, long quantity) {
        if (categories.containsKey(item.getType())) {
            ItemCategory category = categories.get(item.getType());
            if (category != null) {
                if (category.items.containsKey(item.getId())) {
                    Item categoryItem = category.items.get(item.getId());
                    categoryItem.removeQuantity(quantity);
                    if (categoryItem.getQuantity() == 0) {
                        category.items.remove(categoryItem.getId());
                    }
                }
            }
        }
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * This class serializes as follows:
     * [itemType(1)] [itemCount(4)] [item] [item]...
     * [itemType(1)] [itemCount(4)]...
     * 
     * @return byteArray
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        // For all categories, write the category name followed by its contents
        for (final ItemType itemType : categories.keySet()) {
            writer.write(itemType.getValue());
            ItemCategory category = categories.get(itemType);
            writer.writeInt(category.items.size());
            for (final Item item : category.items.values()) {
                writer.write(item.getBytes());
            }
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
        // TODO: Implement this
        return null;//new Location(x, y, xOffset, yOffset);
    }
}
