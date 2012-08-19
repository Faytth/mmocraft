package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle registration acquisition packets.
 * These packets are sent after a player tries to register an account.  They
 * communicate the status of the registration (pass / fail).
 * [passOrFail]
 * @author Faythless
 *
 */
public class RegistrationAckHandler extends AbstractPacketHandler {

    private static int lastError = 0;
    
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            /*
             *  We don't need a mutex because this is the only place that writes to
             *  lastError, and an inconsistent read doesn't matter much.
             */
            lastError = slea.readByte();
            if (lastError == 0) {
                // Return the player to the login screen.
                Game.getInstance().enterState(GameState.LOGIN);
            } else {
                /*
                 *  Tell the player that they failed to register. 
                 *  There's no need to actually do anything here, because
                 *  the static text updates itself based on lastError's value.
                 */
            }
        } catch (Throwable t) {
            t.printStackTrace();
            client.disconnect(); // our login was corrupted, so we should log out and try again.
        }
    }
    
    /**
     * Returns the last error message received for creating an account.
     * @return lastError
     */
    public static int getLastError() {
        return lastError;
    }

}
