package org.unallied.mmocraft.states;

import java.awt.im.InputContext;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.ClientConstants;
import org.unallied.mmocraft.gui.ChatMessage;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.frame.ChatFrame;
import org.unallied.mmocraft.net.PacketCreator;

public class IngameState extends AbstractState {

    private int stateID = GameState.INGAME;
    
    /**
     * The chat frame that the user can send and receive chat messages through.
     */
    private ChatFrame chatFrame = null;
    
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
        // Be careful about using this function, because it occurs BEFORE child elements
        
        // Deactivate chat box.  Enter / return are implemented in a different area.
        if (key == Input.KEY_ESCAPE && chatFrame != null && chatFrame.isActive()) {
            chatFrame.deactivate();
        }
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
        InputContext context = InputContext.getInstance();
        System.out.println(context.getLocale().toString());
        // Set GUI elements
        if( this.elements.size() == 0 ) {
            chatFrame = new ChatFrame(this, new EventIntf(){

                @Override
                public void callback(Event event) {
                    switch( event.getId() ) {
                    case SEND_CHAT_MESSAGE:
                    	ChatMessage message = chatFrame.getMessage();
                    	if (!message.getBody().isEmpty()) {
                        	// Send the message to the server
                        	Game.getInstance().getClient().announce(
                        			PacketCreator.getChatMessage(message));
                        	// Clear the message from the chat frame
                        	message.setBody("");
                        	chatFrame.setMessage(message);
                    	}
                    	chatFrame.deactivate();
                    	// Needed to stop any other enter / return events
                    	Game.getInstance().getContainer().getInput().isKeyPressed(Input.KEY_ENTER);
                    	Game.getInstance().getContainer().getInput().isKeyPressed(Input.KEY_RETURN);
                        break;
                    }
                }
                
            }, container, 10, Game.SCREEN_HEIGHT-210 + 60, -1, -1);
            // Controls
            this.elements.add(chatFrame);
        }
        // Start off with the game focused
        GUIUtility.getInstance().setActiveElement(null);
        container.getInput().isKeyPressed(Input.KEY_ENTER);
        container.getInput().isKeyPressed(Input.KEY_RETURN);
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
        if( this.elements.size() > 0 ) {
            if (chatFrame != null) {
                chatFrame.destroy();
            }
            // Controls
            this.elements.clear();
        }
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
    /**
     * TODO:  Create a class for all GUI controls.  A game state should not
     *        handle GUI animation or events.
     */
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        
        Input input = container.getInput();
        Player player = Game.getInstance().getClient().getPlayer();
        boolean idle = true; // determines whether player is moving

        if (player != null) {
        	Controls controls = Game.getInstance().getControls();

            // Perform gravity checks
            player.accelerateDown(delta, ClientConstants.FALL_ACCELERATION, 
                    ClientConstants.FALL_TERMINAL_VELOCITY);
            
            // Perform shielding
            player.shieldUpdate(controls.isShielding(input));
            
            // These next checks are only if the main game is focused and not a GUI control
            if (GUIUtility.getInstance().getActiveElement() == null) {
                // Perform movement
                if (controls.isMovingLeft(input)) {
                	player.tryMoveLeft(delta);
                	idle = false;
                }
                if (controls.isMovingRight(input)) {
                    player.tryMoveRight(delta);
                    idle = false;
                }
                if (controls.isMovingUp(input)) {
                    player.tryMoveUp(delta);
                    idle = false;
                }
                if (controls.isMovingDown(input)) {
                    player.tryMoveDown(delta);
                    idle = false;
                }
                
                // perform attacks
                player.attackUpdate(controls.isBasicAttack(input));
            }
            
            if (idle) {
                player.idle();
            }
            player.update(delta);

        }
        
/*        if (delta > 20) {
            System.out.println(delta);
        }*/
        
        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements) {
            element.update(container);
        }
        
        // If the main game is focused and not a GUI element
        if (input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_RETURN)) {
            if (GUIUtility.getInstance().getActiveElement() == null) {
                if (chatFrame != null) {
                    chatFrame.activate();
                }
            }
        }
    }

    @Override
    public void update(GameContainer container) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }

    @Override
    public void renderImage(Image image) {
        // TODO Auto-generated method stub
        
    }

	@Override
	protected boolean isAcceptingFocus() {
		return false;
	}

}
