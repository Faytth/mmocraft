package org.unallied.mmocraft.states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.BlockType;
import org.unallied.mmocraft.BoundLocation;
import org.unallied.mmocraft.Controls;
import org.unallied.mmocraft.Player;
import org.unallied.mmocraft.blocks.Block;
import org.unallied.mmocraft.blocks.ItemID;
import org.unallied.mmocraft.chat.ChatCommand;
import org.unallied.mmocraft.chat.ChatMessage;
import org.unallied.mmocraft.client.Camera;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.client.GameState;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.constants.StringConstants;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIElement.Event;
import org.unallied.mmocraft.gui.GUIElement.EventIntf;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.MessageType;
import org.unallied.mmocraft.gui.frame.CharacterFrame;
import org.unallied.mmocraft.gui.frame.ChatFrame;
import org.unallied.mmocraft.gui.frame.InventoryFrame;
import org.unallied.mmocraft.gui.frame.LootFrame;
import org.unallied.mmocraft.gui.frame.MiniMapFrame;
import org.unallied.mmocraft.gui.frame.MiningFrame;
import org.unallied.mmocraft.gui.frame.ReviveFrame;
import org.unallied.mmocraft.gui.frame.StatusFrame;
import org.unallied.mmocraft.gui.frame.ToolbarFrame;
import org.unallied.mmocraft.net.Heartbeat;
import org.unallied.mmocraft.net.PacketCreator;
import org.unallied.mmocraft.net.handlers.PvPToggleResponseHandler;

public class IngameState extends AbstractState {

    private int stateID = GameState.INGAME;
    
    /**
     * The chat frame that the user can send and receive chat messages through.
     */
    private ChatFrame chatFrame = null;
    
    /** Shows received items with fade in/out */
    private LootFrame itemsReceivedFrame = null;
    
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
    
    /**
     * A frame that pops up when the user is dead, containing a button to revive the
     * player.  This is not implemented as a messagebox, but rather as a frame.  The
     * reason for this is that it should ALWAYS be up when the user is dead with
     * absolutely no chance of just "not appearing" or of going away when clicked.
     */
    private ReviveFrame reviveFrame = null;
    
    /**
     * The mining frame which shows which block the player is currently placing.
     * It also offers a toggle between mining mode and normal mode.
     */
    private MiningFrame miningFrame = null;
    
    /** True if we're in "mining mode," else false. */
    private boolean inMiningMode = true;
    
    /** True if we're placing blocks, false if we're destroying them. */
    private boolean placingBlocks = false;
    
    /** True if the mouse is currently down. */
    private boolean mouseDown = false;
    
