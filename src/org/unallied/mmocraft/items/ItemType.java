package org.unallied.mmocraft.items;

public enum ItemType {
    UNASSIGNED(0x00, "Unassigned"),
    EQUIPMENT(0x01, "Equipment"),
    CONSUMABLES(0x02, "Consumables"),
    TRADE_GOODS(0x03, "Trade Goods"),
    BLOCKS(0x04, "Blocks"),
    MISCELLANEOUS(0x05, "Miscellaneous"),
    SHIELD(0x06, "Shield"),
    HEAD(0x07, "Head"),
    NECK(0x08, "Neck"),
    SHOULDER(0x09, "Shoulder"),
    BACK(0x0A, "Back"),
    CHEST(0x0B, "Chest"),
    WRIST(0x0C, "Wrist"),
    HAND(0x0D, "Hand"),
    WAIST(0x0E, "Waist"),
    LEG(0x0F, "Leg"),
    FOOT(0x10, "Foot"),
    RING(0x11, "Ring"),
    TRINKET(0x12, "Trinket"),
    TWIN_BLADES(0x13, "Twin Blades");
    
    int value = 0;
    String name = "";
    
    ItemType(int value, String name) {
        this.value = value;
        this.name = name;
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
        return name;
    }
}
