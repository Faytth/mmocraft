package org.unallied.mmocraft.items;

import org.newdawn.slick.Color;

public enum ItemQuality {
	JUNK(0x00, new Color(159, 159, 159)),
	NORMAL(0x01, new Color(246, 246, 246)),
	UNCOMMON(0x02, new Color(19, 182, 61)),
	SUPERIOR(0x03, new Color(18, 146, 224)),
	EPIC(0x04, new Color(150, 27, 255)),
	LEGENDARY(0x05, new Color(191, 193, 20));
	int value = 0;
	Color qualityColor;
	
	ItemQuality(int value, Color qualityColor) {
	    this.value = value;
	    this.qualityColor = qualityColor;
	}
	
	public byte getValue() {
	    return (byte) value;
	}
	
	public static ItemQuality fromValue(int value) {
	    for (ItemQuality iq : ItemQuality.values()) {
	        if (iq.value == value) {
	            return iq;
	        }
	    }
	    
	    return null; // not found
	}
	
	/**
	 * Returns a copy of the item color.
	 * @return color
	 */
	public Color getColor() {
	    return new Color(qualityColor);
	}
}
