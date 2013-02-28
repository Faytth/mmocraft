package org.unallied.mmocraft.monsters;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains all of the monster data about all monsters.  This is a monster
 * manager for the client.  Use ServerMonsterManager for the server.
 * @author Alexandria
 *
 */
public abstract class MonsterManager {
    protected Map<Integer, MonsterData> monsters = new ConcurrentHashMap<Integer, MonsterData>();

    public void add(MonsterData monsterData) {
        if (monsterData != null) {
            monsters.put(monsterData.getId(), monsterData);
        }
    }
    
    /**
     * Attempts to retrieve the monster's data.  If the monster can't be found, null
     * is returned instead.
     * @param monsterId
     * @return The monster's data or null if it can't be found.
     */
    public abstract MonsterData getMonsterData(Integer monsterId);
    
    /**
     * Returns all monster data that's in the monster manager.
     * @return monsterData
     */
    public Collection<MonsterData> getAllMonsterData() {
        return monsters.values();
    }
    
    public abstract void load(String filename);
    
}
