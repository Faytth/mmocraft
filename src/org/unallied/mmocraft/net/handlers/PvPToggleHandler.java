package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class PvPToggleHandler extends AbstractPacketHandler {
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
        long pvpExpireTime = Game.getInstance().getClient().getLocalTimestamp(slea.readLong());
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
        return pvpExpireTime == -1;
    }
}
