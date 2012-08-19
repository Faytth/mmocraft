package org.unallied.mmocraft.tools.input;

import java.io.IOException;

import org.unallied.mmocraft.tools.input.ByteInputStream;

public abstract class SeekableInputStreamBytestream extends ByteInputStream {
    /**
     * Seeks the stream by the specified offset.
     *
     * @param offset
     *            Number of bytes to seek.
     * @throws IOException
     */
    public abstract void seek(long offset) throws IOException;

    /**
     * Gets the current position of the stream.
     *
     * @return The stream position as a long integer.
     * @throws IOException
     */
    public abstract long getPosition() throws IOException;
}
