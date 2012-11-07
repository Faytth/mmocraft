package org.unallied.mmocraft.states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Collision;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.ImageHandler;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.gui.GUIElement.Event;
import org.unallied.mmocraft.gui.GUIElement.EventIntf;
import org.unallied.mmocraft.gui.frame.LoginFrame;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.PacketSender;
import org.unallied.mmocraft.sessions.LoginSession;

public class LoginState extends AbstractState {

	private int stateID = GameState.LOGIN;

	private LoginFrame loginFrame = null;

	public LoginState() {
		super(null, null, null, 0, 0, Game.getInstance().getWidth(), Game.getInstance().getHeight());
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Remove all cached chunks.
		Game.getInstance().getClient().getTerrainSession().clear();
		// Enable anti-aliasing
		container.getGraphics().setAntiAlias(true);
		// Set GUI elements
		if( orderedFrames.getFrameCount() == 0 ) {
			loginFrame = new LoginFrame(orderedFrames, new EventIntf(){

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
			orderedFrames.addFrame(loginFrame);
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
		if(orderedFrames.getFrameCount() > 0 ) {
			if (loginFrame != null) {
				loginFrame.destroy();
			}
			
			// Controls
			orderedFrames.removeAllFrames();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		orderedFrames.update(container);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

		Image image = ImageHandler.getInstance().getImage(ImageID.LOGIN_SCREEN.toString());
		if( image != null) {
			// Tile the login state across the game
			for (int i = orderedFrames.getAbsoluteWidth(); i < container.getWidth(); i += image.getWidth()) {
				for (int j = orderedFrames.getAbsoluteHeight(); j < container.getHeight(); j += image.getHeight()) {
					image.draw(i, j);
				}
			}
		}

		orderedFrames.render(container, game, g);
	}
}
