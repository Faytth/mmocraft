package org.unallied.mmocraft.net;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public interface PacketHandler {
    void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client);
    boolean validState(MMOClient client);
}
