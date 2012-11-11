package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.Heartbeat;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PvPToggleResponseHandler extends AbstractPacketHandler {
    /** 
     * The time in System.currentTimeMillis() at which the PvP flag will be
     * disabled.  A time of -1 means that the PvP flag is not in the process
     * of being disabled.
     */
    private static long pvpExpireTime = 0;
    
    @Override
    /**
     * Handler for receiving information about when the PvP time will expire.
     * @param slea
     * @param client
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        synchronized (this) {
            int playerId = slea.readInt();
            long serverTime = slea.readLong();
            if (playerId == client.getPlayer().getId()) { // This is us!
                pvpExpireTime = Heartbeat.getInstance().getLocalTimestamp(serverTime);
            } else { // This is someone else
                Player p = client.playerPoolSession.getPlayer(playerId);
                if (p != null) {
                    p.setPvPTime(Heartbeat.getInstance().getLocalTimestamp(serverTime));
                }
            }
        }
    }
    
    /**
     * Retrieves whether the player has PvP enabled or disabled.  Note that this
     * indicates whether the player has turned their PvP on or off, and not whether
     * the PvP flag is on.  To get whether or not the player is flagged for PvP,
     * use {@link #getPvPEnabled()}.
     * @return pvpToggled
     */
    public static boolean getPvPToggled() {
        return pvpExpireTime == -1;
    }
    
    /**
     * Returns true if the PvP flag has not yet been disabled.  Once the PvP
     * flag is disabled, there is a "cooldown" time associated with it before
     * the PvP flag will disappear.
     * 
     * Note that this returns only whether the player has PvP enabled or disabled.
     * It does not indicate whether the PvP flag is on or off.
     * @return true if the user has PvP enabled, else false.
     */
    public static boolean getPvPEnabled() {
        return getPvPFlagDuration() > 0 || getPvPFlagDuration() == -1;
    }
    
    /**
     * Retrieves the amount of time in milliseconds until the PvP flag expires.
     * Returns -1 if the PvP flag will not expire.
     * @return pvpFlagDuration Returns time in milliseconds until the PvP flag
     *                         expires, or 0 if it has already expired.
     */
    public static long getPvPFlagDuration() {
        long result = pvpExpireTime;
        if (result != -1) {
            result = pvpExpireTime - System.currentTimeMillis();
            result = result < 0 ? 0 : result;
        }
        
        return result;
    }
}
