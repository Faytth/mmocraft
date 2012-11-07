package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.chat.ChatCommand;
import org.unallied.mmocraft.chat.ChatMessage;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.MessageType;
import org.unallied.mmocraft.gui.control.Button;
import org.unallied.mmocraft.gui.control.TextCtrl;
import org.unallied.mmocraft.net.handlers.ChatMessageHandler;
import org.unallied.mmocraft.tools.Authenticator;

public class ChatFrame extends Frame {
	
	/** The color of "say" messages. */
	private static final Color SAY_COLOR   = new Color(255, 255, 255);
	
	/** The color of "world" messages. */
	private static final Color WORLD_COLOR = new Color(227, 115, 40);
	
	/** The color of "party" messages. */
	private static final Color PARTY_COLOR = new Color(5, 174, 240);
	
	/** The color of "guild" messages. */
	private static final Color GUILD_COLOR = new Color(5, 210, 51);
	
	/** The color of "system" messages. */
	private static final Color SYSTEM_COLOR = new Color(231, 218, 28);
	
	private static final String MESSAGE_FONT = FontID.STATIC_TEXT_MEDIUM_SMALL.toString();
	private static final String TITLE_FONT = FontID.STATIC_TEXT_MEDIUM_SMALL_BOLD.toString();

	/**
	 * The distance from the top of the chat frame that messageTextCtrl is positioned.
	 * Making this number larger will increase number of messages that can be displayed.
	 */
	private int chatHeight = 120;
	
	/** Used to determine if the scroll bar is currently being dragged. */
	private boolean scrollBarIsDragging = false;
	
	/** Determines the type of message that the player is writing. */
	private MessageType messageType;
	
	/** Stores the message that the player is typing. */
	private TextCtrl messageTextCtrl;
		
	/** The width in pixels of the scroll bar. */
	private int scrollBarWidth = 5;
	
	/**
	 * Index from the bottom of the chat frame.  In a way, this is a "reverse" index.
	 * If a new message is added and this index != 0, then the index needs to be
	 * incremented by the number of messages added.  This index should never go above
	 * <code>receivedMessages.size()</code>.
	 */
	private int lineIndex = 0;

    /**
     * When clicked, this button will expand to offer multiple types / colors<br />
     * of chat.  For example:<br />
     * <orange>World</orange><br />
     * <white>Say</white><br />
     * <green>Guild</green><br />
     * <blue>Party</blue>
     */
    private Button chatTypeButton;
    
    private Button worldButton;
    private Button sayButton;
    /** The width of the worldButton, sayButton, etc. */
    private static final int BUTTON_WIDTH = 60;
    
    private List<ChatMessage> receivedMessages = new ArrayList<ChatMessage>();
    private List<Button> changeChatTypeButtons = new ArrayList<Button>();
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public ChatFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

