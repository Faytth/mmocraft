package org.unallied.mmocraft.client;

import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.net.Packet;
import org.unallied.mmocraft.net.PacketCreator;

import org.apache.mina.core.session.IoSession;

/**
 * This class houses the Player as well as some important account information.
 * @author Faythless
 *
 */
public class MMOClient {
    public static final String CLIENT_KEY = "CLIENT";
    
    private IoSession session; // The current session
    private org.unallied.mmocraft.Player player; // The player associated with this account
    private int accountId;            // Player's account id
    private boolean loggedIn = false; // Whether this account is logged in
    private String accountName; // The username of the logged in account
    private String accountPass; // The password of the logged in account
    private long lastPing = 0;  // The time that the last ping was received from the server
    
    public MMOClient() {
    }
    
    public MMOClient(IoSession session) {
        this.session = session;
    }
    
    /**
     * Return the IoSession associated with this connection
     * @return the IoSession for this connection
     */
    public synchronized IoSession getSession() {
        return session;
    }
    
    /**
     * Sets the current connection's session
     * @param session the session for the connection
     */
    public synchronized void setSession(IoSession session) {
        this.session = session;
    }
    
    /**
     * Returns the player associated with this account
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    /**
     * Get player from server
     */
    private void loadPlayer() {
        /**/
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public void login(String username, String password) {
        
    }
    
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public int getAccountId() {
        return accountId;
    }
    
    public String getAccountName() {
        return accountName;
    }
    
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    /**
     * Remove a player from the server
     */
    private void removePlayer() {
        
    }
    
    /**
     * Disconnects a player from the server
     */
    public final void disconnect() {
        try {
            if (player != null && isLoggedIn()) {
                removePlayer();
                player.save(true);
            }
        } finally {
            player = null;
            session.close(false);
        }
    }
    
    /**
     * Empties out the connection
     */
    public final void empty() {
        player = null;
        session = null;
    }
    
    /**
     * Update the time that the last ping was received to the current time
     */
    public void pingReceived() {
        lastPing = System.currentTimeMillis();
    }
    
    /**
     * Send a ping to the client to make sure it's still alive
     */
    public void sendPong() {
        final long pongSent = System.currentTimeMillis();
        announce(PacketCreator.getPong());
        // FIXME:  Not finished!!!
    }
    
    public void announce(Packet packet) {
        session.write(packet);
    }
}
