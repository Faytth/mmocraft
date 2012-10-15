package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.animations.AnimationType;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle Movement packets received from the server.
 * These packets are sent when another player moves.
 * [Player ID] [animation delay] [Player Location] [Animation state ID] [Direction]
 * @author Faythless
 *
 */
public class MovementHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            int playerId = slea.readInt();
            Player p = client.playerPoolSession.getPlayer(playerId);
            
            // If this is a new player, we have to add it to our pool
            if (p == null) {
                p = new Player();
                p.setId(playerId);
                p.setLocation(BoundLocation.getLocation(slea));
                p.setState(AnimationType.getState(p, null, slea.readShort()));
                p.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
                p.init();
                client.playerPoolSession.addPlayer(p);
            } else { // not a new player, so just update it
                p.setLocation(BoundLocation.getLocation(slea));
                p.setState(AnimationType.getState(p, p.getState(), slea.readShort()));
                p.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}