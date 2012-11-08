package org.unallied.mmocraft.net;

import java.util.ArrayList;
import java.util.List;

import org.unallied.mmocraft.client.Game;

/**
 * A heartbeat class used to send heartbeats to the server and calculate
 * latency.  This is also used to calculate the server time offset from
 * the client 
 * @author Alexandria
 *
 */
public class Heartbeat implements Runnable {

    /** The amount of time in milliseconds to sleep between ping-pong requests. */
    private static final long SLEEP_TIME = 10000;
    
    /** 
     * The maximum number of latencies to store.  Lower values will cause
     * latencies that respond faster to abrupt changes, longer values will
     * cause a more "average" approximation.
     */
    private static final int MAX_LATENCIES = 5;
    
    /** A list of the past x number of latencies. */
    private static List<Long> latencies = new ArrayList<Long>();
    
    @Override
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                Game.getInstance().getClient().announce(PacketCreator.getPing());
                Thread.sleep(SLEEP_TIME);
                try {
                    keepRunning = Game.getInstance().getContainer().running();
                } catch (NullPointerException e) {
                    keepRunning = false; // Critical error
                }
            } catch (Exception e) {
                // No one cares if it failed
            }
        }
    }

    /**
     * Updates the 
     * @param serverTime
     */
    public static void updatePing(long clientTime, long serverTime) {
        if (latencies.size() > MAX_LATENCIES) {
            latencies.remove(0);
        }
        latencies.add(serverTime - clientTime);
    }
}
