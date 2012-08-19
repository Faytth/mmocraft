package org.unallied.mmocraft.net.sessions;

/**
 * Contains important information about client / server nonces and other
 * information related to a single instance of logging in.
 * @author Faythless
 *
 */
public class LoginSession {
    private String username = "";
    private String password = "";
    private int clientNonce; // Nonce created by the client
    private int serverNonce; // Nonce created by the server
    
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username for the login session
     * @param username account username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getClientNonce() {
        return clientNonce;
    }
    
    public void setClientNonce(int clientNonce) {
        this.clientNonce = clientNonce;
    }
    
    public int getServerNonce() {
        return serverNonce;
    }
    
    public void setServerNonce(int serverNonce) {
        this.serverNonce = serverNonce;
    }
}
