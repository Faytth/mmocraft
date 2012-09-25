package org.unallied.mmocraft.gui.frame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.unallied.mmocraft.client.FontHandler;
import org.unallied.mmocraft.client.FontID;
import org.unallied.mmocraft.client.Game;
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
	
	/**
	 * Index from the bottom of the chat frame.  In a way, this is a "reverse" index.
	 * If a new message is added and this index != 0, then the index needs to be
	 * incremented by the number of messages added.  This index should never go above
	 * <code>receivedMessages.size()</code>.
	 */
	private int lineIndex = 0;

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
    public void renderImage(Image image) {
    	try {
    		image.setAlpha(1);
    		Graphics g = image.getGraphics();
    		g.clear();
	    	int lineHeight = FontHandler.getInstance().getFont(MESSAGE_FONT).getLineHeight();
	    	int lineY = this.chatHeight - lineHeight;
	    	int messageCount = receivedMessages.size();
	    	for (int i = this.receivedMessages.size()-1-lineIndex; i >= 0 && lineY >= 0 && i < messageCount; --i, lineY -= lineHeight) {
	    		ChatMessage message = receivedMessages.get(i);
	    		FontHandler.getInstance().draw(MESSAGE_FONT, message.getBody(), 5, lineY, getTypeColor(message.getType()), width, height, false);
	    	}
    	} catch (SlickException e) {
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
            GradientFill scrollFill = new GradientFill(0, 0, new Color(120, 160, 160, 50),
                    0, scrollBarLength/4, new Color(220, 220, 220), true);
            g.fill(new Rectangle(offX, scrollYOffset + offY, 
                    4, scrollBarLength/2), scrollFill);
            g.fill(new Rectangle(offX, scrollYOffset + offY + scrollBarLength/2, 
                    4, scrollBarLength-scrollBarLength/2), scrollFill.getInvertedCopy());
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game
            , Graphics g) {
        
        if (!hidden) {
            if (width > 0 && height > 0) {
                Image image = ImagePool.getInstance().getImage(this, width, height);
                if( image != null) {
                    if (ImagePool.getInstance().needsRefresh() || needsRefresh) {
                        renderImage(image);
                        // We need to flush the data to prevent graphical errors
                        try {
                            image.getGraphics().flush();
                        } catch (SlickException e) {
                        }
                        needsRefresh = false;
                    }
                    if (isActive()) { // Show background
                        g.fill(new Rectangle(getAbsoluteWidth(), getAbsoluteHeight(), width, chatHeight), 
                                new GradientFill(0, 0, new Color(0, 0, 0, 150),
                                        0, chatHeight/2, new Color(0, 0, 0, 150)));
                    }
                    image.draw(getAbsoluteWidth(), getAbsoluteHeight());
                    
                    // Render scrollbar
                    renderScrollBar(g);
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
    		if (font.getWidth(line + word) < width - 5) {
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
    			lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
    			line = "";
    		} else {
    			receivedMessages.add(new ChatMessage(message.getType(), line));
    			lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
    			line = word + " ";
    		}
    	}
    	if (line.length() > 0) {
    		receivedMessages.add(new ChatMessage(message.getType(), line));
    		lineIndex = lineIndex == 0 ? lineIndex : lineIndex + 1;
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
