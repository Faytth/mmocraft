package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This packet is sent from the server when there is an error when attempting
 * to log on, such as invalid username / password.  The format is:
 * [loginErrorMessage]
 * @author Faythless
 *
 */
public class LoginErrorHandler extends AbstractPacketHandler {

    private static Object lastErrorMutex = new Object();
    private static int lastError = 0;
    
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            synchronized (lastErrorMutex) {
                lastError = slea.readInt();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            client.disconnect(); // Our login was corrupted, so we should log out and try again.
        }
    }

    /**
     * Returns the last error message received for logging in.
     * @return lastError
     */
    public static int getLastError() {
        synchronized (lastErrorMutex) {
            return lastError;
        }
    }
}
