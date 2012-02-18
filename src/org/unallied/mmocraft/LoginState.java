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
import org.unallied.mmocraft.gui.frame.LoginFrame;
import org.unallied.mmocraft.packets.handlers.LoginHandler;

public class LoginState extends GUIElement implements GameState {

    private int stateID = -1;
    
    // Contains all GUI elements (e.g. buttons / text) for this game state
    private List<GUIElement> elements = new ArrayList<GUIElement>();
    private LoginFrame loginFrame = null;
    
    public LoginState(int stateID) {
        super(null, null, null, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        this.stateID = stateID;
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
        
        // init is called twice
        if( this.elements.size() == 0 ) {
            loginFrame = new LoginFrame(this, new EventIntf(){

                @Override
                public void callback(Event event) {
                    switch( event.getId() ) {
                    case LOGIN_CLICKED:
                        // If we logged in, go to ingame state
                        if( new LoginHandler(loginFrame.getUsername(), loginFrame.getPassword()).login() ) {
                            Game.getInstance().enterState(org.unallied.mmocraft.GameState.INGAME);
                        }
                        break;
                    case REGISTER_CLICKED:
                        System.out.println("Register");
                        break;
                    }
                }
                
            }, container, Game.SCREEN_WIDTH/3
                    , Game.SCREEN_HEIGHT/3 + 60, -1, -1);
            // Controls
            this.elements.add(loginFrame);
        }
        container.getInput().enableKeyRepeat();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game)
            throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        // Display login screen
        ImageHandler.getInstance().draw(ImageID.LOGIN_SCREEN.toString(), 0, 0);

        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.render(container, game, g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        
        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
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