    public IngameState() {
        super(null, null, null, 0, 0, Game.getInstance().getWidth(), Game.getInstance().getHeight());
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        for (GUIElement element : orderedFrames.getFramesList()) {
            if (element.mouseMoved(oldx, oldy, newx, newy)) {
                return;
            }
        }
        
        try {
            Game.getInstance().getClient().getPlayer().setMiningLocation(
                    inMiningMode ? getBlockLocation(newx, newy) : null);
        } catch (NullPointerException e) {
            // Do nothing
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Sets the status of mining.  This tells the server whether we are
     * currently mining a block or not.
     * @param isMining True if we're mining, else false.
     * @param location The block location that the player's mouse is at.
     */
    public void setMining(boolean isMining, BoundLocation location) {
        Player player = Game.getInstance().getClient().getPlayer();
        if (isMining) {
            player.startMining(location);
        }
    }
    
    /**
     * Converts from screen coordinates to block coordinates.
     * @param x The screen x position.
     * @param y The screen y position.
     * @return blockLocation
     */
    public BoundLocation getBlockLocation(int x, int y) {
        BoundLocation result = null;
        
        try {
            Camera camera = Game.getInstance().getClient().getCamera();
            result = new BoundLocation(camera.getLocation());
            result.moveRight(x);
            result.moveDown(y);
        } catch (NullPointerException e) {
            // Don't care...
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    private void blockDragged(int newx, int newy) {
        try {
            BoundLocation miningLocation = getBlockLocation(newx, newy);
            Player player = Game.getInstance().getClient().getPlayer();
            BoundLocation playerLocation = player.getLocation();
            
            // If a different location
            if (playerLocation.getBlockDeltaX(miningLocation) != 0 ||
                    playerLocation.getBlockDeltaY(miningLocation) != 0) {
                placeBlock(miningLocation);
            }
            player.setMiningLocation(miningLocation);
        } catch (NullPointerException e) {
            // Don't care
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        for (GUIElement element : orderedFrames.getFramesList()) {
            if (element.mouseDragged(oldx, oldy, newx, newy)) {
                return;
            }
        }
        blockDragged(newx, newy);
    }
    
    private boolean placeBlock(BoundLocation miningLocation) {
        boolean result = false;
        
        Block block = Game.getInstance().getClient().getTerrainSession().getBlock(miningLocation);
        result = block.getType() == BlockType.AIR;
        
        if (result) {
            // Tell the server
            try {
                Game.getInstance().getClient().announce(PacketCreator
                        .getPlaceBlock(ItemID.DIRT_BLOCK, miningLocation));
            } catch (NullPointerException e) {
                // Don't care
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        
        return result;
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
        for (GUIElement element : orderedFrames.getFramesList()) {
            if (element.mousePressed(button, x, y)) {
                return;
            }
        }
        mouseDown = true;
        if (inMiningMode) { // Start mining
            try {
                BoundLocation miningLocation = getBlockLocation(x, y);
                placingBlocks = placeBlock(miningLocation);
                Game.getInstance().getClient().getPlayer().setMiningLocation(miningLocation);
                if (!placingBlocks) { // Place blocks
                    setMining(true, getBlockLocation(x, y));
                }
            } catch (NullPointerException e) {
                // Don't care
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        mouseDown = false;
        for (GUIElement element : orderedFrames.getFramesList()) {
            if (element.mouseReleased(button, x, y)) {
                return;
            }
        }
        setMining(false, getBlockLocation(x, y));
    }
    
    @Override
    public void keyPressed(int key, char c) {
        GameContainer container = Game.getInstance().getContainer();
        Input input = container.getInput();
    	
        // Be careful about using this function, because it occurs BEFORE child elements
    	Controls controls = Game.getInstance().getControls();
    	
    	// Check for fullscreen.
    	if (key == Input.KEY_ENTER && (input.isKeyDown(Input.KEY_LALT) || input.isKeyDown(Input.KEY_RALT))) {
    	    toggleFullscreen(container);
    	    resetGUI(container);
    	    return;
    	} else if (key == Input.KEY_ENTER || key == Input.KEY_RETURN) {
            if (GUIUtility.getInstance().getActiveElement() == null) {
                if (chatFrame != null) {
                    chatFrame.activate();
                    return;
                }
            }
    	}
    	
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
        	// reviveFrame shouldn't be deactivated on Escape.
        	return;
        }
        
        if (key == Input.KEY_SLASH) {
            if (GUIUtility.getInstance().getActiveElement() == null) {
                if (chatFrame != null) {
                    chatFrame.activate();
                }
            }
        }
        
        GUIElement activeElement = GUIUtility.getInstance().getActiveElement();
        
        try {
	        switch (controls.getKeyType(key)) {
		        case TOGGLE_INVENTORY:
		        	if (inventoryFrame != null && 
		        	        (activeElement == null || activeElement == inventoryFrame)) {
		        	    inventoryFrame.show(!inventoryFrame.isShown());
		        	    return;
		        	}
		        	break;
		        case TOGGLE_CHARACTER_INFO:
		            if (characterFrame != null && 
		                    (activeElement == null || activeElement == characterFrame)) {
		                characterFrame.show(!characterFrame.isShown());
		                return;
		            }
		            break;
		        }
        } catch (NullPointerException e) {
        	// Don't do anything, because all it means is the key wasn't found
        }
        
        super.keyPressed(key, c);
    }

    /**
     * Resets the GUI elements of this state by destroying and re-initializing them.
     * @param container The container used for resizing the GUI and creating the GUI elements.
     */
    public void resetGUI(GameContainer container) {
        resize(container);
//        container.getGraphics().setDimensions(container.getWidth(), container.getHeight());
        // Set GUI elements
        if( orderedFrames.getFrameCount() > 0 ) {
            // Controls
            orderedFrames.destroy();
            orderedFrames.removeAllFrames();
        }
        toolbarFrame = new ToolbarFrame(orderedFrames, new EventIntf() {
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
        
        inventoryFrame = new InventoryFrame(orderedFrames, new EventIntf() {
            @Override
            public void callback(Event event) {
                
            }
        }, container, 0, 0, -1, -1);
        inventoryFrame.setX(Game.getInstance().getWidth() - inventoryFrame.getWidth() - 10);
        inventoryFrame.setY(toolbarFrame.getY() - inventoryFrame.getHeight() - 10);
        inventoryFrame.hide();
        
        chatFrame = new ChatFrame(orderedFrames, new EventIntf() {

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
        
        miniMapFrame = new MiniMapFrame(orderedFrames, null, container, 
                Game.getInstance().getWidth() - 180, 10, -1, -1);

        statusFrame = new StatusFrame(orderedFrames, null, container, 5, 5, -1, 1);
        
        characterFrame = new CharacterFrame(orderedFrames, null, container, 5, chatFrame.getY() - 5, -1, -1);
        characterFrame.setY(chatFrame.getY() - characterFrame.getHeight() - 5);
        characterFrame.hide();
        
        reviveFrame = new ReviveFrame(orderedFrames, null, container, 5, 0, -1, -1);
        reviveFrame.setX(Game.getInstance().getWidth() / 2 - reviveFrame.getWidth() / 2);
        reviveFrame.setY(Game.getInstance().getHeight() / 2 - reviveFrame.getHeight() / 2);
        
        itemsReceivedFrame = new LootFrame(orderedFrames, null, container, 
        		Game.getInstance().getWidth() - 300 - 10, toolbarFrame.getAbsoluteY() - 100 - 10, 300, 100);
        
        miningFrame = new MiningFrame(orderedFrames, null, container, 0, 0, -1, -1);
        miningFrame.setX(toolbarFrame.getX() - miningFrame.getWidth() - 5);
        miningFrame.setY(toolbarFrame.getY());
        
        // Controls.  Add these in the order you would like to see them appear.
        orderedFrames.addFrame(reviveFrame);
        
        orderedFrames.addFrame(inventoryFrame);
        orderedFrames.addFrame(characterFrame);
        
        orderedFrames.addFrame(chatFrame);
        orderedFrames.addFrame(miniMapFrame);
        orderedFrames.addFrame(statusFrame);
        orderedFrames.addFrame(toolbarFrame);
        orderedFrames.addFrame(miningFrame);
        orderedFrames.addFrame(itemsReceivedFrame);        

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
        try {
            String author = "";
            MessageType type = MessageType.SYSTEM;
            switch (ChatCommand.getCommand(message)) {
            case DIAGNOSTICS:
                GameContainer container = Game.getInstance().getContainer();
                author = "Diagnostics";
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format("Window size: (%d, %d)", container.getWidth(), container.getHeight())));
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format("FPS: %d", container.getFPS())));
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format("Target FPS: %d", container.getTargetFrameRate())));
                chatFrame.addMessage(new ChatMessage(author, type, "Fullscreen: " + container.isFullscreen()));
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format("Received bytes: %d (%.2f B/s)", Game.getInstance().getClient().getSession().getReadBytes(),
                                Game.getInstance().getClient().getSession().getReadBytes() / ((System.currentTimeMillis() - Game.startTime) / 1000.0f))));
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format("Sent bytes: %d (%.2f B/s)", Game.getInstance().getClient().getSession().getWrittenBytes(),
                                Game.getInstance().getClient().getSession().getWrittenBytes() / ((System.currentTimeMillis() - Game.startTime) / 1000.0f))));
                chatFrame.addMessage(new ChatMessage(author, type, "Latency: " + Heartbeat.getInstance().getAverageLatency() + " ms"));
                break;
            case HELP:
                author = "Help";
                chatFrame.addMessage(new ChatMessage(author, type, StringConstants.HELP_INTRODUCTION_MESSAGE));
                for (ChatCommand command : ChatCommand.values()) {
                    if (command != ChatCommand.NONE) {
                        chatFrame.addMessage(new ChatMessage(author, type, command.toString()));
                    }
                }
                break;
            case PVP:
                author = "PvP";
                boolean pvpEnabled = !PvPToggleResponseHandler.isPvPToggled();
                chatFrame.addMessage(new ChatMessage(author, type, 
                        String.format(StringConstants.PVP_TOGGLE, pvpEnabled ? StringConstants.ENABLED : StringConstants.DISABLED)));
                Game.getInstance().getClient().announce(PacketCreator.getPvPToggle(pvpEnabled));
                break;
            case RELOAD_GUI:
                Game game = Game.getInstance();
                org.newdawn.slick.state.GameState state = game.getCurrentState();
                if (state instanceof IngameState) {
                    ((IngameState)state).resetGUI(game.getContainer());
                }
                break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        resetGUI(container);
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
        if( orderedFrames.getFrameCount() > 0 ) {
            if (chatFrame != null) {
                chatFrame.destroy();
            }
            // Controls
            orderedFrames.removeAllFrames();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
        MMOClient client = Game.getInstance().getClient();
        
        // The client knows what to render for all of the game stuff
        client.render(container, game, g);
        
        // Now render the GUI
        orderedFrames.render(container, game, g);
    }

    @Override
    /**
     * TODO:  Create a class for all GUI controls.  A game state should not
     *        handle GUI animation or events.
     */
    public void update(GameContainer container, StateBasedGame game, int delta) {
        try {
            if (container.getWidth() != Display.getWidth() || 
                    container.getHeight() != Display.getHeight() ||
                    container.getWidth() != container.getGraphics().getWidth() ||
                    container.getHeight() != container.getGraphics().getHeight()) {
                resetGUI(container);
            }
            Game.getInstance().getClient().update(container, game, delta);
            
            // Show / Hide the revive frame depending on if the player is alive or dead.
            try {
                reviveFrame.show(!Game.getInstance().getClient().getPlayer().isAlive());
            } catch (NullPointerException e) { // Don't care
            }
            
            // Update our mining position
            Input input = container.getInput();
            if (mouseDown) {
                blockDragged(input.getMouseX(), input.getMouseY());
            } else {
                try {
                    Game.getInstance().getClient().getPlayer().setMiningLocation(
                            getBlockLocation(input.getMouseX(), input.getMouseY()));
                } catch (NullPointerException e) {
                    // Don't care
                }
            }
            
            // Iterate over all GUI controls and inform them of input
            orderedFrames.update(container);
        } catch (Throwable t) {
            t.printStackTrace(); // We had a pretty major error, but let's try to keep going.
        }
    }

}
