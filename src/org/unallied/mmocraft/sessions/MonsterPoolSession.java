package org.unallied.mmocraft.sessions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.monsters.ClientMonster;

public class MonsterPoolSession {

    private final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
    private final Lock readLock = locks.readLock();
    private final Lock writeLock = locks.writeLock();
    
    private final Map<Integer, ClientMonster> pool = new LinkedHashMap<Integer, ClientMonster>();
    
    /**
     * Renders all NPCs in the NPC pool.  NPCs include both NPCs and monsters.
     * TODO:  Add checks for players that are off the map and for players
     * that should be removed from the pool.
     * @param container
     * @param game
     * @param g
     * @param camera
     */
    public void render(GameContainer container, StateBasedGame game,
            Graphics g, BoundLocation camera) {
        readLock.lock();
        try {
            try {
                for (Living l : pool.values()) {
                    l.render(container, game, g, camera);
                }
            } catch (Throwable t) {
                
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Updates all monsters in the monster pool and removes those that are too far away.
     * @param container
     * @param game
     * @param delta
     */
    public void update(GameContainer container, StateBasedGame game, int delta) {
        readLock.lock();
        try {
            for (ClientMonster m : pool.values()) {
                m.update(delta);
            }
            // Clear cache if too far away
            try {
                Iterator<ClientMonster> iter = pool.values().iterator();
                while (iter.hasNext()) {
                    ClientMonster monster = iter.next();
                    BoundLocation playerLocation = Game.getInstance().getClient().getPlayer().getLocation();
                    if (monster.getLocation().getDistance(playerLocation) > ClientConstants.OBJECT_DESPAWN_DISTANCE) {
                        iter.remove();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Retrieve a monster from the monster pool if it exists.
     * @param monsterId The ID of the monster to get
     * @return monster if it exists, else null
     */
    public ClientMonster getMonster(int monsterId) {
        readLock.lock();
        try {
            return pool.containsKey(monsterId) ? pool.get(monsterId) : null;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Returns whether or not this monster ID exists in the pool.
     * @param monsterId Monster ID to check
     * @return true if the player is in the pool; else false
     */
    public boolean contains(int monsterId) {
        readLock.lock();
        try {
            return pool.containsKey(monsterId);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Adds a monster to the pool.
     * @param monster the monster to add to the pool
     */
    public void addMonster(ClientMonster monster) {
        writeLock.lock();
        try {
            pool.put(monster.getId(), monster);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Removes a monster from the pool.  This should be called whenever a monster
     * gets too far away.
     * @param monsterId id of the monster to remove
     */
    public void removeMonster(int monsterId) {
        writeLock.lock();
        try {
            pool.remove(monsterId);
        } finally {
            writeLock.unlock();
        }
    }
}
