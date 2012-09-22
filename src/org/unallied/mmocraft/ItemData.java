package org.unallied.mmocraft;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

public class ItemData {
    
    /** The item's id.  This should be unique for each item. */
    protected final int id;
    
    /** The item name. */
    protected final String name;
    
    /** The item description.  Appears below items when hovering over them.  */
    protected String description = "No description.";
    
    /** 
     * The quality of the item.  Item quality determines  what color an item
     * name appears in.
     */
    protected final ItemQuality quality;
    
    protected final ItemType type;
    
    /** The price that you can buy the item from the vendor for. */
    protected final Integer buyPrice;
    
    /** The price that the item sells for to the vendor. */
    protected final Integer sellPrice;

    /**
     * Creates an item with the specified parameters.  This class is only for
     * general items and should not be used for weapons / armor.
     * @param id The item id.  This should be unique for each item and is obtained
     *           from the server's database.
     * @param name The item name, such as "Uber Greatsword."
     * @param description The item description, such as, "The best greatsword ever."
     * @param quality The item quality, such as junk, normal, epic.
     * @param type The type of the item, such as Equipment, Trade Goods, etc.
     * @param buyPrice The price that you can buy the item for from the vendor.
     * @param sellPrice The price that you can sell an item to the vendor for.
     */
    public ItemData(final int id, final String name, String description, final ItemQuality quality, 
            final ItemType type, final Integer buyPrice, final Integer sellPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quality = quality;
        this.type = type;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public ItemQuality getQuality() {
        return quality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
    
    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * This class serializes as follows:
     * [id(4)] [name] [description] [quality(1)] [type(1)] [buyPrice(4)] [sellPrice(4)]
     * 
     * @return byteArray
     */
    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(id);
        writer.writePrefixedAsciiString(name);
        writer.writePrefixedAsciiString(description);
        writer.write(quality.getValue());
        writer.write(type.getValue());
        writer.writeInt(buyPrice);
        writer.writeInt(sellPrice);
        
        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of an ItemData object.
     * @return itemData
     */
    public static ItemData fromBytes(SeekableLittleEndianAccessor slea) {
        
    	int id = slea.readInt();
    	String name = slea.readPrefixedAsciiString();
    	String description = slea.readPrefixedAsciiString();
    	ItemQuality quality = ItemQuality.fromValue(slea.readByte());
    	ItemType type = ItemType.fromValue(slea.readByte());
    	Integer buyPrice = slea.readInt();
    	Integer sellPrice = slea.readInt();
    	
    	return new ItemData(id, name, description, quality, type, buyPrice, sellPrice);
    }
}
