package org.unallied.mmocraft.net.sessions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
     * @return password Returns the hashed password.  On failure returns the
     *                  empty string.
     */
    public String getPassword() {
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(username.getBytes());
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i=0; i < byteData.length; ++i) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { //Uh oh!
            System.out.println(e.getMessage());
        }
        
        return ""; // Failed
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
