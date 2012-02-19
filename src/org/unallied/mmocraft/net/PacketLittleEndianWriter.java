package org.unallied.mmocraft.net;

import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * Used to create a Packet from a stream of bytes
 * @author Faythless
 *
 */
public class PacketLittleEndianWriter extends GenericLittleEndianWriter {
    
    public PacketLittleEndianWriter() {
        this(32);
    }
    
    /**
     * Initializes the byte array output stream with <code>size</code>
     * @param size
     */
    public PacketLittleEndianWriter(int size) {
        super(size);
    }
    
    public Packet getPacket() {
        return new MMOPacket(this.toByteArray());
    }
}
