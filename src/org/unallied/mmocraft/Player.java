package org.unallied.mmocraft;

/**
 * Contains all information for a given player.
 * @author Faythless
 *
 */
public class Player extends Living {

    private int playerId; // The unique ID of this player
    
    /**
     * Returns this player's unique identifier
     * @return playerId
     */
    public int getId() {
        return playerId;
    }

    /**
     * Saves everything about the Player to the database
     * @param b
     */
    public void save(boolean b) {
        // TODO Auto-generated method stub
        
    }
}
