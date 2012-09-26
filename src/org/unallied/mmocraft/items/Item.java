package org.unallied.mmocraft.items;

import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * A class used for generalized items.  This class should not be used for
 * storing the information of a weapon or piece of armor.
 * @author Alexandria
 *
 */
public class Item {

    /** The item's id.  This should be unique for each item. */
    protected final int id;
	
    /** The quantity of this item. */
	private long quantity = 0;
    
	public Item(final int id) {
        this(id, 0);
    }

	public Item(final int id, final long quantity) {
		this.id = id;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}
	
    public long getQuantity() {
        return quantity;
    }
    
    /**
     * Adds a quantity of items to this item.  For example, if I currently have
     * 2 Apples and then I addItem(3), I will now have 5 Apples.
     * 
     * This method will "cap" at the maximum value of a stacked item.  
     * Adding a quantity that would normally exceed the maximum value
     * will set the quantity to the maximum value.
     * 
     * Negative values are ignored.
     * @param quantity The number of items to add
     */
    public void addQuantity(long quantity) {
        // Guard
        if (quantity < 0) {
            return;
        }
        
        if (this.quantity + quantity > 0) {
            this.quantity += quantity;
        } else { // prevent overflow
            this.quantity = ClientConstants.MAX_ITEM_STACK;
        }
    }
    
    /**
     * Removes a quantity of items to this item.  For example, if I currently have
     * 2 Apples and then I removeItem(3), I will now have 0 Apples.
     * 
     * This method will "cap" at 0 and will not go negative.  This method guarantees that
     * the quantity after the call to removeItem will be at least as low as the current 
     * quantity.  Removing a quantity that would normally go below 0 will set the quantity to 0.
     * 
     * Negative values are ignored.
     * @param quantity The number of items to add
     */
    public void removeQuantity(long quantity) {
        // Guard
        if (quantity < 0) {
            return;
        }
        
        if (this.quantity - quantity >= 0) {
            this.quantity -= quantity;
        } else { // prevent negatives
            this.quantity = 0;
        }
    }

    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * This class serializes as follows:
     * [id] [quantity]
     * 
     * @return byteArray
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(id);
        writer.writeLong(quantity);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an Item.
     * @return item
     */
    public static Item fromBytes(SeekableLittleEndianAccessor slea) {
    	
    	int id = slea.readInt();
    	long quantity = slea.readLong();
    	
    	return new Item(id, quantity);
    }
}
