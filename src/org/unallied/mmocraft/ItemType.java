package org.unallied.mmocraft;

public enum ItemType {
    EQUIPMENT(0x00),
    CONSUMABLES(0x01),
    TRADE_GOODS(0x02),
    BLOCKS(0x03),
    MISCELLANEOUS(0x04);
    int value = 0;
    
    ItemType(int value) {
        this.value = value;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static ItemType fromValue(int value) {
        for (ItemType it : ItemType.values()) {
            if (it.value == value) {
                return it;
            }
        }
        
        return null; // not found
    }
    
    /**
     * Returns the string representation of this item type, useful when you need
     * the actual name such as "Equipment" or "Trade Goods."
     * @return name The name of the item type
     */
    public String toString() {
        String result;
        
        switch (this) {
        case EQUIPMENT:
            result = "Equipment";
            break;
        case CONSUMABLES:
            result = "Consumables";
            break;
        case TRADE_GOODS:
            result = "Trade Goods";
            break;
        case BLOCKS:
            result = "Blocks";
            break;
        case MISCELLANEOUS:
            result = "Miscellaneous";
            break;
        default:
            result = ""; // not found
            break;
        }
        
        return result;
    }
}
