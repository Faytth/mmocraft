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
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.constants.ClientConstants;

/**
 * Contains all players in close proximity to the player.
 * @author Alexandria
 *
 */
public class PlayerPoolSession {

    private final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
    private final Lock readLock = locks.readLock();
    private final Lock writeLock = locks.writeLock();
    
    /** Iteration is probably important, and we might add/remove players inside
     * of a loop.  If this is a normal HashMap, this will undoubtedly cause
     * problems.  We might be able to get away with changing this later on
     * if our code is well thought out to avoid this issue.
     */
    private final Map<Integer, Player> pool = new LinkedHashMap<Integer, Player>();
    
    /**
     * Renders all players in the player pool.
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
                for (Player p : pool.values()) {
                    p.render(container, game, g, camera);
                }
            } catch (Throwable t) {
                
            }
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Updates all players in the player pool.
     * @param container
     * @param game
     * @param delta
     */
    public void update(GameContainer container, StateBasedGame game, int delta) {
        readLock.lock();
        try {
            for (Player p : pool.values()) {
                p.update(delta);
            }
            // Clear cache if too far away
            try {
                Iterator<Player> iter = pool.values().iterator();
                while (iter.hasNext()) {
                    Player player = iter.next();
                    BoundLocation playerLocation = Game.getInstance().getClient().getPlayer().getLocation();
                    if (player.getLocation().getDistance(playerLocation) > ClientConstants.OBJECT_DESPAWN_DISTANCE) {
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
     * Retrieve a player from the player pool if it exists.
     * @param playerId the ID of the player to get
     * @return player if it exists, else null
     */
    public Player getPlayer(int playerId) {
        readLock.lock();
        try {
            return pool.containsKey(playerId) ? pool.get(playerId) : null;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns whether or not this player ID exists in the pool.
     * @param playerId player ID to check
     * @return true if the player is in the pool; else false
     */
    public boolean contains(int playerId) {
        readLock.lock();
        try {
            return pool.containsKey(playerId);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Adds a player to the pool.
     * @param player the player to add to the pool
     */
    public void addPlayer(Player player) {
        writeLock.lock();
        try {
            pool.put(player.getId(), player);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Removes a player from the pool.  This should be called whenever a player gets too far
     * away.
     * @param playerId id of the player to remove
     */
    public void removePlayer(int playerId) {
        writeLock.lock();
        try {
            pool.remove(playerId);
        } finally {
            writeLock.unlock();
        }
    }

}
