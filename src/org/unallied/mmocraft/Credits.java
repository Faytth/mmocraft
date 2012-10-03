package org.unallied.mmocraft;

/** 
 * A class containing all of the people who deserve credit for helping with the
 * game.
 * 
 * @author Alexandria
 */
public enum Credits {
    ALEXANDRIA_CORNWALL("Creator", "Alexandria \"Faythless\" Cornwall"),
    KITTERZ("Programmer", "Kitterz"),
    KEEGAN_FRENCH_CORNWALL("Artist", "Keegan \"Ritorushi\" French-Cornwall");
    String name;
    String position;
    
    Credits(String name, String position) {
        this.position = position;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return position + ": " + name;
    }
    
    public static Credits getRandomCredits() {
        return Credits.values()[Credits.values().length-1];
    }
}
