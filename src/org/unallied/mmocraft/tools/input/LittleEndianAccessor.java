package org.unallied.mmocraft.tools.input;

public interface LittleEndianAccessor {
    byte readByte();
    short readShort();
    int readInt();
    long readLong();
    void skip(int num);
    byte[] read(int num);
    float readFloat();
    double readDouble();
    String readAsciiString(int n);
    String readNullTerminatedAsciiString();
    String readPrefixedAsciiString();
    Object readObject();
    long getBytesRead();
    
    /**
     * Retrieves the number of bytes that are available to be read.
     * @return numberOfBytesAvailable
     */
    long available();
}