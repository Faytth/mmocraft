package org.unallied.mmocraft.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.frame.RegisterFrame;
import org.unallied.mmocraft.net.PacketCreator;

public class RegisterState extends AbstractState {

    private int stateID = GameState.REGISTER;

    private RegisterFrame registerFrame = null;
    
    public RegisterState() {
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
        if( this.elements.size() == 0 ) {
            registerFrame = new RegisterFrame(this, new EventIntf(){

                @Override
                public void callback(Event event) {
                    switch( event.getId() ) {
                    // If the user clicked the "Back" button and should return to the Login state
                    case BACK_CLICKED:
                        Game.getInstance().enterState(GameState.LOGIN);
                        break;
                    case REGISTER_CLICKED:
                        // If the user didn't mess up on their password and such
                        if (registerFrame.isValid()) {
                            Game.getInstance().getClient().announce(PacketCreator.getRegister(
                                    registerFrame.getUsername(), registerFrame.getPassword(), registerFrame.getEmail()));
                        }
                        break;
                    }
                }
                
            }, container, Game.SCREEN_WIDTH/3
                    , Game.SCREEN_HEIGHT/3 + 60, -1, -1);
            // Controls
            this.elements.add(registerFrame);
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
            if (registerFrame != null) {
                registerFrame.destroy();
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
            image.draw(getAbsoluteWidth(), getAbsoluteHeight());
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
    public void renderImage(Image image) {
        // Set background
        image = ImageHandler.getInstance().getImage(ImageID.LOGIN_SCREEN.toString());
    }

	@Override
	protected boolean isAcceptingFocus() {
		return false;
	}
}
