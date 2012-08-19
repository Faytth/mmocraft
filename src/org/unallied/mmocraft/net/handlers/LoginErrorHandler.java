package org.unallied.mmocraft.net.handlers;

import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.PrintError;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This packet is sent from the server when there is an error when attempting
 * to log on, such as invalid username / password.  The format is:
 * [loginErrorMessage]
 * @author Faythless
 *
 */
public class LoginErrorHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
        PrintError.print(PrintError.ERROR_MESSAGE, slea.readPrefixedAsciiString());
    }

}
