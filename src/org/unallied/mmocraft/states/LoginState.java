package org.unallied.mmocraft.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.frame.LoginFrame;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.PacketSender;
import org.unallied.mmocraft.net.sessions.LoginSession;

public class LoginState extends AbstractState {

    private int stateID = GameState.LOGIN;
    
    private LoginFrame loginFrame = null;
    
    public LoginState() {
        super(null, null, null, 0, 0, Game.getInstance().getWidth(), Game.getInstance().getHeight());
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
                        if (loginFrame.getUsername().length() > 0 && loginFrame.getPassword().length() > 0) {
                            Game.getInstance().getClient().announce(
                                    PacketCreator.getLogon(loginSession.getUsername()));
                        }
                        break;
                    case REGISTER_CLICKED:
                        Game.getInstance().enterState(GameState.REGISTER);
                        break;
                    }
                }
                
            }, container, 0, 0, -1, -1);
            loginFrame.setX( (Game.getInstance().getWidth() - loginFrame.getWidth()) / 2);
            loginFrame.setY( (Game.getInstance().getHeight() - loginFrame.getHeight()) / 2);
            
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
        // Initialize the packet sending thread
        (new Thread(new PacketSender())).start();
        SpriteHandler.getInstance(); // init
        if (container != null) {
            container.setVerbose(false);
            container.setShowFPS(false);
            container.setAlwaysRender(true);
            container.setUpdateOnlyWhenVisible(false);
            container.setTargetFrameRate(Game.MAX_FPS);
            container.setClearEachFrame(false);
        }
        
        /*
         * Initialize the Collision enum.  This MUST be done in the same thread.
         * If initialization is left until later, then it's possible for
         * Collision to be initialized in another thread, which will cause all
         * Collisions to fail until the game is restarted.
         */
        Collision c = Collision.SWORD_BACK_AIR;
        Collision d = c;
        c = d;
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
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
        Image image = ImageHandler.getInstance().getImage(ImageID.LOGIN_SCREEN.toString());
        if( image != null) {
            // Tile the login state across the game
            for (int i = getAbsoluteWidth(); i < container.getWidth(); i += image.getWidth()) {
                for (int j = getAbsoluteHeight(); j < container.getHeight(); j += image.getHeight()) {
                    image.draw(i, j);
                }
            }
        }
        
        
        if (elements != null) {
            // Iterate over all GUI controls and inform them of input
            for( GUIElement element : elements ) {
                element.render(container, game, g);
            }
            
            for( GUIElement element : elements ) {
                element.renderToolTip(container, game, g);
            }
        }
    }
    
    @Override
    public void renderImage(Graphics g) {
    }

	@Override
	public boolean isAcceptingFocus() {
		return false;
	}
}
