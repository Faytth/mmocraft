package org.unallied.mmocraft.skills;

/**
 * Contains all types of skills that the player can level.
 * @author Alexandria
 *
 */
public enum SkillType {
    CONSTITUTION(0x00, "Constitution"),
    STRENGTH(0x01, "Strength"),
    DEFENCE(0x02, "Defence"),
    MINING(0x03, "Mining"),
    SMITHING(0x04, "Smithing"),
    FISHING(0x05, "Fishing"),
    COOKING(0x06, "Cooking");
    int value = 0;
    String name = "";
    
    SkillType(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static SkillType fromValue(int value) {
        for (SkillType st : SkillType.values()) {
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
