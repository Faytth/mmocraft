package org.unallied.mmocraft.net.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.unallied.mmocraft.chat.ChatMessage;
import org.unallied.mmocraft.client.MMOClient;
import org.unallied.mmocraft.gui.MessageType;
import org.unallied.mmocraft.net.AbstractPacketHandler;
import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;

/**
 * This class is used to handle chat packets received from the server.
 * These packets are sent when another player sends a message.
 * [author] [type] [message]
 * @author Faythless
 *
 */
public class ChatMessageHandler extends AbstractPacketHandler {
	/** A queue of all chat messages that need to be added to the chat frame. */
	private static List<ChatMessage> messageQueue = new LinkedList<ChatMessage>();

	/** Locks for the message queue to prevent "badness." */
    private static final ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
    private static final Lock writeLock = locks.writeLock();

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MMOClient client) {
    	String author = slea.readPrefixedAsciiString();
    	MessageType type = MessageType.fromValue(slea.readByte());
    	String body = slea.readPrefixedAsciiString();
    	ChatMessage message = new ChatMessage(type, body);
    	message.setAuthor(author);
    	writeLock.lock();
    	try {
    		messageQueue.add(message);
    	} finally {
    		writeLock.unlock();
    	}
    }
    
    /**
     * Returns the next message or null if no message is found.
     * @return chatMessage if one exists, else null
     */
    public static ChatMessage getNextMessage() {
    	ChatMessage result = null;
    	writeLock.lock();
    	try {
    		if (!messageQueue.isEmpty()) {
    			result = messageQueue.get(0);
    			messageQueue.remove(0);
    		}
    	} finally {
    		writeLock.unlock();
    	}
    	
    	return result;
    }

}