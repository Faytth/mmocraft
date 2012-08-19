package org.unallied.mmocraft.net.handlers;

import java.util.Arrays;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.Hasher;
import org.unallied.mmocraft.tools.PrintError;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

public class VerifyHandler extends AbstractPacketHandler {

    @Override
    /**
     * A message containing a [Hash(serverNonce.clientNonce.password)].
     */
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        byte[] serverHash = slea.read(ClientConstants.HASH_SIZE);
        byte[] computedHash = Hasher.getSHA256(
                (client.loginSession.getServerNonce()
                + client.loginSession.getClientNonce()
                + client.loginSession.getPassword()).getBytes(
                        ClientConstants.CHARSET) );
        
        // Compare our computed hash with the serverHash.  They should match.
        if (computedHash != null && Arrays.equals(serverHash, computedHash)) {
            // Go ahead and log in
            Game.getInstance().enterState(GameState.INGAME);
        } else {
            // There was an error.  Uh oh!  Bad server!!!
            PrintError.print(PrintError.ERROR_MESSAGE, 
                    "Server provided bad credentials.  Not an official server!");
        }
    }

}
