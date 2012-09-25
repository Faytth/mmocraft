package org.unallied.mmocraft;

/**
 * Contains all types of requirements, such as Twin Blades, Defense Level, and so on.
 * @author Alexandria
 *
 */
public enum RequirementType {
    TWIN_BLADES(0x00, "Twin Blades");
    int value = 0;
    String name = "";
    
    RequirementType(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public byte getValue() {
        return (byte) value;
    }
    
    public static RequirementType fromValue(int value) {
        for (RequirementType rt : RequirementType.values()) {
            if (rt.value == value) {
                return rt;
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
