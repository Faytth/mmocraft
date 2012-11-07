package org.unallied.mmocraft.sessions;

import org.unallied.mmocraft.tools.Hasher;

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
    
    /**
     * Gets the hashed password.
     * @return password Returns the hashed password.
     */
    public String getPassword() {
        byte[] byteData = Hasher.getSHA256((username + password).getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < byteData.length; ++i) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
    /**
     * Sets the password.
     * @param password the unhashed password
     */
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
