package org.unallied.mmocraft;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.gui.GUIElement;

public class IngameState extends GUIElement implements GameState {

    private GameLogic gl = null;
    private int stateID = -1;
    
    // Contains all GUI elements (e.g. buttons / text) for this game state
    private List<GUIElement> controls = new ArrayList<GUIElement>();
    
    public IngameState(int stateID) {
        super(null, null, null, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        this.stateID = stateID;
        this.gl = new GameLogic();
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
        return false;
    }

    @Override
    public void setInput(Input input) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(int key, char c) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
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
