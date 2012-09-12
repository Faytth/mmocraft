package org.unallied.mmocraft.gui;

/**
 * An enumeration of all types of chat messages.  Chat messages are messages
 * sent between players or between the server and players.  They appear in the
 * chat frame.
 * @author Alexandria
 *
 */
public enum MessageType {
	WORLD(0x00),
	SAY(0x01),
	PARTY(0x02),
	GUILD(0x03);
	byte value = 0;
	
	MessageType(int value) {
		this.value = (byte) value;
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
}
