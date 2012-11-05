package org.unallied.mmocraft.gui;

/**
 * An enumeration of all types of chat messages.  Chat messages are messages
 * sent between players or between the server and players.  They appear in the
 * chat frame.
 * @author Alexandria
 *
 */
public enum MessageType {
	WORLD(0x00, "World"),
	SAY(0x01, "Say"),
	PARTY(0x02, "Party"),
	GUILD(0x03, "Guild");
	byte value = 0;
	String name;
	
	MessageType(int value, String name) {
		this.value = (byte) value;
		this.name = name;
	}
	
	public byte getValue() {
		return (byte) value;
	}
	
	public static MessageType fromValue(byte value) {
		for (MessageType mt : MessageType.values()) {
			if (mt.value == value) {
				return mt;
			}
		}
		
		return null; // not found
	}
	
	@Override
	public String toString() {
	    return name;
	}
}
