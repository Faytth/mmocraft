package org.unallied.mmocraft;

/**
 * A class used for generalized items.  This class should not be used for
 * storing the information of a weapon or piece of armor.
 * @author Alexandria
 *
 */
public class Item {
	/** The item name. */
	private String name = "Null";
	
	/** The item description.  Appears below items when hovering over them.  */
	private String description = "No description.";
	
	/** 
	 * The quality of the item.  Item quality determines  what color an item
	 * name appears in.
	 */
	private ItemQuality quality = ItemQuality.NORMAL;
	
	/** The price that you can buy the item from the vendor for. */
	private Integer buyPrice = 0;
	
	/** The price that the item sells for to the vendor. */
	private Integer sellPrice = 0;
	
	/**
	 * Creates an item with the specified parameters.  This class is only for
	 * general items and should not be used for weapons / armor.
	 * @param name The item name, such as "Uber Greatsword."
	 * @param description The item description, such as, "The best greatsword ever."
	 * @param quality The item quality, such as junk, normal, epic.
	 * @param buyPrice The price that you can buy the item for from the vendor.
	 * @param sellPrice The price that you can sell an item to the vendor for.
	 */
	public Item(String name, String description, ItemQuality quality, 
			Integer buyPrice, Integer sellPrice) {		
		this.setName(name);
		this.setDescription(description);
		this.setQuality(quality);
		this.setBuyPrice(buyPrice);
		this.setSellPrice(sellPrice);
	}

	public Integer getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Integer getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Integer buyPrice) {
		this.buyPrice = buyPrice;
	}

	public ItemQuality getQuality() {
		return quality;
	}

	public void setQuality(ItemQuality quality) {
		this.quality = quality;
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

	public void setName(String name) {
		this.name = name;
	}
}
