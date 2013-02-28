package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Direction;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.Velocity;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle Movement packets received from the server.
 * These packets are sent when another player moves.
 * [Player ID] [Player Location] [Animation state ID] [Direction] [Velocity] [Fall Speed] [Initial Velocity]
 * @author Faythless
 *
 */
public class PlayerMovementHandler extends AbstractPacketHandler {

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
                p.setState(AnimationID.getState(p, null, slea.readShort()));
                p.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
                p.init();
                p.setVelocity(Velocity.fromBytes(slea));
                p.setFallSpeed(slea.readFloat());
                p.setInitialVelocity(slea.readFloat());
                //p.update(Heartbeat.getInstance().getAverageLatency()); // account for latency
                client.playerPoolSession.addPlayer(p);
                // Request player info from server
                client.announce(PacketCreator.getPlayerInfo(playerId));
            } else { // not a new player, so just update it
                p.setLocation(BoundLocation.getLocation(slea));
                p.setState(AnimationID.getState(p, p.getState(), slea.readShort()));
                p.setDirection(slea.readByte() == 0 ? Direction.RIGHT : Direction.LEFT);
                p.setVelocity(Velocity.fromBytes(slea));
                p.setFallSpeed(slea.readFloat());
                p.setInitialVelocity(slea.readFloat());
                //p.update(Heartbeat.getInstance().getAverageLatency()); // account for latency
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}