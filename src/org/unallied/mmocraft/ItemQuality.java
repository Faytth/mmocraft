package org.unallied.mmocraft;

public enum ItemQuality {
	JUNK(0x00),
	NORMAL(0x01),
	UNCOMMON(0x02),
	SUPERIOR(0x03),
	EPIC(0x04),
	LEGENDARY(0x05);
	int value = 0;
	
	ItemQuality(int value) {
	    this.value = value;
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
}
