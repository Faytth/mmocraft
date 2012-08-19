package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle player disconnection packets.
 * These packets are sent when someone nearby disconnects from the game.
 * [Player ID]
 * @author Faythless
 *
 */
public class PlayerDisconnectHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int playerId = slea.readInt();
            client.playerPoolSession.removePlayer(playerId);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}