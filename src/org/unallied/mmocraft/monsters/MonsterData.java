package org.unallied.mmocraft.monsters;

import java.util.Map;

import org.unallied.mmocraft.LootIntf;
import org.unallied.mmocraft.Passive;
import org.unallied.mmocraft.PassiveType;
import org.unallied.mmocraft.animations.AnimationType;

public interface MonsterData {

    /**
     * Serializes the bytes for this class.  This is used in place of
     * writeObject / readObject because Java adds a lot of "extra"
     * stuff that isn't particularly useful in this case.
     * 
     * @return byteArray
     */
    public byte[] getBytes();
    
    public Integer getId();
    
    public String getName();
    
    public int getMaxHP();
    
    public short getLevel();
    
    public short getWidth();
    
    public short getHeight();
    
    public double getDamageMultiplier();
    
    public float getMovementSpeed();
    
    public MonsterType getType();
    
    public Map<AnimationType, String> getAnimations();
    
    public Map<PassiveType, Passive> getPassives();
    
    public int getExperience();
    
    public int getMinimumGold();
    
    public int getMaximumGold();

    public LootIntf getLoot();
}
