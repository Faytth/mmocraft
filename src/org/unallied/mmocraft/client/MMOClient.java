package org.unallied.mmocraft.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.net.Packet;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.PacketSocket;
import org.unallied.mmocraft.net.sessions.LoginSession;
import org.unallied.mmocraft.net.sessions.NPCSession;
import org.unallied.mmocraft.net.sessions.ObjectSession;
import org.unallied.mmocraft.net.sessions.PlayerPoolSession;
import org.unallied.mmocraft.net.sessions.TerrainSession;

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
    private long lastPing = 0;  // The time that the last ping was received from the server
    
    private Camera camera = null;
    
    // Stores important information about a client's login information
    public LoginSession loginSession = new LoginSession();

    // Stores important information about all of the terrain, such as world chunks
    public TerrainSession terrainSession = new TerrainSession();

    // Stores all of the inanimate game objects
    public ObjectSession objectSession = new ObjectSession();

    // Stores all other players
    public PlayerPoolSession playerPoolSession = new PlayerPoolSession();

    // Stores all non-playable characters
    public NPCSession npcSession = new NPCSession();
    
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
        if (player != null) {
            this.player = player;
            camera = new Camera(new BoundLocation(this.player.getLocation()));
        }
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
        try {
            // Reconnect if needed
            if (session == null || !session.isConnected()) {
                session = PacketSocket.getInstance().getSession();
            }
            session.write(packet);
        } catch (Throwable t) {
            // Error sending packet.  This can be caused by a connection failure.
            session = PacketSocket.getInstance().getSession();
        }
    }

    /**
     * Renders all of the game world.  This includes terrain, objects, players,
     * and NPCs.
     * @param container
     * @param game
     * @param g
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
        try {
            // Get player's Location.  Used to determine rendering location
            BoundLocation location = player.getLocation();
            BoundLocation cameraLocation = new BoundLocation(location);
            
            // Adjust camera location
            cameraLocation.moveLeft(container.getWidth() / 2);
            cameraLocation.moveUp(container.getHeight() / 2);
            
            // Just in case...
            if (camera == null) {
                camera = new Camera(cameraLocation);
            }
            
            camera.setLocation(cameraLocation);
            cameraLocation = camera.getLocation();
            
            // Render the background
            ImageHandler.getInstance().draw(ImageID.BACKGROUND_SKY.toString(), 0, 0);
            
            if (cameraLocation != null) {
                // Render the "back" blocks of the world
                
                // Render the "blocks" of the world
                terrainSession.render(container, game, g, cameraLocation);
    
                // Render the inanimate objects
                objectSession.render(container, game, g, cameraLocation);
                
                // Render the other players
                playerPoolSession.render(container, game, g, cameraLocation);
                
                // Render the NPCs
                npcSession.render(container, game, g, cameraLocation);
                
                // Render yourself
                player.render(container, game, g, cameraLocation);
            }
        } catch (Throwable t) {
            // Do nothing.  It's a rendering bug.  So what?
        }
    }

    /**
     * Returns the terrain session associated with this client.
     * The terrain session manages all of the loaded chunks and also handles
     * rendering of those chunks.
     * @return terrainSession
     */
    public TerrainSession getTerrainSession() {
        return terrainSession;
    }
}
