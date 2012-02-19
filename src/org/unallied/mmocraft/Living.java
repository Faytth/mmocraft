package org.unallied.mmocraft;

/**
 * Anything that can be considered "alive," such as a player, monster, or NPC
 * is derived from this class.
 * @author Faythless
 *
 */
public abstract class Living extends GameObject {

    // The maximum HP for this creature.  If it's alive, it can die!!!
    private int hpMax;
    
    // The current HP for this creature
    private int hpCurrent;
}
