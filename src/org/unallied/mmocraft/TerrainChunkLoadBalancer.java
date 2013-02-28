package org.unallied.mmocraft;

/**
 * The terrain chunk load balancer ensures that refreshes and loads of terrain
 * chunks do not halt the game for a noticeable amount of time.  It does this
 * by limiting the number of refreshes and loads per render.
 * TODO:  Generalize this to work for any class, and not just terrain chunks.
 * @author Alexandria
 * 
 *
 */
public class TerrainChunkLoadBalancer {
    
    /** True if we're allowed to load or refresh a terrain chunk on this render loop. */
    private boolean loadable = true;
    
    private TerrainChunkLoadBalancer() {
    }
    
    private static class TerrainChunkLoadBalancerHolder {
        private static TerrainChunkLoadBalancer instance = new TerrainChunkLoadBalancer();
    }
    
    public static TerrainChunkLoadBalancer getInstance() {
        return TerrainChunkLoadBalancerHolder.instance;
    }
    
    /**
     * Returns true if the terrain chunk is allowed to load or refresh.  Calling
     * this method will cause {@link #canLoad()} to return false on every
     * consecutive call until it is reset.
     * @return true if this chunk is allowed to load or refresh, else false.
     */
    public boolean canLoad() {
        boolean result = loadable;
        loadable = !loadable;
        return result;
    }
    
    /**
     * This should be called after every render loop.  It resets the status
     * of whether or not a chunk can be loaded.
     */
    public void resetLoad() {
        loadable = true;
    }
}
