package org.unallied.mmocraft.tools.input;

import java.io.IOException;

public class GenericSeekableLittleEndianAccessor 
        extends GenericLittleEndianAccessor implements SeekableLittleEndianAccessor {
    private SeekableInputStreamBytestream bs;

    public GenericSeekableLittleEndianAccessor(SeekableInputStreamBytestream bs) {
        super(bs);
        this.bs = bs;
    }

    @Override
    /**
     * Seeks to a position in the stream
     * @param offset the offset to seek to from the start of the stream
     */
    public void seek(long offset) {
        try {
            bs.seek(offset);
        } catch (IOException e) {
            // failed to seek
        }
    }
    
    @Override
    /**
     * Gets the current position in the stream
     * @return the current position in the stream
     */
    public long getPosition() {
        try {
            return bs.getPosition();
        } catch (IOException e) {
            return -1;
        }
    }
    
    @Override
    /**
     * Skips <code>num</code> number of bytes in the stream
     * @param num the number of bytes to skip
     */
    public void skip(int num) {
        seek(getPosition() + num);
    }
}
