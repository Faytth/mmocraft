package org.unallied.mmocraft;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.net.PacketSocket;

public class Game extends StateBasedGame {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    private static final int MAX_FPS = 60;
    
    private MMOClient client = new MMOClient(); // The client associated with this game
    
    private Game() {
        super("MMOCraft");
        
        // Calling this causes our client's session to be set
        PacketSocket.getInstance().connect();
        
        this.addState(new LoginState(GameState.LOGIN));
        this.addState(new IngameState(GameState.INGAME));
        this.enterState(GameState.LOGIN);
    }
    
    private static class GameHolder {
        public static final Game instance = new Game();
    }
    	
    public static void main( String[] args ) {
    	AppGameContainer app;
    	
        try {
            app = new AppGameContainer( getInstance() );
            app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            app.setShowFPS(true);
            app.setTargetFrameRate(MAX_FPS);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
        
        // Cleanup
        PacketSocket.getInstance().close();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(GameState.LOGIN).init(gameContainer, this);
        this.getState(GameState.INGAME).init(gameContainer, this);
    }
    
    public static Game getInstance() {
        return GameHolder.instance;
    }
    
    /**
     * Returns the MMOClient associated with this game.  This is a class that
     * stores stuff about a player's connection / account.
     * @return
     */
    public MMOClient getClient() {
        return client;
    }
}
