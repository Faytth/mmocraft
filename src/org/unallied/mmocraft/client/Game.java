package org.unallied.mmocraft.client;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.net.PacketSocket;
import org.unallied.mmocraft.states.*;

public class Game extends StateBasedGame {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int MAX_FPS = 60;
    
    /// We are not able to use the superior Singleton pattern for this due to applet complications
    protected static Game instance = null;
    
    /** The client associated with this game */
    protected MMOClient client = new MMOClient();

    /** Contains all controls and provides an easy context for configuring said controls. */
    protected Controls controls = new Controls();
    
    /**
     * Must be public to make AppGameContainer happy for AppLoader.
     * DO NOT USE THE CONSTRUCTOR IN YOUR CODE!!!
     */
    public Game() {
        super("MMOCraft");
        instance = this;

        this.addState(new LoginState());
        this.addState(new IngameState());
        this.addState(new RegisterState());
    }
    
    public static void main( String[] args ) {
    	// NOTE:  Most of the container initialization is performed in LoginState.init()
        try {
            AppGameContainer app = new AppGameContainer( getInstance() );
            Display.setResizable(true);
            app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            app.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        // Cleanup
        try {
            PacketSocket.getInstance().close();
        } catch (Exception e) {
            // don't care.  We're closing.
        }
    }
    
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
    }
            
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
    
    /**
     * Returns the MMOClient associated with this game.  This is a class that
     * stores stuff about a player's connection / account.
     * @return client
     */
    public MMOClient getClient() {
        return client;
    }
    
    /**
     * Returns the game controls class which stores all information about which
     * keys / controls do what.
     * @return controls
     */
    public Controls getControls() {
    	return controls;
    }
    
    /**
     * Retrieves the width of the container that this game is in.  Returns 0 if
     * the container doesn't exist.
     * @return width
     */
    public int getWidth() {
        GameContainer container = getContainer();
        return container == null ? 0 : container.getWidth();
    }
    
    /**
     * Retrieves the height of the container that this game is in.  Returns 0 if
     * the container doesn't exist.
     * @return height
     */
    public int getHeight() {
        GameContainer container = getContainer();
        return container == null ? 0 : container.getHeight();
    }
}
