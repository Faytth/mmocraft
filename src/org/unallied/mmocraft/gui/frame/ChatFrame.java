package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.ImageID;
import org.unallied.mmocraft.client.ImagePool;
import org.unallied.mmocraft.gui.ChatMessage;
import org.unallied.mmocraft.gui.EventType;
import org.unallied.mmocraft.gui.GUIElement;
import org.unallied.mmocraft.gui.GUIUtility;
import org.unallied.mmocraft.gui.MessageType;
import org.unallied.mmocraft.gui.control.TextCtrl;
import org.unallied.mmocraft.net.handlers.ChatMessageHandler;
import org.unallied.mmocraft.tools.Authenticator;

public class ChatFrame extends Frame {
	
	/**
	 * The color of "say" messages.
	 */
	private static final Color SAY_COLOR   = new Color(255, 255, 255);
	
	/**
	 * The color of "world" messages.
	 */
	private static final Color WORLD_COLOR = new Color(227, 115, 40);
	
	private static final String MESSAGE_FONT = FontID.STATIC_TEXT_MEDIUM.toString();

	/**
	 * The distance from the top of the chat frame that messageTextCtrl is positioned.
	 * Making this number larger will increase number of messages that can be displayed.
	 */
	private int chatHeight = 120;
	
	/**
	 * Determines the type of message that the player is writing.
	 */
	private MessageType messageType;
	
	/**
	 * Stores the message that the player is typing.
	 */
	private TextCtrl messageTextCtrl;

//TODO:  Add these buttons.
    /**
     * When clicked, this button will expand to offer multiple types / colors<br />
     * of chat.  For example:<br />
     * <orange>World</orange><br />
     * <white>Say</white><br />
     * <green>Guild</green><br />
     * <blue>Party</blue>
     */
/*    private Button chatTypeButton;
    
    private Button worldButton;
    private Button sayButton;
*/    
    private List<ChatMessage> receivedMessages = new ArrayList<ChatMessage>();
    
    /**
     * Initializes a LoginFrame with its elements (e.g. Username, Password fields)
     * @param x The x offset for this frame (from the parent GUI element)
     * @param y The y offset for this frame (from the parent GUI element)
     */
    public ChatFrame(final Frame parent, EventIntf intf, GameContainer container
            , float x, float y, int width, int height) {
        super(parent, intf, container, x, y, width, height);

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
        }, container, "", 0, chatHeight, -1, -1, ImageID.TEXTCTRL_CHAT_NORMAL.toString()
                , ImageID.TEXTCTRL_CHAT_HIGHLIGHTED.toString()
                , ImageID.TEXTCTRL_CHAT_SELECTED.toString(), 0);
        messageTextCtrl.setMaxLength(Authenticator.MAX_MESSAGE_LENGTH);

        elements.add(messageTextCtrl);
        
        setType(MessageType.SAY);
        this.width  = messageTextCtrl.getWidth();
        this.height = chatHeight + messageTextCtrl.getHeight();
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
        if (message.startsWith("/s ")) {
        	setType(MessageType.SAY);
        	messageTextCtrl.setLabel("");
        } else if (message.startsWith("/w ")) {
        	setType(MessageType.WORLD);
        	messageTextCtrl.setLabel("");
        }
    }

    @Override
    public boolean isAcceptingTab() {
        return false;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
    	if (!hidden) {
	    	int lineHeight = FontHandler.getInstance().getFont(MESSAGE_FONT).getLineHeight();
	    	int lineY = this.chatHeight - lineHeight;
	    	for (int i = this.receivedMessages.size()-1; i >= 0 && lineY >= 0; --i, lineY -= lineHeight) {
	    		ChatMessage message = receivedMessages.get(i);
	    		FontHandler.getInstance().draw(MESSAGE_FONT, message.getBody(), 0, lineY, getTypeColor(message.getType()), width, height, false);
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
    }

    
    @Override
    public void renderImage(Image image) {
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
    }
    
    /**
     * Adds a new message to the chat frame.  These messages appear
     * above the <code>messageTextCtrl</code>.
     * @param message
     */
    public void addMessage(ChatMessage message) {
    	Font font = FontHandler.getInstance().getFont(MESSAGE_FONT);
    	String[] words = message.getBody().split(" ");
    	String line = "[" + message.getAuthor() + "]: ";
    	
    	/*
    	 *  Go through each character and separate the message into chunks.
    	 *  This is done so that the message can be read in the chat frame
    	 *  without the message extending past the end of the frame.
    	 */
    	for (String word : words) {
    		if (font.getWidth(line + word) < width) {
    			line += word + " ";
    		} else if (line.length() == 0) {
       			/*
    			 *  The word was too long for an entire line!
    			 *  We should split it and display it on multiple lines, but I'm
    			 *  lazy.  For now we'll just display what we can.
    			 *  TODO:  Display huge words on multiple lines.
    			 */
    			line += word;
    			receivedMessages.add(new ChatMessage(message.getType(), line));
    			line = "";
    		} else {
    			receivedMessages.add(new ChatMessage(message.getType(), line));
    			line = word + " ";
    		}
    	}
    	if (line.length() > 0) {
    		receivedMessages.add(new ChatMessage(message.getType(), line));
    	}
    	needsRefresh = true;
    }
    
	@Override
	protected boolean isAcceptingFocus() {
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
}
