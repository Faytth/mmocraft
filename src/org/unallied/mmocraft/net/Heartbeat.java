package org.unallied.mmocraft.net;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Long> latencies = new ArrayList<Long>();
    private List<Long> orderedLatencies = new ArrayList<Long>();
    
    /** The time difference between the client and server. */
    private long timeOffset = 0;
    
    @Override
    /**
     * Starts listening for packets.
     */
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                if (Game.getInstance().getClient().isLoggedIn()) {
                    Game.getInstance().getClient().announce(PacketCreator.getPing());
                    Thread.sleep(SLEEP_TIME);
                    try {
                        keepRunning = Game.getInstance().getContainer().running();
                    } catch (NullPointerException e) {
                        keepRunning = false; // Critical error
                    }
                }
            } catch (Exception e) {
                // No one cares if it failed
            }
        }
    }

    /**
     * Retrieves the average latency between the client and the server.  The
     * round trip time is twice this latency.  The latency is the time it takes
     * for the client to contact the server or vice versa.
     * @return averageLatency
     */
    public long getAverageLatency() {
        if (!orderedLatencies.isEmpty()) {
            return orderedLatencies.get(orderedLatencies.size() / 2); // return median (latencies are always sorted)
        } else {
            return 0;
        }
    }
    
    /**
     * Retrieves the time offset between the client and the server.
     * @return timeOffset
     */
    public long getTimeOffset() {
        return timeOffset;
    }
    
    /**
     * Updates the latency based on a client start time, server response time,
     * and the current time.  The client start time is the time that the
     * packet was sent to the server, the server time is the time that the
     * server received the packet
     * @param clientTime
     * @param serverTime
     */
    public void updateLatency(long clientTime, long serverTime) {
        synchronized (this) {
            if (latencies.size() > MAX_LATENCIES) {
                latencies.remove(0);
            }
            latencies.add((System.currentTimeMillis() - clientTime) / 2);
            orderedLatencies = new ArrayList<Long>(latencies);
            Collections.sort(orderedLatencies);
        }
        timeOffset = (serverTime - getAverageLatency()) - clientTime;
    }
    
    private static class HeartbeatHolder {
        private static Heartbeat instance = new Heartbeat();
    }
    
    public static Heartbeat getInstance() {
        return HeartbeatHolder.instance;
    }
    
    /**
     * Retrieves a local timestamp from a server timestamp.  This is calculated
     * from the latency information retrieved from ping-pong events.  The local
     * timestamp is roughly the equivalent of having called 
     * {@link System#currentTimeMillis()} on the client.  Returns -1 if timestamp
     * is -1.
     * @param timestamp The server timestamp
     * @return localTime Returns -1 if timestamp is -1, else returns the local
     *                   time.
     */
    public long getLocalTimestamp(long timestamp) {
        if (timestamp == -1) {
            return timestamp;
        }
        return timestamp - timeOffset - getAverageLatency();
    }
}