        int buttonY = chatHeight;
        chatTypeButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                for (Button button : changeChatTypeButtons) {
                    button.show(!button.isShown());
                }
            }
        }, container, "Say", 0, buttonY, BUTTON_WIDTH, -1, 0);
        chatTypeButton.setColor(SAY_COLOR);
        
        worldButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                for (Button button : changeChatTypeButtons) {
                    button.hide();
                    setType(MessageType.WORLD);
                }
            }
        }, container, "World", 0, buttonY -= 21, BUTTON_WIDTH, -1, 0);
        worldButton.setColor(WORLD_COLOR);
        
        sayButton = new Button(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                for (Button button : changeChatTypeButtons) {
                    button.hide();
                    setType(MessageType.SAY);
                }
            }
        }, container, "Say", 0, buttonY -= 21, BUTTON_WIDTH, -1, 0);
        sayButton.setColor(SAY_COLOR);
        
        // Add all of the chat type buttons to the changeChatTypeButtons list.
        changeChatTypeButtons.add(worldButton);
        changeChatTypeButtons.add(sayButton);
        
        // Hide all of the buttons for changing the chat type.
        for (Button button : changeChatTypeButtons) {
            button.hide();
        }
        
        messageTextCtrl = new TextCtrl(this, new EventIntf() {
            @Override
            public void callback(Event event) {
                switch (event.getId()) {
                case TEXT_ENTER:
                    if (isActive()) {
                        GUIElement element = event.getElement().getParent();
                        element.callback(new Event(element, EventType.SEND_CHAT_MESSAGE));
                    }
                    break;
                }
            }
        }, container, "", chatTypeButton.getWidth(), chatHeight, 370, 21, 0);
        messageTextCtrl.setMaxLength(Authenticator.MAX_MESSAGE_LENGTH);
        messageTextCtrl.setFontKey(FontID.STATIC_TEXT_MEDIUM_SMALL.toString());

        elements.add(chatTypeButton);
        elements.add(worldButton);
        elements.add(sayButton);
        elements.add(messageTextCtrl);
        
        setType(MessageType.SAY);
        this.width  = messageTextCtrl.getX() + messageTextCtrl.getWidth();
        this.height = chatHeight + messageTextCtrl.getHeight();
        if (this.height > container.getHeight() / 4) {
            chatHeight = container.getHeight() / 4 - messageTextCtrl.getHeight();
            this.height = chatHeight + messageTextCtrl.getHeight();
        }
        messageTextCtrl.setY(chatHeight);
        buttonY = chatHeight;
        chatTypeButton.setY(buttonY);
        worldButton.setY(buttonY -= 21);
        sayButton.setY(buttonY -= 21);
    }
    
    @Override
    public void update(GameContainer container) {

    	// Poll the chat handler for new messages
    	ChatMessage receivedMessage;
    	while ((receivedMessage = ChatMessageHandler.getNextMessage()) != null) {
    		addMessage(receivedMessage);
    	}
    	
        // Iterate over all GUI controls and inform them of input
        for( GUIElement element : elements ) {
            element.update(container);
        }
        
        /*
         * We now need to check to see if our messageTextCtrl has shortcuts
         * for the message type.  If it does, we need to adjust our type
         * accordingly.
         */
        String message = messageTextCtrl.getLabel();
        int index = message.indexOf(' ');
        if (index > 0) {
            message = message.substring(0, index);
            switch (ChatCommand.getCommand(message)) {
            case SELECT_SAY:
                setType(MessageType.SAY);
                messageTextCtrl.setLabel("");
                break;
            case SELECT_WORLD:
                setType(MessageType.WORLD);
                messageTextCtrl.setLabel("");
                break;
            }
        }
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    @Override
    public void renderImage(Graphics g) {
    	try {
	    	int lineHeight = FontHandler.getInstance().getFont(MESSAGE_FONT).getLineHeight();
	    	int lineY = this.chatHeight - lineHeight;
	    	int messageCount = receivedMessages.size();
	    	for (int i = this.receivedMessages.size()-1-lineIndex; i >= 0 && lineY >= 0 && i < messageCount; --i, lineY -= lineHeight) {
	    		ChatMessage message = receivedMessages.get(i);
	    		FontHandler.getInstance().draw(TITLE_FONT, message.getAuthor(), scrollBarWidth + 1, lineY, getTypeColor(message.getType()), width - scrollBarWidth - 1, height, false);
	    		int widthOffset = FontHandler.getInstance().getFont(TITLE_FONT.toString()).getWidth(message.getAuthor());
	    		FontHandler.getInstance().draw(MESSAGE_FONT, message.getBody(), scrollBarWidth + 1 + widthOffset, lineY, getTypeColor(message.getType()), width - scrollBarWidth - 1 - widthOffset, height, false);
	    	}
    	} catch (Throwable t) {
    	}
    }

    /**
     * Returns the maximum number of displayable lines.
     * @return lineCount
     */
    public int getMaxLines() {
        return chatHeight / FontHandler.getInstance().getFont(MESSAGE_FONT).getLineHeight();
    }
    
    public void renderScrollBar(Graphics g) {
        if (receivedMessages.size() > 1) {
            int offX = getAbsoluteWidth();
            int offY = getAbsoluteHeight();
            int scrollBarLength = (int)((1.0 * 1 / receivedMessages.size()) * (chatHeight));
            scrollBarLength = scrollBarLength < 20 ? 20 : scrollBarLength; // minimum length
            int scrollYOffset = chatHeight - scrollBarLength - ((int)(1.0 * lineIndex / (receivedMessages.size()-1) * (chatHeight - scrollBarLength)));
            scrollYOffset = (scrollYOffset + scrollBarLength) > height ? (height - scrollBarLength) : scrollYOffset;
            GradientFill scrollFill = new GradientFill(0, 0, new Color(255, 250, 250),
                    0, scrollBarLength/4, new Color(220, 220, 220), true);
            g.fill(new Rectangle(offX + 1, scrollYOffset + offY, 
                    4, scrollBarLength/2), scrollFill);
            g.fill(new Rectangle(offX + 1, scrollYOffset + offY + scrollBarLength/2, 
                    4, scrollBarLength-scrollBarLength/2), scrollFill.getInvertedCopy());
            g.setColor(new Color(0, 78, 100));
            g.drawRect(offX + 1, scrollYOffset + offY, 4, scrollBarLength);
            g.setColor(new Color(255, 255, 255, 255));
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
        if (!hidden) {
            if (width > 0 && height > 0) {
                if (isActive()) { // Show background
                    g.fill(new Rectangle(getAbsoluteWidth(), getAbsoluteHeight(), width, chatHeight), 
                            new GradientFill(0, 0, new Color(0, 0, 0, 150),
                                    0, chatHeight/2, new Color(0, 0, 0, 150)));
                } else {
                    g.fill(new Rectangle(getAbsoluteWidth(), getAbsoluteHeight(), width, chatHeight),
                            new GradientFill(0, 0, new Color(0, 0, 0, 100),
                                    0, chatHeight/2, new Color(0, 0, 0, 100)));
                    messageTextCtrl.setLabel("");
                }
                g.translate(getAbsoluteWidth(), getAbsoluteHeight());
                renderImage(g);
                g.flush();
                g.translate(-getAbsoluteWidth(), -getAbsoluteHeight());
                
                // Render scrollbar
                renderScrollBar(g);
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
        // Render border
        if (isActive()) {
            g.setColor(new Color(0, 128, 210));
        } else {
            g.setColor(new Color(0, 0, 0));
        }
        g.drawRect(getAbsoluteWidth(), getAbsoluteHeight(), width, height);
        g.setColor(new Color(255, 255, 255));
    }
    
    /**
     * Retrieves the color of the given type
     * @param type the type to get the color of
     * @return typeColor
     */
    private Color getTypeColor(MessageType type) {
    	Color result = null;
    	switch (type) {
    	case WORLD:
    		result = WORLD_COLOR;
    		break;
    	case PARTY:
    	    result = PARTY_COLOR;
    	    break;
    	case GUILD:
    	    result = GUILD_COLOR;
    	    break;
    	case SYSTEM:
    	    result = SYSTEM_COLOR;
    	    break;
    	case SAY:
    	default:
    		result = SAY_COLOR;
    		break;
    	}
    	
		return result;
	}

	/**
     * Returns the message that the user is attempting to send.
     * @return message
     */
    public ChatMessage getMessage() {
        return new ChatMessage(messageType, messageTextCtrl.getLabel());
    }

    /**
     * Sets the message to the given type / body.  This changes the text box
     * that the player types messages into.
     * @param message the new message
     */
    public void setMessage(ChatMessage message) {
    	if (message != null) {
    		setType(message.getType());
    		messageTextCtrl.setLabel(message.getBody());
    	}
    }
    
    /**
     * Sets the message type and also changes the color of messageTextCtrl
     * accordingly.
     * @param type the new message type
     */
    public void setType(MessageType type) {
    	this.messageType = type;
    	messageTextCtrl.setColor(getTypeColor(type));
    	chatTypeButton.setLabel(type.toString());
    	chatTypeButton.setColor(getTypeColor(type));
    }
    
    /**
     * Adds a new message to the chat frame.  These messages appear
     * above the <code>messageTextCtrl</code>.
     * @param message
     */
    public void addMessage(ChatMessage message) {
        Font titleFont = FontHandler.getInstance().getFont(TITLE_FONT);
    	Font font = FontHandler.getInstance().getFont(MESSAGE_FONT);
    	String[] words = message.getBody().split(" ");
    	String title = "[" + message.getAuthor() + "]: ";
    	String line = "";
    	
    	/*
    	 *  Go through each character and separate the message into chunks.
    	 *  This is done so that the message can be read in the chat frame
    	 *  without the message extending past the end of the frame.
    	 */
    	for (String word : words) {
    		if (titleFont.getWidth(title) + font.getWidth(line + word) < width - 5) { // The 5 here is for a border
    			line += word + " ";
    		} else if (line.length() == 0) {
       			/*
    			 *  The word was too long for an entire line!
    			 *  We should split it and display it on multiple lines, but I'm
    			 *  lazy.  For now we'll just display what we can.
    			 *  TODO:  Display huge words on multiple lines.
    			 */
    			line += word;
    			receivedMessages.add(new ChatMessage(title, message.getType(), line));
    			lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
    			line = "";
    			title = "";
    		} else {
    			receivedMessages.add(new ChatMessage(title, message.getType(), line));
    			lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
    			line = word + " ";
    			title = "";
    		}
    	}
    	if (line.length() > 0) {
    		receivedMessages.add(new ChatMessage(title, message.getType(), line));
    		lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
    	}
    	needsRefresh = true;
    }
    
	@Override
	public boolean isAcceptingFocus() {
		return true;
	}

	/**
	 * Sets the chat frame to the active control.  This will let the player enter a message.
	 */
    public void activate() {
        GUIUtility.getInstance().setActiveElement(messageTextCtrl);
    }
    
    /**
     * Deactivates the chat frame if it's the active control.
     */
    public void deactivate() {
        if (GUIUtility.getInstance().getActiveElement() == messageTextCtrl) {
            GUIUtility.getInstance().setActiveElement(null);
        }
    }
    
    /**
     * Returns whether or not this frame is active (meaning that the user can
     * enter a chat message).
     * @return active
     */
    public boolean isActive() {
        return GUIUtility.getInstance().getActiveElement() == messageTextCtrl;
    }
    
    
    /**
     * Updates the scroll bar position by getting the index of where y should be.
     * This is used for determining index when the mouse is pressed or dragged on
     * the scroll bar.
     * @param y The absolute y value to update to.
     */
    public void updateScrollBarPosition(int y) {
        // Guard
        if (chatHeight <= 0 || receivedMessages.size() < 2) {
            return;
        }
        
        // "Wiggle room" 
        final float BORDER = 1f;
                
        // We need to see if our scroll bar was pressed
        if ((y >= this.y &&
            y <= this.y + chatHeight) || scrollBarIsDragging) {
            // Get the percentage from 0 to 1 of the index
            float offset = this.y + chatHeight - y;
            // Give the user some "wiggle room" for going to the top / bottom
            if (offset < BORDER) {
                offset = 0;
            } else if (offset > chatHeight - BORDER && offset < chatHeight) {
                offset = chatHeight;
            }
            
            double index = 1.0 * offset / (chatHeight - BORDER*2);
            int newIndex = (int)((receivedMessages.size()-1) * index);
            newIndex = newIndex > receivedMessages.size()-1 ? receivedMessages.size()-1 : newIndex;
            newIndex = newIndex < 0 ? 0 : newIndex;
            if (newIndex != lineIndex) {
                lineIndex = newIndex;
                needsRefresh = true;
            }
        }
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
        // Guard
        if (chatHeight <= 0) {
            return;
        }
        
        // We need to see if our scroll bar was pressed
        if (x >= this.x &&
            x <= this.x + scrollBarWidth + 1 &&
            y >= this.y &&
            y <= this.y + chatHeight) {
            updateScrollBarPosition(y);
            scrollBarIsDragging = true;
        } else {
            scrollBarIsDragging = false;
        }
    }
        
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (scrollBarIsDragging) {
            updateScrollBarPosition(newy);
        }
    }
    
    @Override
    public void mouseReleased(int button, int x, int y) {
        scrollBarIsDragging = false;
    }
    
    
    @Override
    public void mouseWheelMoved(int change) {
        Input input = Game.getInstance().getContainer().getInput();
        if (input != null && input.getMouseX() >= x && input.getMouseX() <= x+width &&
                input.getMouseY() >= y && input.getMouseY() <= y+height) {
            lineIndex += change > 0 ? 1 : -1; // This is not a mistake.
            lineIndex = lineIndex < 0 ? 0 : lineIndex;
            lineIndex = lineIndex >= receivedMessages.size() ? receivedMessages.size()-1 : lineIndex;
            needsRefresh = true;
        }
    }
}
