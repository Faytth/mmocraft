package org.unallied.mmocraft.packets.handlers;

import org.unallied.mmocraft.Game;
import org.unallied.mmocraft.net.PacketCreator;

/**
 * This class is used for the login process initiated by the client.
 * This provides a safe and reliable mechanism for logging into the
 * server, and ensures that the server is official.
 * @author Faythless
 *
 */
public class LoginHandler {
    private String username;
    private String password;
    
    public LoginHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Initiates the server login process.  Returns true on success, false on
     * failure.
     */
    public boolean login() {
        boolean result = true;
        
        // Send a logon message to the server
        Game.getInstance().getClient().getSession().write(
                PacketCreator.getLogon(username));
        
        return result;
    }
}
