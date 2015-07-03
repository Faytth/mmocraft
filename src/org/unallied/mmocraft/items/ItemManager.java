package org.unallied.mmocraft.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.util.ResourceLoader;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.blocks.*;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.tools.IOUtils;
import org.unallied.mmocraft.tools.input.ByteArrayByteStream;
import org.unallied.mmocraft.tools.input.GenericSeekableLittleEndianAccessor;

/**
 * Contains all of the item data about all items.
 * @author Alexandria
 *
 */
public class ItemManager {

    private Map<Integer, ItemData> items = new ConcurrentHashMap<Integer, ItemData>(8, 0.9f,1);
    
    /** Provides a mapping of itemId to BlockType. */
    private Map<Integer, BlockType> itemsToBlocks = new HashMap<Integer, BlockType>();
    
    private ItemManager() {
        init();
    }
    
    private static class ItemManagerHolder {
        private static final ItemManager instance = new ItemManager();
    }
        
    private void init() {}
    
    public static void add(ItemData itemData) {
        if (itemData != null) {
            ItemManagerHolder.instance.items.put(itemData.getId(), itemData);
        }
    }

    /**
     * Attempts to retrieve the item's data.  If the item can't be found, null
     * is returned instead.
     * @param itemId
     * @return The item's data or null if it can't be found.
     */
    public static ItemData getItemData(Integer itemId) {
        if (itemId == null) {
            return null;
        }
        ItemData result = ItemManagerHolder.instance.items.get(itemId);
        if (result == null) {
            // Enter a placeholder value
            ItemManagerHolder.instance.items.put(itemId, new ItemData(itemId, "", "", ItemQuality.JUNK, 
                    ItemType.UNASSIGNED, 0, 0));
            // Query the real data from the server
            Game.getInstance().getClient().announce(PacketCreator.getItemData(itemId));
        } else if (result.getType() == ItemType.UNASSIGNED) {
            // Result was a placeholder value.
            result = null;
        }
        
        return result;
    }
    
    /**
     * Retrieves a block type that is linked to the item whose item ID is
     * provided.
     * 
     * @param itemId The item ID linked to the block type we want.
     * @return The block's type that this item ID is linked to or null if the
     * block type doesn't exist.
     */
    public static BlockType getBlockType(Integer itemId) {
        if (itemId == null) {
            return null;
        }
        
        return ItemManagerHolder.instance.itemsToBlocks.get(itemId);
    }
    
    /**
     * Returns all item data that's in the item manager.
     * @return itemData
     */
    public static Collection<ItemData> getAllItemData() {
        return ItemManagerHolder.instance.items.values();
    }
    
    /**
     * Loads the mappings for items to block types.
     */
    private static void loadItemsToBlocks() {
        try {
//            Class<?>[] classes = ReflectionsUtil.getClasses("org.unallied.mmocraft.blocks");
            List<Class<?>> classes = new ArrayList<Class<?>>();
            classes.add(AirBlock.class);
            classes.add(AirGreenBlock.class);
            classes.add(AirRedBlock.class);
            classes.add(Block.class);
            classes.add(ClayBlock.class);
            classes.add(DirtBlock.class);
            classes.add(GrassBlock.class);
            classes.add(GravelBlock.class);
            classes.add(IronBlock.class);
            classes.add(SandBlock.class);
            classes.add(SandstoneBlock.class);
            classes.add(StoneBlock.class);
            for (Class<?> c : classes) {
                if (c.getSuperclass() == Block.class) {
                    Block b = (Block)c.newInstance();
                    ItemData itemData;
                    // Special check on grass 
                    if (b.getType() != BlockType.GRASS && (itemData = b.getItem()) != null) {
                        ItemManagerHolder.instance.itemsToBlocks
                                .put(itemData.getId(), b.getType());
                    }
                }
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
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
            if (slea.readInt() != ClientConstants.MAGIC_ITEM_PACK_NUMBER) {
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
        
        loadItemsToBlocks();
    }
    
}
