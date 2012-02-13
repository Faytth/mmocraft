package org.unallied.mmocraft;

/**
 * Contains game logic, such as the current game state
 * @author Ryokko
 *
 */
public class GameLogic {
    
    // The current state of the game (login, ingame, etc.)
    private int currentGameState = GameState.LOGIN;
    
    // Contains stats about the actual game world, such as its width / height
    private World gameWorld = null;
    
    
    public GameLogic() {
    }
    
    
    /**
     * Gets the game's current state
     * @return The current state of the game (e.g. login, ingame)
     */
    public int getState() {
        return currentGameState;
    }

    
    /**
     * Gets the game's world
     * @return The World object associated with the game
     */
    public World getWorld() {
        return gameWorld;
    }


    public Player getCurrentPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

}
