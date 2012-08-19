package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.sword.SwordIdle;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle Player packets received from the server.
 * These packets are sent after login and have the following format:
 * [Player object]
 * @author Faythless
 *
 */
public class PlayerHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            Player p = (Player) slea.readObject();
            p.init();
            p.setState(new SwordIdle(p, null)); // FIXME:  We need to set the idle state based on their weapon
            client.setPlayer(p);
        } catch (Throwable t) {
            t.printStackTrace();
            client.disconnect(); // our login was corrupted, so we should log out and try again.
        }
    }

}
