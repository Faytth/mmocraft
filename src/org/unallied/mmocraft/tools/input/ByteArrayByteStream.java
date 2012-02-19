package org.unallied.mmocraft.tools.input;

import java.io.IOException;

public class ByteArrayByteStream implements SeekableInputStreamBytestream {
    private int pos = 0;
    private long bytesRead = 0;
    private byte[] data;

    public ByteArrayByteStream(byte[] data) {
        this.data = data;
    }

    @Override
    /**
     * Returns the current byte and increments position by one.
     * @return the current byte
     */
    public int readByte() {
        ++bytesRead;
        return ((int) data[pos++]) & 0xFF;
    }
    
    @Override
    /**
     * Returns the number of bytes read
     * @return bytes read
     */
    public long getBytesRead() {
        return bytesRead;
    }

    @Override
    /**
     * Returns the number of remaining bytes that are available to read
     * @return the number of bytes available
     */
    public long available() {
        return data.length - pos;
    }

    @Override
    public void seek(long offset) throws IOException {
        pos = (int) offset;
    }

    @Override
    /**
     * Returns the current position of the stream from the beginning
     * @return the current position
     */
    public long getPosition() throws IOException {
        return pos;
    }
}
