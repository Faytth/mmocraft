package org.unallied.mmocraft.gui;

/**
 * Contains the message type and the body of a message.  Used in the ChatFrame
 * for sending and displaying messages.
 * @author Alexandria
 *
 */
public class ChatMessage {
	private MessageType messageType;
	private String messageBody;
	/** The author of the message */
	private String author = "NULL";
	
	/**
	 * Creates a new ChatMessage.
	 * @param messageType the type of the message, such as Say, Guild, Party...
	 * @param messageBody the body of the message (i.e. what it contains),
	 *                    such as "Hello, my name is Alex."
	 */
	public ChatMessage(MessageType messageType, String messageBody) {
		this.messageType = messageType;
		this.messageBody = messageBody;
	}
	
	/**
	 * Creates a new ChatMessage.
	 * @param author the author (creator) of the message.
	 * @param messageType the type of the message, such as Say, Guild, Party...
	 * @param messageBody the body of the message (i.e. what it contains),
	 *                    such as "Hello, my name is Alex."
	 */
	public ChatMessage(String author, MessageType messageType, String messageBody) {
		this.author      = author;
		this.messageType = messageType;
		this.messageBody = messageBody;
	}
	
	public ChatMessage(ChatMessage message) {
		this.messageType = message.getType();
		this.messageBody = message.getBody();
		this.author      = message.getAuthor();
	}

	/**
	 * Returns the message type.
	 * @return messageType
	 */
	public MessageType getType() {
		return messageType;
	}
	
	/**
	 * Sets the type of the message, such as Say, Guild, Party, World.
	 * @param messageType
	 */
	public void setType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * Returns the body of the message.
	 * @return messageBody
	 */
	public String getBody() {
		return messageBody;
	}
	
	/**
	 * Sets the body of the message.
	 * @param messageBody the body of the message
	 */
	public void setBody(String messageBody) {
		this.messageBody = messageBody;
	}

	/**
	 * Retrieves the author of this message.  The author is the person who wrote
	 * the message.
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Sets the author of this message.  The author is the person who wrote the
	 * message.
	 * @param author the creator of the message
	 */
	public void setAuthor(String author) {
		if (author != null) {
			this.author = author;
		}
	}
}
