package org.unallied.mmocraft;

/**
 * Contains information about a given playable character.
 * @author Ryokko
 *
 */
public class Player {

    // The exact location of this player in the world.
    private Location currentLocation = null;
    
    /**
     * Returns the absolute location of the player in the world.
     * @return the absolute location of the player.
     */
    public Location getLocation() {
        return currentLocation;
    }

}
