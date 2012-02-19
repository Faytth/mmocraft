package org.unallied.mmocraft.net;

import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;
import org.unallied.mmocraft.tools.output.LittleEndianWriter;

/**
 * The main packet class for the MMORPG
 * @author Faythless
 *
 */
public class MMOPacket implements Packet {
    private byte[] data;
    private Runnable onSend;

    public MMOPacket(byte[] data) {
        this.data = data;
    }
    
    @Override
    /**
     * Returns the payload + 4 bytes for the length of the payload. e.g.:
     * [length][header][payload]
     */
    public byte[] getBytes() {
        LittleEndianWriter writer = new GenericLittleEndianWriter();
        writer.writeInt(data.length);
        writer.write(data);
        return writer.toByteArray();
    }

    @Override
    public Runnable getOnSend() {
        return onSend;
    }

    @Override
    public void setOnSend(Runnable onSend) {
        this.onSend = onSend;
    }

}
