package org.unallied.mmocraft;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {
    
    /** Minimum of requested item quantity range. */
    protected static final long MIN_ITEM_QUANTITY = 0;
    
    /** Maximum of requested item quantity range. */
    protected static final long MAX_ITEM_QUANTITY = Long.MAX_VALUE;

    private int expId = 1337;
    private String expName = "Sword";
    private String expDescription = "A sword.";
    private ItemQuality expQuality = ItemQuality.EPIC;
    private ItemType expType = ItemType.EQUIPMENT;
    private Integer expBuyPrice = 42;
    private Integer expSellPrice = 30;
    private long expQuantity = 0;
    
    protected Item getItem() {
        Item result = new Item(expId, expName, expDescription, expQuality, expType, expBuyPrice, expSellPrice);
        result.addQuantity(expQuantity);
        return result;
    }
    
    @Test
    public void testAddQuantity() {
        expQuantity = 1;
        Item item = getItem();
        assertEquals(expQuantity, item.getQuantity());
        expQuantity = MAX_ITEM_QUANTITY;
        item.addQuantity(expQuantity);
        assertEquals(expQuantity, item.getQuantity());
        item.addQuantity(-1);
        assertEquals(expQuantity, item.getQuantity());
    }
    
    @Test
    public void testRemoveQuantity() {
        expQuantity = 1;
        Item item = getItem();
        item.removeQuantity(-1);
        assertEquals(expQuantity, item.getQuantity());
        expQuantity = MIN_ITEM_QUANTITY;
        item.removeQuantity(MAX_ITEM_QUANTITY);
        assertEquals(expQuantity, item.getQuantity());
    }
}
