package org.unallied.mmocraft.items;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * @author Alexandria
 *
 */
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

    protected List<ItemRequirement> requirements = new ArrayList<ItemRequirement>();
    protected List<ItemStat> stats = new ArrayList<ItemStat>();
    protected List<ItemEffect> effects = new ArrayList<ItemEffect>();
    
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

    /**
     * Retrieves the price that 1 of this item can be sold to the store at.
     * @return sellPrice
     */
    public Integer getSellPrice() {
        return sellPrice;
    }

    /**
     * Retrieves the price that 1 of this item can be purchased from the store 
     * at.
     * @return buyPrice
     */
    public Integer getBuyPrice() {
        return buyPrice;
    }

    /**
     * Retrieves the quality of the item, such as Junk, Common, Uncommon,
     * Superor, Epic, or Legendary.
     * @return quality
     */
    public ItemQuality getQuality() {
        return quality;
    }

    /**
     * Retrieves the description of the item.  This is the description that
     * appears when the player mouses over an item.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.  This is the description that appears
     * when the player mouses over an item.
     * @param description The description of an item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the item name.  This is the name that the player sees, such as
     * "Dirt Block."
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the item, such as Block, Head, Chest, 
     * Miscellaneous.
     * @return type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Retrieves the id of the item.
     * @return id The id of the item.
     */
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
        writer.write7BitPrefixedAsciiString(name);
        writer.write7BitPrefixedAsciiString(description);
        writer.write(quality.getValue());
        writer.write(type.getValue());
        writer.writeInt(buyPrice);
        writer.writeInt(sellPrice);
        
        // Write requirements
        writer.write((byte)requirements.size());
        for (ItemRequirement requirement : requirements) {
            writer.write(requirement.getBytes());
        }
        
        // Write stats
        writer.write((byte)stats.size());
        for (ItemStat stat : stats) {
            writer.write(stat.getBytes());
        }
        
        // Write effects
        writer.write((byte)effects.size());
        for (ItemEffect effect : effects) {
            writer.write(effect.getBytes());
        }
        
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
        try {
        	int id = slea.readInt();
        	String name = slea.read7BitPrefixedAsciiString();
        	String description = slea.read7BitPrefixedAsciiString();
        	ItemQuality quality = ItemQuality.fromValue(slea.readByte());
        	ItemType type = ItemType.fromValue(slea.readByte());
        	Integer buyPrice = slea.readInt();
        	Integer sellPrice = slea.readInt();
        	ItemData result = new ItemData(id, name, description, quality, type, buyPrice, sellPrice);
        	
        	// Read requirements
        	int count = slea.readByte();
        	for (int i=0; i < count; ++i) {
        	    result.getRequirements().add(ItemRequirement.fromBytes(slea));
        	}
        	
        	// Read stats
        	count = slea.readByte();
        	for (int i=0; i < count; ++i) {
        	    result.getStats().add(ItemStat.fromBytes(slea));
        	}
        	
        	// Read effects
        	count = slea.readByte();
        	for (int i=0; i < count; ++i) {
        	    result.getEffects().add(ItemEffect.fromBytes(slea));
        	}
        	
        	return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the requirements needed to wield / use this item.
     * @return requirements
     */
    public List<ItemRequirement> getRequirements() {
        return requirements;
    }

    /**
     * Sets the requirements needed to wield / use this item.
     * @param requirements The requirements for this item.
     */
    public void setRequirements(List<ItemRequirement> requirements) {
        this.requirements = requirements;
    }

    /**
     * Retrieves the stats that this item grants.  This is mainly just for
     * equipment.  Stats will boost a player's stats by the amount specified.
     * @return stats
     */
    public List<ItemStat> getStats() {
        return stats;
    }

    /**
     * Sets the stats that this item grants.  This is mainly just for
     * equipment.  Stats will boost a player's stats by the amount specified.
     * @param stats The stats to set for this item.
     */
    public void setStats(List<ItemStat> stats) {
        this.stats = stats;
    }

    /**
     * Retrieves the effects for this item.  Effects are typically found on
     * consumables.  They occur when a player "uses" the item.
     * @return effects
     */
    public List<ItemEffect> getEffects() {
        return effects;
    }

    /**
     * Sets the effects for this item.  Effects are typically found on
     * consumables.  They occur when a player "uses" the item.
     * @param effects The effects to set for this item.
     */
    public void setEffects(List<ItemEffect> effects) {
        this.effects = effects;
    }
}
