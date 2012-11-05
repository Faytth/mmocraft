package org.unallied.mmocraft.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.chat.ChatCommand;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.gui.ChatMessage;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.frame.CharacterFrame;
import org.unallied.mmocraft.gui.frame.ChatFrame;
import org.unallied.mmocraft.gui.frame.InventoryFrame;
import org.unallied.mmocraft.gui.frame.MiniMapFrame;
import org.unallied.mmocraft.gui.frame.StatusFrame;
import org.unallied.mmocraft.gui.frame.ToolbarFrame;
import org.unallied.mmocraft.net.PacketCreator;

public class IngameState extends AbstractState {

    private int stateID = GameState.INGAME;
    
    /**
     * The chat frame that the user can send and receive chat messages through.
     */
    private ChatFrame chatFrame = null;
    
    /** The inventory frame that contains all of a player's items. */
    private InventoryFrame inventoryFrame = null;
    
    /** A frame containing the player's location information. */
    private MiniMapFrame miniMapFrame = null;
    
    /** The player's status, such as current / maximum health, experience, name. */
    private StatusFrame statusFrame = null;
    
    /** 
     * The toolbar frame.  Contains important buttons for navigating around the
     * GUI, such as a button to open / close the inventory.
     */
    private ToolbarFrame toolbarFrame = null;
    
    /**
     * A frame containing other frames inside of it that contain character information,
     * such as the player's equipment and skills.
     */
    private CharacterFrame characterFrame = null;
    
    public IngameState() {
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
        // Be careful about using this function, because it occurs BEFORE child elements
    	Controls controls = Game.getInstance().getControls();
    	
        // Deactivate GUI if needed.
        if (key == Input.KEY_ESCAPE) {
        	if (chatFrame != null && chatFrame.isActive()) {
        		chatFrame.deactivate();
        	}
        	if (inventoryFrame != null) {
        		inventoryFrame.hide();
        	}
        	if (characterFrame != null) {
        	    characterFrame.hide();
        	}
        }
        
        GUIElement activeElement = GUIUtility.getInstance().getActiveElement();
        
        try {
	        switch (controls.getKeyType(key)) {
		        case TOGGLE_INVENTORY:
		        	if (inventoryFrame != null && 
		        	        (activeElement == null || activeElement == inventoryFrame)) {
		        	    inventoryFrame.show(!inventoryFrame.isShown());
		        	}
		        	break;
		        case TOGGLE_CHARACTER_INFO:
		            if (characterFrame != null && 
		                    (activeElement == null || activeElement == characterFrame)) {
		                characterFrame.show(!characterFrame.isShown());
		            }
		            break;
		        }
        } catch (NullPointerException e) {
        	// Don't do anything, because all it means is the key wasn't found
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

    /**
     * Resets the GUI elements of this state by destroying and re-initializing them.
     */
    public void resetGUI(GameContainer container, StateBasedGame game) {
        // Set GUI elements
        if( this.elements.size() > 0 ) {
            // Controls
            this.destroy();
            this.elements.clear();
        }
        toolbarFrame = new ToolbarFrame(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case INVENTORY_CLICKED:
                    inventoryFrame.show(!inventoryFrame.isShown());
                    break;
                case CHARACTER_CLICKED:
                    characterFrame.show(!characterFrame.isShown());
                    break;
                }
            }
        }, container, 0, 0, -1, -1);
        toolbarFrame.setX(Game.getInstance().getWidth() - toolbarFrame.getWidth() - 10);
        toolbarFrame.setY(Game.getInstance().getHeight() - toolbarFrame.getHeight() - 10);
        
        inventoryFrame = new InventoryFrame(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                
            }
        }, container, 0, 0, -1, -1);
        inventoryFrame.setX(Game.getInstance().getWidth() - inventoryFrame.getWidth() - 10);
        inventoryFrame.setY(toolbarFrame.getY() - inventoryFrame.getHeight() - 10);
        inventoryFrame.hide();
        
        chatFrame = new ChatFrame(this, new EventIntf() {

            @Override
            public void callback(Event event) {
                switch( event.getId() ) {
                case SEND_CHAT_MESSAGE:
                    ChatMessage message = chatFrame.getMessage();
                    if (!message.getBody().isEmpty()) {
                        if (ChatCommand.isCommand(message.getBody())) {
                            handleChatCommand(message.getBody());
                        } else {
                            // Send the message to the server
                            Game.getInstance().getClient().announce(
                                    PacketCreator.getChatMessage(message));
                            // Clear the message from the chat frame
                            message.setBody("");
                            chatFrame.setMessage(message);
                        }
                    }
                    chatFrame.deactivate();
                    // Needed to stop any other enter / return events
                    Game.getInstance().getContainer().getInput().isKeyPressed(Input.KEY_ENTER);
                    Game.getInstance().getContainer().getInput().isKeyPressed(Input.KEY_RETURN);
                    break;
                }
            }
            
        }, container, 5, 0, -1, -1);
        chatFrame.setY(Game.getInstance().getHeight() - chatFrame.getHeight() - 5);
        
        miniMapFrame = new MiniMapFrame(this, null, container, 
                Game.getInstance().getWidth() - 180, 10, -1, -1);

        statusFrame = new StatusFrame(this, null, container, 5, 5, -1, 1);
        
        characterFrame = new CharacterFrame(this, null, container, 5, chatFrame.getY() - 5, -1, -1);
        characterFrame.setY(chatFrame.getY() - characterFrame.getHeight() - 5);
        characterFrame.hide();
        
        // Controls.  Add these in the order you would like to see them appear.
        this.elements.add(chatFrame);
        this.elements.add(miniMapFrame);
        this.elements.add(statusFrame);
        this.elements.add(toolbarFrame);
 
        this.elements.add(characterFrame);
        this.elements.add(inventoryFrame);

        // Start off with the game focused
        GUIUtility.getInstance().setActiveElement(null);
        container.getInput().isKeyPressed(Input.KEY_ENTER);
        container.getInput().isKeyPressed(Input.KEY_RETURN);
    }
    
    /**
     * Handles chat commands.  These commands cause the client to respond in
     * some way, such as reloading the GUI.
     * @param message
     */
    protected void handleChatCommand(String message) {
        switch (ChatCommand.getCommand(message)) {
        case HELP:
            
            break;
        case RELOAD_GUI:
            Game game = Game.getInstance();
            org.newdawn.slick.state.GameState state = game.getCurrentState();
            if (state instanceof IngameState) {
                ((IngameState)state).resetGUI(game.getContainer(), game);
            }
            break;
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        resetGUI(container, game);
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return stateID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
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
    public void update(GameContainer container, StateBasedGame game, int delta) {
        try {
            Input input = container.getInput();

            Game.getInstance().getClient().update(container, game, delta);
            
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
        } catch (Throwable t) {
            t.printStackTrace(); // We had a pretty major error, but let's try to keep going.
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
    public void renderImage(Graphics g) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public boolean isAcceptingFocus() {
		return false;
	}

}
