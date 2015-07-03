package org.unallied.mmocraft.states;

import org.lwjgl.opengl.Display;
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
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.gui.GUIElement.Event;
import org.unallied.mmocraft.gui.GUIElement.EventIntf;
import org.unallied.mmocraft.gui.frame.RegisterFrame;
import org.unallied.mmocraft.net.PacketCreator;

public class RegisterState extends AbstractState {

	private int stateID = GameState.REGISTER;

	private RegisterFrame registerFrame = null;

	public RegisterState() {
		super(null, null, null, 0, 0, Game.getInstance().getWidth(), Game.getInstance().getHeight());
	}

	public void keyPressed(int key, char c) {
	    GameContainer container = Game.getInstance().getContainer();
	    Input input = container.getInput();
	    
	    // Check for fullscreen.
        if (key == Input.KEY_ENTER && (input.isKeyDown(Input.KEY_LALT) || input.isKeyDown(Input.KEY_RALT))) {
            toggleFullscreen(container);
            resetGUI(container);
            return;
        }
        
        super.keyPressed(key, c);
	}
	
	public void resetGUI(GameContainer container) {
	    resize(container);
	    
	    // Clear the current frames
        if( orderedFrames.getFrameCount() > 0 ) {
            orderedFrames.destroy();
            orderedFrames.removeAllFrames();
        }
       
        registerFrame = new RegisterFrame(orderedFrames, new EventIntf(){

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

        }, container, 0, 0, -1, -1);
        registerFrame.setX( (Game.getInstance().getWidth() - registerFrame.getWidth()) / 2);
        registerFrame.setY( (Game.getInstance().getHeight()- registerFrame.getHeight()) / 2);

        // Controls
        orderedFrames.addFrame(registerFrame);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Remove all cached chunks.
		Game.getInstance().getClient().getTerrainSession().clear();
		resetGUI(container);
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
		if( orderedFrames.getFrameCount() > 0 ) {
			if (registerFrame != null) {
				registerFrame.destroy();
			}
			// Controls
			orderedFrames.removeAllFrames();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
        if (container.getWidth() != Display.getWidth() || 
                container.getHeight() != Display.getHeight() ||
                container.getWidth() != container.getGraphics().getWidth() ||
                container.getHeight() != container.getGraphics().getHeight()) {
            resetGUI(container);
        }
		orderedFrames.update(container);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		Image image = ImageHandler.getInstance().getImage(ImageID.LOGIN_SCREEN.toString());
		if( image != null) {
            long curTime = System.currentTimeMillis();
            int backgroundOffsetX = (int) ((curTime % (image.getWidth() * ClientConstants.BACKGROUND_SCROLL_RATE_X)) / ClientConstants.BACKGROUND_SCROLL_RATE_X);
            int backgroundOffsetY = (int) ((curTime % (image.getHeight() * ClientConstants.BACKGROUND_SCROLL_RATE_Y)) / ClientConstants.BACKGROUND_SCROLL_RATE_Y);
            
            // Tile the login state across the game
            final int imageWidth = image.getWidth();
            final int imageHeight = image.getHeight();
            image.startUse();
            for (int i = orderedFrames.getAbsoluteX() - backgroundOffsetX; i < container.getWidth(); i += imageWidth) {
                for (int j = orderedFrames.getAbsoluteY() - backgroundOffsetY; j < container.getHeight(); j += imageHeight) {
                    image.drawEmbedded(i, j, imageWidth, imageHeight);
                }
            }
            image.endUse();
		}
		
        Image title = ImageHandler.getInstance().getImage(ImageID.LOGIN_TITLE.toString());
        if (title != null) {
            title.draw(container.getWidth() / 2 - title.getWidth() / 2, container.getHeight() / 2 - title.getHeight() - 100);
        }

		orderedFrames.render(container, game, g);
	}
}
