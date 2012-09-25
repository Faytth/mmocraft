package org.unallied.mmocraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.tools.hiero.truetype.IOUtils;
import org.newdawn.slick.util.ResourceLoader;
import org.unallied.mmocraft.constants.ServerConstants;
import org.unallied.mmocraft.tools.input.ByteArrayByteStream;
import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;

public class ItemManager {

    private Map<Integer, ItemData> items = new HashMap<Integer, ItemData>();
    
    private ItemManager() {
        init();
    }
    
    private static class ItemManagerHolder {
        private static final ItemManager instance = new ItemManager();
    }
    
    private void init() {
    }
    
    public static void add(ItemData itemData) {
        if (itemData != null) {
            synchronized(ItemManagerHolder.instance) {
                ItemManagerHolder.instance.items.put(itemData.getId(), itemData);
            }
        }
    }

    /**
     * Attempts to retrieve the item's data.  If the item can't be found, null
     * is returned instead.
     * @param id
     * @return The item's data or null if it can't be found.
     */
    public static ItemData getItemData(Integer id) {
        if (id == null) {
            return null;
        }
        
        return ItemManagerHolder.instance.items.get(id);
    }
    
    /**
     * Returns all item data that's in the item manager.
     * @return itemData
     */
    public static Collection<ItemData> getAllItemData() {
        return ItemManagerHolder.instance.items.values();
    }
    
    /**
     * Loads the item data from an Unallied Item Pack formatted resource.
     * @param filename The file that should be loaded.
     */
    public static void load(String filename) {
        InputStream is = null;
        try {
            is = ResourceLoader.getResourceAsStream(filename);
            byte[] bytes = IOUtils.toByteArray(is);
            ByteArrayByteStream babs = new ByteArrayByteStream(bytes);
            GenericSeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(babs);
            if (slea.readInt() != ServerConstants.MAGIC_ITEM_PACK_NUMBER) {
                System.err.println("Unable to load Unallied Item Pack file.  Bad magic number: " + filename);
                return;
            }
            ItemData itemData;
            while ((itemData = ItemData.fromBytes(slea)) != null) {
                ItemManager.add(itemData);
            }
            System.out.println("Successfully loaded Unallied Item Pack from: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
