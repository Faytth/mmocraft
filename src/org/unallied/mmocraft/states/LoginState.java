package org.unallied.mmocraft.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.frame.LoginFrame;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.sessions.LoginSession;

public class LoginState extends AbstractState {

    private int stateID = GameState.LOGIN;
    
    private LoginFrame loginFrame = null;
    
    public LoginState() {
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
        // Remove all cached chunks.
        Game.getInstance().getClient().getTerrainSession().clear();
        // Enable anti-aliasing
        container.getGraphics().setAntiAlias(true);
        // Set background
        this.image = ImageHandler.getInstance().getImage(ImageID.LOGIN_SCREEN.toString());
        // Set GUI elements
        if( this.elements.size() == 0 ) {
            loginFrame = new LoginFrame(this, new EventIntf(){

                @Override
                public void callback(Event event) {
                    switch( event.getId() ) {
                    case LOGIN_CLICKED:
                        // Set user / pass for the loginSession
                        LoginSession loginSession = Game.getInstance().getClient().loginSession;
                        loginSession.setUsername(loginFrame.getUsername());
                        loginSession.setPassword(loginFrame.getPassword());
                        
                        // Initiate communication to the server for the login
                        Game.getInstance().getClient().announce(
                                PacketCreator.getLogon(loginSession.getUsername()));
                        try {
                            Thread.sleep(500); // This is needed to allow time to connect
                        } catch (InterruptedException e) {
                        }
                        break;
                    case REGISTER_CLICKED:
                        Game.getInstance().enterState(GameState.REGISTER);
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
    public int getID() {
        // TODO Auto-generated method stub
        return stateID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        SpriteHandler.getInstance(); // init
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game)
            throws SlickException {
        if( this.elements.size() > 0 ) {
            if (loginFrame != null) {
                loginFrame.destroy();
            }
            // Controls
            this.elements.clear();
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
