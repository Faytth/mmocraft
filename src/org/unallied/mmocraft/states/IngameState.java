package org.unallied.mmocraft.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.gui.GUIElement;

public class IngameState extends AbstractState {

    private int stateID = GameState.INGAME;
    
    // Contains all GUI elements (e.g. buttons / text) for this game state
    private List<GUIElement> controls = new ArrayList<GUIElement>();
    
    public IngameState() {
        super(null, null, null, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
    }
    
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(int button, int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseWheelMoved(int change) {
        // TODO Auto-generated method stub

    }

    @Override
    public void inputEnded() {
        // TODO Auto-generated method stub

    }

    @Override
    public void inputStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setInput(Input input) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerButtonPressed(int controller, int button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerDownPressed(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerDownReleased(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerLeftPressed(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerLeftReleased(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerRightPressed(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerRightReleased(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerUpPressed(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void controllerUpReleased(int controller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return stateID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        // TODO Auto-generated method stub
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
        MMOClient client = Game.getInstance().getClient();
        
        // The client knows what to render for all of the game stuff
        client.render(container, game, g);
        
        // Now render the GUI
        
         // Get maximum width / height of the game
        
/*                long gameWidth  = gl.getWorld().getWidth();
           long gameHeight = gl.getWorld().getHeight();
           
           // Get the terrain for easy reference and the player's location
           Terrain  t  = gl.getWorld().getTerrain();
           Location l  = gl.getCurrentPlayer().getLocation();*/
                           
           // Player is always at center, even at world's edge.

    }

    @Override
    /**
     * TODO:  Create a class for all GUI controls.  A game state should not
     *        handle GUI animation or events.
     */
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        
        Input input = container.getInput();
        Player player = Game.getInstance().getClient().getPlayer();
        boolean leftMove = input.isKeyDown(Input.KEY_LEFT);
        boolean rightMove = input.isKeyDown(Input.KEY_RIGHT);
        boolean upMove = input.isKeyDown(Input.KEY_UP);
        boolean downMove = input.isKeyDown(Input.KEY_DOWN);
        boolean idle = true; // determines whether player is moving

        if (player != null) {
            // Perform gravity checks
            player.accelerateDown(delta, ClientConstants.FALL_ACCELERATION, 
                    ClientConstants.FALL_TERMINAL_VELOCITY);
            
            // Perform shielding
            player.shieldUpdate(input.isKeyDown(Input.KEY_B));
            
            // Perform movement
            if (leftMove && !rightMove) {
                player.tryMoveLeft(delta);
                idle = false;
            }
            if (rightMove && !leftMove) {
                player.tryMoveRight(delta);
                idle = false;
            }
            if (upMove && !downMove) {
                player.tryMoveUp(delta);
                idle = false;
            }
            if (downMove && !upMove) {
                player.tryMoveDown(delta);
                idle = false;
            }
            
            // perform attacks
            player.attackUpdate(input.isKeyDown(Input.KEY_LSHIFT));
            
            if (idle) {
                player.idle();
            }
            player.update(delta);

        }
        
/*        if (delta > 20) {
            System.out.println(delta);
        }*/
        
        // Iterate over all GUI controls and inform them of input
        for( GUIElement control : controls ) {
            control.update(container);
        }
    }

    @Override
    public void update(GameContainer container) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingTab() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void renderImage() {
        // TODO Auto-generated method stub
        
    }

}
