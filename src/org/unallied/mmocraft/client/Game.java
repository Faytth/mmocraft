package org.unallied.mmocraft.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.net.PacketSocket;
import org.unallied.mmocraft.states.*;

public class Game extends StateBasedGame {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    protected static final int MAX_FPS = 60000;
    
    /// We are not able to use the superior Singleton pattern for this due to applet complications
    protected static Game instance = null;
    
    protected MMOClient client = new MMOClient(); // The client associated with this game

    /**
     * Must be public to make AppGameContainer happy for AppLoader.
     * DO NOT USE THE CONSTRUCTOR IN YOUR CODE!!!
     */
    public Game() {
        super("MMOCraft");
        instance = this;
        // Calling this causes our client's session to be set
        //PacketSocket.getInstance().connect();

        this.addState(new LoginState());
        this.addState(new IngameState());
        this.addState(new RegisterState());
    }
    
    public static void main( String[] args ) {
    	
        try {
            AppGameContainer app = new AppGameContainer( getInstance() );
            app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            app.setShowFPS(true);
            app.setAlwaysRender(true);
            app.setUpdateOnlyWhenVisible(false);
            app.setTargetFrameRate(MAX_FPS);
            app.setClearEachFrame(false);
            app.start();
        } catch (Throwable e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, e.getMessage());
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
}
