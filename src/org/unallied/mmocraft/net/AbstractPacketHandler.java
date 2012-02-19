package org.unallied.mmocraft.net;

import org.unallied.mmocraft.net.PacketHandler;
import org.unallied.mmocraft.client.MMOClient;


public abstract class AbstractPacketHandler implements PacketHandler {
    @Override
    public boolean validState(MMOClient client) {
        return true; //client.isLoggedIn();
    }
}
