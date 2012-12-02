package org.unallied.mmocraft.items;

import org.newdawn.slick.Color;
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
    
	/**
	 * Creates an item with a quantity of 0.
	 * @param id The id of the item to create.
	 */
	public Item(final int id) {
        this(id, 0);
    }

	/**
	 * Creates an item with the specified quantity.
	 * @param id The id of the item to create.
	 * @param quantity The quantity of items to start with.
	 */
	public Item(final int id, final long quantity) {
		this.id = id;
		this.quantity = quantity;
	}

	/**
	 * Retrieves the id of the item.
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retrieves the quantity of the item.
	 * @return quantity
	 */
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
        if (quantity <= 0) {
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
        if (quantity <= 0) {
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
    

    /**
     * Returns an abbreviated quantity name, such as 100k, 100M, 100G, 100T, 100P.
     * Prefixes are standard SI notation.  The reason for this is that when you get
     * into really large numbers (like quadrillions / quintillions), simply using the
     * first letter breaks down and results in confusion.
     * @param quantity The quantity to get the short name of
     * @return shortName the short name of the quantity
     */
    public static String getShortQuantityName(long quantity) {
        String result = "";
        
        if (quantity < 0) {
            result += "-";
            quantity *= -1;
        }
        
        if (quantity < 100000L) { // 100k
            result = Long.toString(quantity);
        } else if (quantity < 10000000L) { //10M 
            result = Long.toString(quantity / 1000L) + "k";
        } else if (quantity < 10000000000L) { //10G
            result = Long.toString(quantity / 1000000L) + "M";
        } else if (quantity < 10000000000000L) { //10T
            result = Long.toString(quantity / 1000000000L) + "G";
        } else if (quantity < 10000000000000000L) { //10P
            result = Long.toString(quantity / 1000000000000L) + "T";
        } else {
            result = Long.toString(quantity / 1000000000000000L) + "P";
        }
        
        return result;
    }
    
    /**
     * Returns a color based on the quantity.  Use this to color-code quantity results returned from <code>#getShortQuantityName(long)</code>.
     * @param quantity The quantity to get the color of
     * @return color The color of the quantity
     */
    public static Color getQuantityColor(long quantity) {
        Color result = null;
        
        if (quantity < 0) {
            quantity *= -1;
        }
        
        if (quantity < 100000L) { // 100k
            result = new Color(241, 255, 18);
        } else if (quantity < 10000000L) { //10M 
            result = new Color(255, 255, 255);
        } else if (quantity < 10000000000L) { //10G
            result = new Color(6, 203, 29);
        } else if (quantity < 10000000000000L) { //10T
            result = new Color(26, 147, 241);
        } else if (quantity < 10000000000000000L) { //10P
            result = new Color(168, 119, 210);
        } else {
            result = new Color(232, 59, 59);
        }
        
        return result;
    }

    /**
     * Sets the item quantity to the specified amount.  This should be used
     * with caution.  This does not have any guards other than ensuring that
     * the new quantity isn't below 0 (in which case this will set the quantity
     * to 0).  This function also ensures that the new quantity is not above
     * the max item stack.  If the quantity *is* above the max item stack,
     * the quantity is set to the max item stack.
     * @param quantity The new item quantity.
     */
    public void setQuantity(long quantity) {
        quantity = quantity < 0 ? 0 : quantity;
        quantity = quantity > ClientConstants.MAX_ITEM_STACK ? 
                ClientConstants.MAX_ITEM_STACK : quantity;
        this.quantity = quantity;
    }
}
