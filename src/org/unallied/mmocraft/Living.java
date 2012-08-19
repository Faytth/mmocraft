package org.unallied.mmocraft;

/**
 * Anything that can be considered "alive," such as a player, monster, or NPC
 * is derived from this class.
 * @author Faythless
 *
 */
public abstract class Living extends GameObject {

    /**
     * 
     */
    private static final long serialVersionUID = 6521921565335367996L;

    // The name of the creature, such as "Bob" or "Charles the Unicorn"
    protected String name = "";
    
    // The maximum HP for this creature.  If it's alive, it can die!!!
    protected int hpMax = 0;
    
    // The current HP for this creature
    protected int hpCurrent = 0;
    
    /**
     * Returns the name of the living creature, such as "Bob" or "Alice."
     * @return name of the living creature
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the living creature, such as "Bob" or "Alice."
     * @param name new name of the living creature
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the maximum HP for this creature.
     * @return maximum HP
     */
    public int getHpMax() {
        return hpMax;
    }
    
    /**
     * Sets the maximum HP for this creature.
     * @param hpMax maximum HP
     */
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    
    /**
     * Returns the current HP for this creature.  
     * Current HP is always <= max HP.
     * @return current HP
     */
    public int getHpCurrent() {
        return hpCurrent;
    }
    
    /**
     * Sets the current HP for this creature.
     * Current HP cannot exceed max HP.
     * @param hpCurrent current HP
     */
    public void setHpCurrent(int hpCurrent) {
        this.hpCurrent = hpCurrent;
        
        // Make sure we didn't go over the max
        if (this.hpCurrent > hpMax) {
            this.hpCurrent = hpMax; // If we went over, set the HP to max
        }
    }
}
