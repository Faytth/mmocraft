package org.unallied.mmocraft;

/**
 * Contains all types of stats, such as Min / Max Weapon Damage, Armor, Strength, and so on.
 * @author Alexandria
 *
 */
public enum StatType {
    MIN_ATTACK(0x00, "Minimum Attack"),
    MAX_ATTACK(0x01, "Maximum Attack"),
    
    ARMOR(0x02, "Armor"),
    
    STRENGTH(0x03, "Strength"),
    DEXTERITY(0x04, "Dexterity"),
    INTELLECT(0x05, "Intellect"),
    VITALITY(0x06, "Vitality"),
    SPIRIT(0x07, "Spirit"),
    
    HPS(0x08, "Healing Per Second"),
    PHYSICAL_COUNTER(0x09, "Physical Counter"),
    MAGICAL_COUNTER(0x0A, "Magical Counter"),
    HASTE(0x0B, "Haste"),
    FIRE_DAMAGE(0x0C, "Fire Damage"),
    WATER_DAMAGE(0x0D, "Water Damage"),
    LIGHTNING_DAMAGE(0x0E, "Lightning Damage"),
    EARTH_DAMAGE(0x0F, "Earth Damage"),
    LIGHT_DAMAGE(0x10, "Light Damage"),
    SHADOW_DAMAGE(0x11, "Shadow Damage"),
    
    PHYSICAL_RESISTANCE(0x12, "Physical Resistance"),
    FIRE_RESISTANCE(0x13, "Fire Resistance"),
    WATER_RESISTANCE(0x14, "Water Resistance"),
    LIGHTNING_RESISTANCE(0x15, "Lightning Resistance"),
    EARTH_RESISTANCE(0x16, "Earth Resistance"),
    LIGHT_RESISTANCE(0x17, "Light Resistance"),
    SHADOW_RESISTANCE(0x18, "Shadow Resistance");
    int value = 0;
    String name = "";
    
    StatType(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static StatType fromValue(int value) {
        for (StatType st : StatType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
