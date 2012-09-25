package org.unallied.mmocraft.tools.input;

public interface LittleEndianAccessor {
    public byte readByte();
    public short readShort();
    
    /**
     * Reads out an int 7 bits at a time.  The high bit of the byte when on
     * means to continue reading more bytes.
     * @return int
     */
    public int read7BitEncodedInt();
    
    public int readInt();
    public long readLong();
    public void skip(int num);
    public byte[] read(int num);
    public float readFloat();
    public double readDouble();
    public String readAsciiString(int n);
    public String readNullTerminatedAsciiString();
    public String readPrefixedAsciiString();
    
    /**
     * Reads an ASCII string that has been prefixed with a 7-bit encoded int
     * for size.
     * @return string
     */
    public String read7BitPrefixedAsciiString();

    public Object readObject();
    public long getBytesRead();
    
    /**
     * Retrieves the number of bytes that are available to be read.
     * @return numberOfBytesAvailable
     */
    public long available();
}
