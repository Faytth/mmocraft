package org.unallied.mmocraft.net.handlers;

import java.security.SecureRandom;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.tools.Hasher;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class ChallengeHandler extends AbstractPacketHandler {

    @Override
    /**
     * A message containing a [serverNonce].  Sent after a logon request.
     * The client needs to respond with a CREDS packet.
     * @param slea The packet
     * @param client The client (there should only be one...)
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        int serverNonce = slea.readInt();
        int clientNonce = new SecureRandom().nextInt();
        client.loginSession.setServerNonce(serverNonce);
        client.loginSession.setClientNonce(clientNonce);
        
        // Get the client hash
        byte[] clientHash = Hasher.getSHA256(
                (client.loginSession.getClientNonce()
                + client.loginSession.getServerNonce()
                + client.loginSession.getPassword()).getBytes(
                        ClientConstants.CHARSET) );
        
        // Send a CREDS packet to the server
        client.announce(PacketCreator.getCreds(clientNonce, clientHash));
    }
}
