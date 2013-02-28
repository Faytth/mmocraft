package org.unallied.mmocraft.monsters;

public enum MonsterType {
    UNASSIGNED(0x00, "Unassigned"),
    GROUND(0x01, "Ground"),
    FLYING(0x02, "Flying");
    
    int value = 0;
    String name = "";
    
    MonsterType(int value, String name)
    {
        this.value = value;
        this.name = name;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static MonsterType fromValue(int value) {
        for (MonsterType mt : MonsterType.values()) {
            if (mt.value == value) {
                return mt;
            }
        }
        
        return null; // not found
    }
    
    /**
     * Returns the string representation of this monster type, useful when you
     * need the actual name such as "Ground" or "Flying."
     * @return name The name of the monster type
     */
    public String toString() {
        return name;
    }
}
