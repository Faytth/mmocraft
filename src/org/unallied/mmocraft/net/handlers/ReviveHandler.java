package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.Velocity;
import org.unallied.mmocraft.animations.AnimationID;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class ReviveHandler extends AbstractPacketHandler {
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        try {
            Player player = client.getPlayer();
            
            if (player != null) {
                player.setHpCurrent(slea.readInt());
                player.setLocation(BoundLocation.fromBytes(slea));
                player.setState(AnimationID.getState(player,  null, slea.readShort()));
                player.setVelocity(Velocity.fromBytes(slea));
                player.setFallSpeed(slea.readFloat());
                player.setInitialVelocity(slea.readFloat());
            }
        } catch (Throwable t) {
            
        }
    }
}
